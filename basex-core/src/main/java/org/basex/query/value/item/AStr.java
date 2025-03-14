package org.basex.query.value.item;

import java.util.*;

import org.basex.query.*;
import org.basex.query.util.*;
import org.basex.query.util.collation.*;
import org.basex.query.value.type.*;
import org.basex.util.*;
import org.basex.util.list.*;

/**
 * Abstract string item.
 *
 * @author BaseX Team 2005-23, BSD License
 * @author Christian Gruen
 */
public abstract class AStr extends Item {
  /** ASCII offset flag. */
  private static final int[] ASCII = {};

  /** String data ({@code null} if not initialized yet). */
  byte[] value;
  /** Character offsets. {@code null}: unknown, {@code ASCII}: ASCII, otherwise: offsets. */
  private int[] offsets;

  /**
   * Constructor.
   */
  AStr() {
    super(AtomType.STRING);
  }

  /**
   * Constructor, specifying a type and value.
   * @param value value
   * @param type atomic type
   */
  AStr(final byte[] value, final Type type) {
    super(type);
    this.value = value;
  }

  @Override
  public final boolean bool(final InputInfo ii) throws QueryException {
    return string(ii).length != 0;
  }

  /**
   * Checks if the string only consists of ASCII characters.
   * @param ii input info
   * @return result of check
   * @throws QueryException query exception
   */
  public boolean ascii(final InputInfo ii) throws QueryException {
    if(offsets == null) {
      final byte[] token = string(ii);
      if(Token.ascii(token)) {
        offsets = ASCII;
      } else {
        final IntList list = new IntList();
        final int tl = token.length;
        for(int t = 0; t < tl; t += Token.cl(token, t)) list.add(t);
        offsets = list.finish();
      }
    }
    return offsets == ASCII;
  }

  /**
   * Returns the string length.
   * @param ii input info
   * @return result of check
   * @throws QueryException query exception
   */
  public int length(final InputInfo ii) throws QueryException {
    return ascii(ii) ? string(ii).length : offsets.length;
  }

  /**
   * Returns a substring.
   * @param ii input info
   * @param start start position
   * @param end end position
   * @return substring
   * @throws QueryException query exception
   */
  public AStr substring(final InputInfo ii, final int start, final int end) throws QueryException {
    if(start == 0 && end == length(ii)) return this;

    final byte[] token = string(ii);
    final boolean ascii =  ascii(ii);
    final int s = ascii ? start : offsets[start];
    final int e = ascii ? end : end < offsets.length ? offsets[end] : token.length;
    return Str.get(Arrays.copyOfRange(token, s, e));
  }

  @Override
  public final boolean comparable(final Item item) {
    return item.type.isStringOrUntyped();
  }

  @Override
  public final boolean eq(final Item item, final Collation coll, final StaticContext sc,
      final InputInfo ii) throws QueryException {
    return Token.eq(string(ii), item.string(ii), coll);
  }

  @Override
  public final boolean deepEqual(final Item item, final DeepEqual deep) throws QueryException {
    return comparable(item) && Token.eq(string(deep.info), item.string(deep.info), deep);
  }

  @Override
  public final int diff(final Item item, final Collation coll, final InputInfo ii)
      throws QueryException {
    return Token.diff(string(ii), item.string(ii), coll);
  }

  @Override
  public boolean equals(final Object obj) {
    if(this == obj) return true;
    if(!(obj instanceof AStr)) return false;
    final AStr a = (AStr) obj;
    return type == a.type && Token.eq(value, a.value);
  }

  @Override
  public void toString(final QueryString qs) {
    qs.quoted(value);
  }
}
