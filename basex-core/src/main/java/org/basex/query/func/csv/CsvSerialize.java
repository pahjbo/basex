package org.basex.query.func.csv;

import static org.basex.query.QueryError.*;

import org.basex.build.csv.*;
import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.func.*;
import org.basex.query.iter.*;
import org.basex.query.value.item.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-23, BSD License
 * @author Christian Gruen
 */
public final class CsvSerialize extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter input = arg(0).iter(qc);
    final CsvSerialOptions options = toOptions(arg(1), new CsvSerialOptions(), true, qc);

    final SerializerOptions sopts = new SerializerOptions();
    sopts.set(SerializerOptions.METHOD, SerialMethod.CSV);
    sopts.set(SerializerOptions.CSV, options);
    return Str.get(serialize(input, sopts, INVALIDOPT_X, qc));
  }
}
