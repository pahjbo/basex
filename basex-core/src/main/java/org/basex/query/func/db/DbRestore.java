package org.basex.query.func.db;

import static org.basex.query.QueryError.*;

import org.basex.core.*;
import org.basex.query.*;
import org.basex.query.up.primitives.name.*;
import org.basex.query.value.item.*;
import org.basex.query.value.seq.*;
import org.basex.util.*;
import org.basex.util.list.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-23, BSD License
 * @author Christian Gruen
 */
public final class DbRestore extends BackupFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    // extract database name from backup file
    final String name = toName(arg(0), true, qc);

    // find backup with or without date suffix
    final StringList backups = qc.context.databases.backups(name);
    if(backups.isEmpty()) throw DB_NOBACKUP_X.get(info, name);

    final String backup = backups.get(0), db = Databases.name(backup);
    qc.updates().add(new DBRestore(db, backup, qc, info), qc);
    return Empty.VALUE;
  }
}
