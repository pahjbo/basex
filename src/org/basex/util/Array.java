package org.basex.util;

/**
 * This class provides convenience methods for handling arrays.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Christian Gruen
 */
public final class Array {
  /** Empty integer array. */
  public static final int[] NOINTS = {};

  /** Private constructor. */
  private Array() { }

  /**
   * Doubles the array size.
   * @param ar array to be resized
   * @return finished array
   */
  public static boolean[] extend(final boolean[] ar) {
    final int s = ar.length;
    return resize(ar, s, s << 1);
  }

  /**
   * Doubles the array size.
   * @param ar array to be resized
   * @return finished array
   */
  public static char[] extend(final char[] ar) {
    final int s = ar.length;
    return resize(ar, s, s << 1);
  }

  /**
   * Doubles the array size.
   * @param ar array to be resized
   * @return finished array
   */
  public static int[] extend(final int[] ar) {
    final int s = ar.length;
    return resize(ar, s, s << 1);
  }

  /**
   * Doubles the array size.
   * @param ar array to be resized
   * @return finished array
   */
  public static int[][] extend(final int[][] ar) {
    final int s = ar.length;
    return resize(ar, s, s << 1);
  }

  /**
   * Doubles the array size.
   * @param ar array to be resized
   * @return finished array
   */
  public static long[] extend(final long[] ar) {
    final int s = ar.length;
    return resize(ar, s, s << 1);
  }

  /**
   * Doubles the array size.
   * @param ar array to be resized
   * @return finished array
   */
  public static byte[] extend(final byte[] ar) {
    final int s = ar.length;
    return resize(ar, s, s << 1);
  }

  /**
   * Doubles the array size.
   * @param ar array to be resized
   * @return finished array
   */
  public static byte[][] extend(final byte[][] ar) {
    final int s = ar.length;
    return resize(ar, s, s << 1);
  }

  /**
   * Doubles the array size.
   * @param ar array to be resized
   * @return finished array
   */
  public static String[] extend(final String[] ar) {
    final int s = ar.length;
    return resize(ar, s, s << 1);
  }

  /**
   * Doubles the array size.
   * @param ar array to be resized
   * @param <T> array type
   * @return array
   */
  public static <T> T[] extend(final T[] ar) {
    final int s = ar.length;
    return resize(ar, s, s << 1);
  }

  /**
   * Resizes an array and adds an entry at the end.
   * @param ar array to be resized
   * @param e entry to be added
   * @return finished array
   */
  public static byte[][] add(final byte[][] ar, final byte[] e) {
    final int s = ar.length;
    final byte[][] b = resize(ar, s, s + 1);
    b[s] = e;
    return b;
  }

  /**
   * Resizes an array and adds an entry at the end.
   * @param ar array to be resized
   * @param e entry to be added
   * @param <T> array type
   * @return array
   */
  public static <T> T[] add(final T[] ar, final T e) {
    final int s = ar.length;
    final T[] t = resize(ar, s, s + 1);
    t[s] = e;
    return t;
  }

  /**
   * Optimizes the array size.
   * @param ar array to be resized
   * @param size final size
   * @return finished array
   */
  public static int[] finish(final int[] ar, final int size) {
    final int s = ar.length;
    return resize(ar, s > size ? size : s, size);
  }

  /**
   * Optimizes the array size.
   * @param ar array to be resized
   * @param size final size
   * @return finished array
   */
  public static String[] finish(final String[] ar, final int size) {
    final int s = ar.length;
    return resize(ar, s > size ? size : s, size);
  }

  /**
   * Optimizes the array size.
   * @param ar array to be resized
   * @param size final size
   * @return finished array
   */
  public static boolean[] finish(final boolean[] ar, final int size) {
    final int s = ar.length;
    return resize(ar, s > size ? size : s, size);
  }

  /**
   * Optimizes the array size.
   * @param ar array to be resized
   * @param size final size
   * @return finished array
   */
  public static byte[] finish(final byte[] ar, final int size) {
    final int s = ar.length;
    return resize(ar, s > size ? size : s, size);
  }

