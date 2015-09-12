/*
 */
package liquibase.ext.postgresql.role;

import liquibase.database.Database;
import liquibase.ext.postgresql.sql.SqlBuilder;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class RoleOptionsSqlBuilder extends SqlBuilder {

  Database database;

  public RoleOptionsSqlBuilder(StringBuilder sql, Database database) {
    super(sql);
    this.database = database;
  }

  public RoleOptionsSqlBuilder append(RoleOptions roleOptions) {
    append(roleOptions.getSuperUser(), "SUPERUSER", "NOSUPERUSER", false);
    append(roleOptions.getCreateDatabase(), "CREATEDB", "NOCREATEDB", false);
    append(roleOptions.getCreateRole(), "CREATEROLE", "NOCREATEROLE", false);
    append(roleOptions.getInherit(), "INHERIT", "NOINHERIT", false);
    append(roleOptions.getInherit(), "LOGIN", "NOLOGIN", false);

    append(roleOptions.getConnectionLimit(), "CONNECTION LIMIT");

    append(roleOptions.getEncryptedPassword(), "ENCRYPTED", "UNENCRYPTED");

    append(getDatabase().escapeStringForDatabase(roleOptions.getPassword()), "PASSWORD");
    append(roleOptions.getValidUntil(), "VALID UNTIL");
    return this;
  }

  public Database getDatabase() {
    return database;
  }

}
