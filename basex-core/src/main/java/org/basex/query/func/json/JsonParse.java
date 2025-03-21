package org.basex.query.func.json;

import static org.basex.query.QueryError.*;

import java.io.*;

import org.basex.build.json.*;
import org.basex.io.*;
import org.basex.io.parse.json.*;
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
public class JsonParse extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] value = toTokenOrNull(arg(0), qc);
    return value != null ? parse(new IOContent(value), qc) : Empty.VALUE;
  }

  @Override
  protected final Expr opt(final CompileContext cc) {
    return optFirst();
  }

  /**
   * Parses the input and creates an XML document.
   * @param io input data
   * @param qc query context
   * @return node
   * @throws QueryException query exception
   */
  protected final Item parse(final IO io, final QueryContext qc) throws QueryException {
    final JsonParserOptions options = toOptions(arg(1), new JsonParserOptions(), true, qc);
    try {
      return JsonConverter.get(options).convert(io);
    } catch(final IOException ex) {
      throw JSON_PARSE_X.get(info, ex);
    }
  }
}