  /**
   * Optimizes the array size.
   * @param ar array to be resized
   * @param size final size
   * @return finished array
   */
  public static byte[][] finish(final byte[][] ar, final int size) {
    final int s = ar.length;
    return resize(ar, s > size ? size : s, size);
  }

  /**
   * Optimizes the array size.
   * @param ar array to be resized
   * @param <T> array type
   * @param size final size
   * @return array
   */
  public static <T> T[] finish(final T[] ar, final int size) {
    final int s = ar.length;
    return resize(ar, s > size ? size : s, size);
  }

  /**
   * Convenience method for resizing a String array.
   * @param ar array to be resized
   * @param os old size
   * @param ns new size
   * @return resized array
   */
  public static String[] resize(final String[] ar, final int os, final int ns) {
    final String[] tmp = new String[ns];
    System.arraycopy(ar, 0, tmp, 0, os);
    return tmp;
  }

  /**
   * Convenience method for resizing a character array.
   * @param ar array to be resized
   * @param os old size
   * @param ns new size
   * @return resized array
   */
  public static char[] resize(final char[] ar, final int os, final int ns) {
    final char[] tmp = new char[ns];
    System.arraycopy(ar, 0, tmp, 0, os);
    return tmp;
  }

  /**
   * Convenience method for resizing an integer array.
   * @param ar array to be resized
   * @param os old size
   * @param ns new size
   * @return resized array
   */
  public static int[] resize(final int[] ar, final int os, final int ns) {
    final int[] tmp = new int[ns];
    System.arraycopy(ar, 0, tmp, 0, os);
    return tmp;
  }

  /**
   * Convenience method for resizing an integer array.
   * @param ar array to be resized
   * @param os old size
   * @param ns new size
   * @return resized array
   */
  public static int[][] resize(final int[][] ar, final int os, final int ns) {
    final int[][] tmp = new int[ns][];
    System.arraycopy(ar, 0, tmp, 0, os);
    return tmp;
  }

  /**
   * Convenience method for resizing an integer array.
   * @param ar array to be resized
   * @param os old size
   * @param ns new size
   * @return resized array
   */
  public static long[] resize(final long[] ar, final int os, final int ns) {
    final long[] tmp = new long[ns];
    System.arraycopy(ar, 0, tmp, 0, os);
    return tmp;
  }

  /**
   * Convenience method for resizing a boolean array.
   * @param ar array to be resized
   * @param os old size
   * @param ns new size
   * @return resized array
   */
  public static boolean[] resize(final boolean[] ar,
      final int os, final int ns) {
    final boolean[] tmp = new boolean[ns];
    System.arraycopy(ar, 0, tmp, 0, os);
    return tmp;
  }

  /**
   * Convenience method for resizing a byte array.
   * @param ar array to be resized
   * @param os old size
   * @param ns new size
   * @return resized array
   */
  public static byte[] resize(final byte[] ar, final int os, final int ns) {
    final byte[] tmp = new byte[ns];
    System.arraycopy(ar, 0, tmp, 0, os);
    return tmp;
  }

  /**
   * Convenience method for resizing a two-dimensional byte array.
   * @param ar array to be resized
   * @param os old size
   * @param ns new size
   * @return resized array
   */
  public static byte[][] resize(final byte[][] ar, final int os, final int ns) {
    final byte[][] tmp = new byte[ns][];
    System.arraycopy(ar, 0, tmp, 0, os);
    return tmp;
  }

  /**
   * Doubles the array size.
   * @param ar array to be resized
   * @param os old size
   * @param ns new size
   * @param <T> array type
   * @return array
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] resize(final T[] ar, final int os, final int ns) {
    final T[] copy = (T[]) java.lang.reflect.Array.newInstance(
        ar.getClass().getComponentType(), ns);
    System.arraycopy(ar, 0, copy, 0, os);
    return copy;
  }

  /**
   * Copies the source token into the target token.
   * @param src source array
   * @param trg target array
   * @param s start position
   */
  public static void copy(final byte[] src, final byte[] trg, final int s) {
    System.arraycopy(src, 0, trg, s, src.length);
  }

