package org.basex.query.func.file;

import java.nio.file.*;

import org.basex.query.*;
import org.basex.query.value.item.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-23, BSD License
 * @author Christian Gruen
 */
public final class FileIsFile extends FileFn {
  @Override
  public Item item(final QueryContext qc) throws QueryException {
    return Bln.get(Files.isRegularFile(toPath(arg(0), qc)));
  }
}
