package org.basex.query.func.fn;

import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.func.*;
import org.basex.query.value.item.*;
import org.basex.query.value.seq.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-23, BSD License
 * @author Christian Gruen
 */
public final class FnFloor extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final ANum value = toNumberOrNull(arg(0), qc);
    return value == null ? Empty.VALUE : value.floor();
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    return optFirst();
  }
}