  /**
   * Copies the source array into the target array, starting from
   * the specified offset.
   * @param src source array
   * @param trg target array
   * @param s start position
   */
  public static void copy(final Object[] src, final Object[] trg, final int s) {
    System.arraycopy(src, 0, trg, s, src.length);
  }

  /**
   * Creates a new array from the source array and with the specified length.
   * @param src source array
   * @param pos array position
   * @param len array length
   * @return new array
   */
  public static byte[] create(final byte[] src, final int pos, final int len) {
    final byte[] tmp = new byte[len];
    System.arraycopy(src, pos, tmp, 0, len);
    return tmp;
  }

  /**
   * Moves entries inside an array.
   * @param ar array
   * @param pos position
   * @param off move offset
   * @param l length
   */
  public static void move(final Object ar, final int pos, final int off,
      final int l) {
    System.arraycopy(ar, pos, ar, pos + off, l);
  }

  /**
   * Convert a byte array to a positive integer value.
   * @param b byte array
   * @return result
   */
  public static int byteToInt(final byte[] b) {
    int i = 0;
    for(int j = 0, p = 0; j < b.length; j++, p += 8) i += (b[j] & 0xff) << p;
    return i;
  }
  
  /**
   * Convert integer to byte array, does only work for values > 0.
   * @param i integer
   * @return result
   */
  public static byte[] intToByteNN(final int i) {
    if(i == 0) return new byte[] { 0 };

    final int l = i < 0x100 ? 1 : i < 0x10000 ? 2 : i < 0x1000000 ? 3 : 4;
    final byte[] b = new byte[l];
    for(int c = 0, j = i; c < l; c++) {
      b[c] = (byte) j;
      j >>>= 8;
    }
    return b;
  }
 
  /**
   * Extracts ids out of an int[2][]-array.
   * int[0] = ids
   * int[1] = position values
   * @param data data
   * @return ids int[]
   */
  public static int[] extractIDsFromData(final int[][] data) {
    if(data == null || data.length == 0 || data[0] == null 
        || data[0].length == 0) return NOINTS;

    final int l = data[0].length;
    final int[] max = new int[l];
    max[0] = data[0][0];
    int c = 1;
    for(int i = 1; i < l; i++) {
      final int j = data[0][i];
      if(max[c - 1] != j) max[c++] = j;
    }
    return finish(max, c);
  }

  /**
   * Compares 2 int[1][2] array entries and returns.
   *  0 for equality
   * -1 if intArrayEntry1 < intArrayEntry2 (same id) or
   *  1  if intArrayEntry1 > intArrayEntry2 (same id)
   *  2  real bigger (different id)
   * -2 real smaller (different id)
   *
   * @param id1 first id
   * @param p1 first position
   * @param id2 second id
   * @param p2 second position
   * @return result [0|-1|1|2|-2]
   */
  public static int compareIntArrayEntry(final int id1, final int p1,
      final int id2, final int p2) {

    // equal ID, equal pos or data1 behind/before data2
    if(id1 == id2) return p1 == p2 ? 0 : p1 > p2 ? 1 : -1;
    // real bigger/smaller
    return id1 > id2 ? 2 : -2;
  }

  /**
   * Checks whether i is contained in a. 
   * @param i value to be found
   * @param a array to be checked
   * @return boolean contained
   */
  public static boolean contains(final int i, final int[] a) {
    return firstIndexOf(i, a) != -1;
  }

  /**
   * Checks whether p is contained in a. 
   * returns 
   *    -1 if i is not contained in a.
   *    first index of p if it is contained.
   *    
   * @param p value to be found
   * @param a array to be checked
   * @return int index of p in a contained
   */
  public static int firstIndexOf(final int p, final int[] a) {
    if(a == null || a.length == 0) return -1;
    int f = 0;
    int l = a.length - 1;

    while(f <= l) {
      int m = f + ((l - f) >> 1);
      if(a[m] < p) {
        f = m + 1; // continue right
      } else if(a[m] > p) {
        l = m - 1; // continue left
      } else {
        // find first index
        while(m > -1 && a[m] == p) m--;
        return m + 1;
      }
    }
    return -1;
  }
}

