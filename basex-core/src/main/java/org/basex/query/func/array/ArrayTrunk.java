package org.basex.query.func.array;

import static org.basex.query.QueryError.*;

import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.value.array.XQArray;
import org.basex.query.value.type.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-23, BSD License
 * @author Christian Gruen
 */
public final class ArrayTrunk extends ArrayFn {
  @Override
  public XQArray item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final XQArray array = toArray(arg(0), qc);
    if(array.isEmptyArray()) throw ARRAYEMPTY.get(info);
    return array.init();
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    final Type type = arg(0).seqType().type;
    if(type instanceof ArrayType) exprType.assign(type);
    return this;
  }
}
