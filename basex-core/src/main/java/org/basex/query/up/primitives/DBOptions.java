package org.basex.query.up.primitives;

import static org.basex.query.QueryError.*;

import java.util.*;
import java.util.Map.Entry;

import org.basex.core.*;
import org.basex.query.*;
import org.basex.util.*;
import org.basex.util.options.*;

/**
 * Contains various helper variables and methods for database operations.
 *
 * @author BaseX Team 2005-23, BSD License
 * @author Christian Gruen
 */
public final class DBOptions {
  /** Runtime options. */
  private final HashMap<Option<?>, Object> map = new HashMap<>();

  /**
   * Constructor.
   * @param options query options
   * @param supported supported options
   * @param info input info
   * @throws QueryException query exception
   */
  public DBOptions(final HashMap<String, String> options, final List<Option<?>> supported,
      final InputInfo info) throws QueryException {
    this(options, supported.toArray(Option<?>[]::new), info);
  }

  /**
   * Constructor.
   * @param qopts query options
   * @param supported supported options
   * @param info input info
   * @throws QueryException query exception
   */
  public DBOptions(final HashMap<String, String> qopts, final Option<?>[] supported,
      final InputInfo info) throws QueryException {

    final HashMap<String, Option<?>> support = new HashMap<>();
    for(final Option<?> option : supported) {
      support.put(option.name().toLowerCase(Locale.ENGLISH), option);
    }

    for(final Entry<String, String> entry : qopts.entrySet()) {
      final String key = entry.getKey();
      final Option<?> option = support.get(key);
      if(option == null) throw BASEX_OPTIONS1_X.get(info, key);

      final String value = entry.getValue();
      if(option instanceof NumberOption) {
        final int v = Strings.toInt(value);
        if(v < 0) throw BASEX_OPTIONS_X_X.get(info, key, value);
        map.put(option, v);
      } else if(option instanceof BooleanOption) {
        final boolean yes = Strings.toBoolean(value);
        if(!yes && !Strings.no(value)) throw BASEX_OPTIONS_X_X.get(info, key, value);
        map.put(option, yes);
      } else if(option instanceof StringOption) {
        map.put(option, value);
      } else if(option instanceof EnumOption) {
        final EnumOption<?> eo = (EnumOption<?>) option;
        final Object ev = eo.get(value);
        if(ev == null) throw BASEX_OPTIONS_X_X.get(info, key, value);
        map.put(option, ev);
      } else if(option instanceof OptionsOption) {
        try {
          final Options o = ((OptionsOption<?>) option).newInstance();
          o.assign(value);
          map.put(option, o);
        } catch(final BaseXException ex) {
          throw BASEX_OPTIONS2_X.get(info, ex);
        }
      } else {
        throw Util.notExpected();
      }
    }
  }

  /**
   * Returns the value of the specified option.
   * @param option option
   * @return main options
   */
  public Object get(final Option<?> option) {
    return map.get(option);
  }

  /**
   * Assigns the specified option if it has not been assigned before.
   * @param option option
   * @param value value
   */
  public void assignIfAbsent(final Option<?> option, final Object value) {
    map.putIfAbsent(option, value);
  }

  /**
   * Assigns runtime options to the specified main options.
   * @param opts main options
   * @return main options
   */
  public MainOptions assignTo(final MainOptions opts) {
    map.forEach(opts::put);
    return opts;
  }
}
