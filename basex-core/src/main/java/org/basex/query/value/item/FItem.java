package org.basex.query.value.item;

import static org.basex.query.QueryError.*;

import java.util.function.*;

import org.basex.data.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.func.*;
import org.basex.query.util.collation.*;
import org.basex.query.util.list.*;
import org.basex.query.value.map.*;
import org.basex.query.value.type.*;
import org.basex.util.*;

/**
 * Abstract super class for function items.
 * This class is inherited by {@link XQMap}, {@link Array}, and {@link FuncItem}.
 *
 * @author BaseX Team 2005-23, BSD License
 * @author Leo Woerteler
 */
public abstract class FItem extends Item implements XQFunction {
  /** Annotations. */
  final AnnList anns;

  /**
   * Constructor.
   * @param type function type
   * @param anns this function item's annotations
   */
  protected FItem(final Type type, final AnnList anns) {
    super(type);
    this.anns = anns;
  }

  @Override
  public final AnnList annotations() {
    return anns;
  }

  @Override
  public final boolean eq(final Item item, final Collation coll, final StaticContext sc,
      final InputInfo ii) throws QueryException {
    throw FIATOMIZE_X.get(ii, this);
  }

  @Override
  public final boolean atomicEqual(final Item item, final InputInfo ii) {
    return false;
  }

  @Override
  public void refineType(final Expr expr) {
    final FuncType t = funcType().intersect(expr.seqType().type);
    if(t != null) type = t;
  }

  @Override
  public final FuncType funcType() {
    return (FuncType) type;
  }

  @Override
  public Item materialize(final Predicate<Data> test, final InputInfo ii, final QueryContext qc)
      throws QueryException {
    throw BASEX_STORE_X.get(ii, this);
  }

  @Override
  public boolean materialized(final Predicate<Data> test, final InputInfo ii)
      throws QueryException {
    throw BASEX_STORE_X.get(ii, this);
  }

  /**
   * Coerces this function item to the given function type.
   * @param ft function type
   * @param qc query context
   * @param ii input info
   * @param optimize optimize resulting item
   * @return coerced item
   * @throws QueryException query exception
   */
  public abstract FItem coerceTo(FuncType ft, QueryContext qc, InputInfo ii, boolean optimize)
      throws QueryException;
}
