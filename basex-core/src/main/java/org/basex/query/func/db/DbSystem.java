package org.basex.query.func.db;

import org.basex.core.cmd.*;
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
public final class DbSystem extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    return DbInfo.toNode(DbAccess.Q_SYSTEM, Info.info(qc.context), qc);
  }
}
