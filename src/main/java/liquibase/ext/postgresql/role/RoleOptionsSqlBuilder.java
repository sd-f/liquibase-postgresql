/*
 */
package liquibase.ext.postgresql.role;

import liquibase.database.Database;
import liquibase.ext.postgresql.sql.SqlPropertyBuilder;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class RoleOptionsSqlBuilder extends SqlPropertyBuilder {

  Database database;

  public RoleOptionsSqlBuilder(StringBuilder sql, Database database) {
    super(sql);
    this.database = database;
  }

  public RoleOptionsSqlBuilder append(RoleOptions roleOptions) {
    append(roleOptions.getSuperUser(), "SUPERUSER", "NOSUPERUSER");
    append(roleOptions.getCreateDatabase(), "CREATEDB", "NOCREATEDB");
    append(roleOptions.getCreateRole(), "CREATEROLE", "NOCREATEROLE");
    append(roleOptions.getInherit(), "INHERIT", "NOINHERIT");
    append(roleOptions.getLoginAllowed(), "LOGIN", "NOLOGIN");

    append(roleOptions.getConnectionLimit(), "CONNECTION LIMIT");

    append(roleOptions.getEncryptedPassword(), "ENCRYPTED", "UNENCRYPTED");

    append(getDatabase().escapeStringForDatabase(roleOptions.getPassword()), "PASSWORD");
    append(roleOptions.getValidUntil(), "VALID UNTIL");
    append(roleOptions.getReplication(), "REPLICATION", "NOREPLICATION");
    return this;
  }

  public Database getDatabase() {
    return database;
  }

}
