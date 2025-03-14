package org.basex.query.func.rest;

import org.basex.http.web.*;
import org.basex.query.*;
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
public final class RestInit extends ApiFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final boolean update = toBooleanOrFalse(arg(0), qc);

    WebModules.get(qc.context).init(update);
    return Empty.VALUE;
  }
}
