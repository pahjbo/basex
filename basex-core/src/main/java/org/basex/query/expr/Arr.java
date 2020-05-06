package org.basex.query.expr;

import static org.basex.query.QueryText.*;

import java.util.function.*;

import org.basex.query.*;
import org.basex.query.CompileContext.*;
import org.basex.query.func.Function;
import org.basex.query.func.fn.*;
import org.basex.query.util.*;
import org.basex.query.util.list.*;
import org.basex.query.value.*;
import org.basex.query.value.type.*;
import org.basex.query.var.*;
import org.basex.util.*;
import org.basex.util.hash.*;

/**
 * Abstract array expression.
 *
 * @author BaseX Team 2005-20, BSD License
 * @author Christian Gruen
 */
public abstract class Arr extends ParseExpr {
  /** Expressions. */
  public Expr[] exprs;

  /**
   * Constructor.
   * @param info input info
   * @param seqType sequence type
   * @param exprs expressions
   */
  protected Arr(final InputInfo info, final SeqType seqType, final Expr... exprs) {
    super(info, seqType);
    this.exprs = exprs;
  }

  @Override
  public void checkUp() throws QueryException {
    checkNoneUp(exprs);
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    final int el = exprs.length;
    for(int e = 0; e < el; e++) exprs[e] = exprs[e].compile(cc);
    return optimize(cc);
  }

  @Override
  public boolean has(final Flag... flags) {
    for(final Expr expr : exprs) {
      if(expr.has(flags)) return true;
    }
    return false;
  }

  @Override
  public boolean inlineable(final Var var) {
    for(final Expr expr : exprs) {
      if(!expr.inlineable(var)) return false;
    }
    return true;
  }

  @Override
  public VarUsage count(final Var var) {
    return VarUsage.sum(var, exprs);
  }

  @Override
  public Expr inline(final Var var, final Expr ex, final CompileContext cc) throws QueryException {
    return inlineAll(var, ex, exprs, cc) ? optimize(cc) : null;
  }

  /**
   * Inlines an expression into this one, replacing all variable or context references.
   * @param var variable to replace
   * @param ex expression to inline
   * @param cc compilation context
   * @param context function for context inlining
   * @return resulting expression if something changed, {@code null} otherwise
   * @throws QueryException query exception
   */
  public Expr inline(final Var var, final Expr ex, final CompileContext cc,
      final QueryFunction<Void, Expr> context) throws QueryException {

    // no arguments are inlined: return null or apply context inlining function
    if(!inlineAll(var, ex, exprs, cc))
      return var != null ? null : context.apply(null);

    // optimize new expression. skip further optimizations if variable was inlined
    final Expr opt = optimize(cc);
    if(var != null) return opt;

    // inline again if context was inlined
    final Expr inlined = opt.inline(null, ex, cc);
    return inlined != null ? inlined : opt;
  }

  /**
   * Creates a deep copy of the given array.
   * @param <T> element type
   * @param cc compilation context
   * @param vm variable mapping
   * @param arr array to copy
   * @return deep copy of the array
   */
  @SuppressWarnings("unchecked")
  public static <T extends Expr> T[] copyAll(final CompileContext cc, final IntObjMap<Var> vm,
      final T[] arr) {

    final T[] copy = arr.clone();
    final int cl = copy.length;
    for(int c = 0; c < cl; c++) copy[c] = (T) copy[c].copy(cc, vm);
    return copy;
  }

  /**
   * Returns true if all arguments are values (possibly of small size).
   * @param limit check if result size of any expression exceeds {@link CompileContext#MAX_PREEVAL}
   * @return result of check
   */
  protected final boolean allAreValues(final boolean limit) {
    for(final Expr expr : exprs) {
      if(!(expr instanceof Value) || (limit && expr.size() > CompileContext.MAX_PREEVAL))
        return false;
    }
    return true;
  }

  /**
   * Simplifies all expressions for requests of the specified type.
   * @param mode mode of simplification
   * @param cc compilation context
   * @return {@code true} if at least one expression has changed
   * @throws QueryException query exception
   */
  protected boolean simplifyAll(final Simplify mode, final CompileContext cc)
      throws QueryException {

    boolean changed = false;
    final int el = exprs.length;
    for(int e = 0; e < el; e++) {
      final Expr expr = exprs[e].simplifyFor(mode, cc);
      if(expr != exprs[e]) {
        exprs[e] = expr;
        changed = true;
      }
    }
    return changed;
  }

  /**
   * Flattens nested expressions.
   * @param cc compilation context
   * @param clazz expressions to be flattened
   */
  protected void flatten(final CompileContext cc, final Class<? extends Arr> clazz) {
    // flatten nested expressions
    final ExprList list = new ExprList(exprs.length);
    for(final Expr expr : exprs) {
      if(clazz.isInstance(expr)) {
        list.add(((Arr) expr).exprs);
        cc.info(OPTFLAT_X_X, expr, (Supplier<?>) this::description);
      } else {
        list.add(expr);
      }
    }
    exprs = list.finish();
  }

