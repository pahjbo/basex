package org.basex.core.users;

import static org.basex.core.users.UserText.*;
import static org.basex.util.Token.*;
import static org.basex.util.XMLAccess.*;

import java.io.*;
import java.util.*;
import java.util.Map.*;
import java.util.regex.*;

import org.basex.build.*;
import org.basex.core.*;
import org.basex.io.*;
import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.value.item.*;
import org.basex.query.value.node.*;
import org.basex.util.*;
import org.basex.util.list.*;

/**
 * This class organizes all users.
 *
 * @author BaseX Team 2005-23, BSD License
 * @author Christian Gruen
 */
public final class Users {
  /** User array. */
  private final LinkedHashMap<String, User> users = new LinkedHashMap<>();
  /** Filename. */
  private final IOFile file;
  /** Info node (can be {@code null}). */
  private ANode info;

  /**
   * Constructor for global users.
   * @param sopts static options
   */
  public Users(final StaticOptions sopts) {
    file = sopts.dbPath(string(Q_USERS.string()) + IO.XMLSUFFIX);
    read();
    // ensure that default admin user exists
    if(get(ADMIN) == null) add(new User(ADMIN).perm(Perm.ADMIN));
  }

  /**
   * Reads user permissions.
   */
  private void read() {
    if(!file.exists()) return;
    try {
      final MainOptions options = new MainOptions(false);
      options.set(MainOptions.INTPARSE, true);
      options.set(MainOptions.STRIPWS, true);
      final ANode doc = new DBNode(Parser.singleParser(file, options, ""));
      final ANode root = children(doc, Q_USERS).next();
      if(root == null) {
        Util.errln("%: No <%/> root element.", file, Q_USERS);
      } else {
        for(final ANode child : children(root)) {
          final QNm qname = child.qname();
          if(qname.eq(Q_USER)) {
            try {
              final User user = new User(child, file);
              final String name = user.name();
              if(users.get(name) != null) {
                Util.errln("%: User '%' supplied more than once.", file, name);
              } else {
                users.put(name, user);
              }
            } catch(final BaseXException ex) {
              // reject users with faulty data
              Util.errln("%: %", file, ex.getLocalizedMessage());
            }
          } else if(qname.eq(Q_INFO)) {
            if(info != null) Util.errln("%: <%/> occurs more than once.", file, qname);
            else info = child.finish();
          } else {
            Util.errln("%: invalid element <%/>.", file, qname);
          }
        }
      }
    } catch(final IOException ex) {
      Util.errln(ex);
    }
  }

  /**
   * Writes permissions to disk.
   */
  public void write() {
    synchronized(users) {
      try {
        file.parent().md();
        final FBuilder root = FElem.build(Q_USERS);
        for(final User user : users.values()) {
          root.add(user.toXml(null, null));
        }
        if(info != null) {
          root.add(info);
          info.parent(null);
        }
        file.write(root.finish().serialize(SerializerMode.INDENT.get()).finish());
      } catch(final IOException | QueryException ex) {
        Util.errln(ex);
      }
    }
  }

  /**
   * Adds a user.
   * @param user user to be added
   */
  public void add(final User user) {
    synchronized(users) {
      users.put(user.name(), user);
    }
  }

  /**
   * Renames a user.
   * @param user user reference
   * @param name new name
   */
  public void alter(final User user, final String name) {
    synchronized(users) {
      users.remove(user.name());
      user.name(name);
      users.put(name, user);
    }
  }

  /**
   * Drops a user from the list.
   * @param user user reference
   * @return success flag
   */
  public boolean drop(final User user) {
    synchronized(users) {
      return users.remove(user.name()) != null;
    }
  }

  /**
   * Returns a user with the specified name.
   * @param name username
   * @return username or {@code null}
   */
  public User get(final String name) {
    synchronized(users) {
      return users.get(name);
    }
  }

  /**
   * Returns the names of all users that match the specified pattern.
   * @param pattern glob pattern
   * @return user list
   */
  public String[] find(final Pattern pattern) {
    final StringList sl = new StringList();
    synchronized(users) {
      for(final String name : users.keySet()) {
        if(pattern.matcher(name).matches()) sl.add(name);
      }
    }
    return sl.finish();
  }

  /**
   * Returns table with all users, or users from a specific database.
   * The list will only contain the current user if no admin permissions are available.
   * @param db database (can be {@code null})
   * @param ctx database context
   * @return user information
   */
  public Table info(final String db, final Context ctx) {
    final Table table = new Table();
    table.description = Text.USERS_X;

    for(final String user : S_USERINFO) table.header.add(user);
    for(final User user : users(db, ctx)) {
      table.contents.add(new TokenList().add(user.name()).add(user.perm(db).toString()));
    }
    return table.sort().toTop(token(ADMIN));
  }

  /**
   * Returns all users, or users that have permissions for a specific database.
   * The list will only contain the current user if no admin permissions are available.
   * @param db database (can be {@code null})
   * @param ctx database context
   * @return user information
   */
  public ArrayList<User> users(final String db, final Context ctx) {
    final User curr = ctx.user();
    final boolean admin = curr.has(Perm.ADMIN);
    final ArrayList<User> list = new ArrayList<>();
    synchronized(users) {
      for(final User user : users.values()) {
        if(admin || curr == user) {
          if(db == null) {
            list.add(user);
          } else {
            final Entry<String, Perm> entry = user.find(db);
            if(entry != null) list.add(user);
          }
        }
      }
    }
    return list;
  }

  /**
   * Returns the info element.
   * @return info element (can be {@code null})
   */
  public ANode info() {
    return info;
  }

  /**
   * Sets the info element.
   * @param elem info element
   */
  public void info(final ANode elem) {
    info = elem.hasChildren() || elem.attributeIter().size() != 0 ? elem : null;
  }
}
