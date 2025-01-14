package org.basex.query.func.lazy;

import org.basex.query.*;
import org.basex.query.func.*;
import org.basex.query.value.item.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-23, BSD License
 * @author Christian Gruen
 */
public final class LazyIsLazy extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Bln.get(toAtomItem(arg(0), qc) instanceof Lazy);
  }
}
