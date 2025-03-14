package org.basex.query.func.ws;

import org.basex.http.ws.*;
import org.basex.query.*;
import org.basex.query.value.item.*;
import org.basex.query.value.seq.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-23, BSD License
 * @author Christian Gruen
 */
public final class WsEmit extends WsFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    WsPool.emit(toItem(arg(0), qc));
    return Empty.VALUE;
  }
}