  /**
   * Returns the first expression that yields an empty sequence. If all expressions return non-empty
   * results, the original expression is returned.
   * @return empty or original expression
   */
  final Expr emptyExpr() {
    // pre-evaluate if one value is empty (e.g.: () = local:expensive() )
    for(final Expr expr : exprs) {
      if(expr.seqType().zero()) return expr;
    }
    return this;
  }

  /**
   * Tries to merge consecutive EBV tests.
   * @param or union or intersection
   * @param positional consider positional tests
   * @param cc compilation context
   * @return {@code true} if evaluation can be skipped
   * @throws QueryException query exception
   */
  public boolean optimizeEbv(final boolean or, final boolean positional, final CompileContext cc)
      throws QueryException {

    final ExprList list = new ExprList(exprs.length);
    boolean pos = false;
    for(final Expr expr : exprs) {
      // pre-evaluate values
      if(expr instanceof Value) {
        // skip evaluation: true() or $bool  ->  true()
        if(expr.ebv(cc.qc, info).bool(info) ^ !or) return true;
        // ignore result: true() and $bool  ->  $bool
        cc.info(OPTREMOVE_X_X, expr, (Supplier<?>) this::description);
      } else if(!pos && list.contains(expr) && !expr.has(Flag.NDT)) {
        // ignore duplicates: A[$node and $node]  ->  A[$node]
        cc.info(OPTREMOVE_X_X, expr, (Supplier<?>) this::description);
      } else {
        list.add(expr);
        // preserve entries after positional predicates
        if(positional && !pos) pos = mayBePositional(expr);
      }
    }
    exprs = list.next();

    if(exprs.length > 1 && !(positional && has(Flag.POS))) {
      final Class<? extends Logical> clazz = or ? And.class : Or.class;
      final Checks<Expr> logical = expr -> clazz.isInstance(expr);
      if(logical.all(exprs)) {
        // (A and B) or (A and C)  ->  A and (B or C)
        // (A or B) and (A or C) and (A or D)  ->  A or (B and C and D)
      } else if(logical.any(exprs)) {
        // A or (A and B)  ->  A
        // A and (A or B) and (A or C or D) ->  A
        Expr root = null;
        for(final Expr expr : exprs) {
          if(clazz.isInstance(expr)) continue;
          root = root == null ? expr : null;
          if(root == null) break;
        }
        if(root != null) {
          final Expr rt = root;
          if(((Checks<Expr>) expr ->
            expr == rt || ((Checks<Expr>) ex -> ex.equals(rt)).any(((Logical) expr).exprs)
          ).all(exprs)) {
            exprs = new Expr[] { FnBoolean.get(root, info, cc.sc()) };
          }
        }
      }
    }

    // 'a'[. = 'a' or . = 'b']  ->  'a'[. = ('a', 'b')]
    // $v[. != 'a'][. != 'b']  ->  $v[not(. = ('a', 'b')]
    list.add(exprs);
    for(int l = 0; l < list.size(); l++) {
      for(int m = l + 1; m < list.size(); m++) {
        final Expr expr = list.get(l);
        if(!(positional && expr.has(Flag.POS))) {
          final Expr merged = expr.mergeEbv(list.get(m), or, cc);
          if(merged != null) {
            cc.info(OPTSIMPLE_X_X, (Supplier<?>) this::description, this);
            list.set(l, merged);
            list.remove(m--);
          }
        }
      }
    }
    exprs = list.finish();

    // not($a) and not($b)  ->  not($a or $b)
    final Checks<Expr> fnNot = ex -> Function.NOT.is(ex) && !(positional && ex.has(Flag.POS));
    if(exprs.length > 1 && fnNot.all(exprs)) {
      final ExprList tmp = new ExprList(exprs.length);
      for(final Expr expr : exprs) tmp.add(((FnNot) expr).exprs[0]);
      final Expr expr = or ? new And(info, tmp.finish()) : new Or(info, tmp.finish());
      exprs = new Expr[] { cc.function(Function.NOT, info, expr.optimize(cc)) };
    }
    return false;
  }

  /**
   * Checks if the specified expression may be positional.
   * @param expr expression
   * @return result of check
   */
  protected static boolean mayBePositional(final Expr expr) {
    return expr.seqType().mayBeNumber() || expr.has(Flag.POS);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return visitAll(visitor, exprs);
  }

  @Override
  public int exprSize() {
    int size = 1;
    for(final Expr expr : exprs) size += expr.exprSize();
    return size;
  }

  /**
   * {@inheritDoc}
   * Must be overwritten by implementing class.
   */
  @Override
  public boolean equals(final Object obj) {
    return obj instanceof Arr && Array.equals(exprs, ((Arr) obj).exprs);
  }

  @Override
  public void plan(final QueryPlan plan) {
    plan.add(plan.create(this), exprs);
  }

  /**
   * Returns a string representation of this expression.
   * Separates the array entries with the specified separator.
   * @param separator separator (operator, delimiter)
   * @return string representation
   */
  protected String toString(final String separator) {
    return new TokenBuilder().add(PAREN1).addSeparated(exprs, separator).add(PAREN2).toString();
  }
}
