package liquibase.ext.postgresql.createrole;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.structure.DatabaseObject;

public class CreateRoleGenerator extends AbstractSqlGenerator<CreateRoleStatement> {

  @Override
  public boolean supports(CreateRoleStatement statement, Database database) {
    return database instanceof PostgresDatabase;
  }

  @Override
  public ValidationErrors validate(CreateRoleStatement statement, Database database, SqlGeneratorChain chain) {
    ValidationErrors validationErrors = new ValidationErrors();
    validationErrors.checkRequiredField("roleName", statement.getRoleName());
    validationErrors.checkRequiredField("password", statement.getPassword());
    return validationErrors;
  }

  /**
   * creates statement for create role in postgres sql
   * <p>
   * @param statement
   * @param database
   * @param chain
   * @return <p>
   * @see @ http://www.postgresql.org/docs/8.1/static/sql-createrole.html where option can be:
   * <p>
   * CREATE ROLE name [ [ WITH ] option [ ... ] ]
   * <p>
   * SUPERUSER | NOSUPERUSER<p>
   * | CREATEDB | NOCREATEDB<p>
   * | CREATEROLE | NOCREATEROLE<p>
   * | CREATEUSER | NOCREATEUSER<p>
   * | INHERIT | NOINHERIT<p>
   * | LOGIN | NOLOGIN<p>
   * | CONNECTION LIMIT connlimit<p>
   * | [ ENCRYPTED | UNENCRYPTED ] PASSWORD 'password'
   * <p>
   * | VALID UNTIL 'timestamp'
   * <p>
   * | IN ROLE rolename [, ...]
   * <p>
   * | IN GROUP rolename [, ...]
   * <p>
   * | ROLE rolename [, ...]
   * <p>
   * | ADMIN rolename [, ...]
   * <p>
   * | USER rolename [, ...]
   * <p>
   * | SYSID uid<p>
   */
  @Override
  public Sql[] generateSql(CreateRoleStatement statement, Database database, SqlGeneratorChain chain) {
    StringBuilder sql = new StringBuilder("CREATE ROLE ");

    sql.append(database.escapeObjectName(statement.getRoleName(), DatabaseObject.class));
    sql.append(" ");

    if (statement.isSuperUser() != null && statement.isSuperUser()) {
      sql.append("SUPERUSER ");
    } else {
      sql.append("NOSUPERUSER ");
    }

    if (statement.isCreateDatabase() != null && statement.isCreateDatabase()) {
      sql.append("CREATEDB ");
    } else {
      sql.append("NOCREATEDB ");
    }

    if (statement.isCreateRole() != null && statement.isCreateRole()) {
      sql.append("CREATEROLE ");
    } else {
      sql.append("NOCREATEROLE ");
    }

    if (statement.isInherit() != null && statement.isInherit()) {
      sql.append("INHERIT ");
    } else {
      sql.append("NOINHERIT ");
    }

    if (statement.isLoginAllowed() != null && statement.isLoginAllowed()) {
      sql.append("LOGIN ");
    } else {
      sql.append("NOLOGIN ");
    }

    if (statement.getConnectionLimit() != null) {
      sql.append("CONNECTION LIMIT ");
      sql.append(statement.getConnectionLimit());
      sql.append(" ");
    }

    if (statement.isEncryptedPassword() != null) {
      if (statement.isEncryptedPassword()) {
        sql.append("ENCRYPTED ");
      } else {
        sql.append("UNENCRYPTED ");
      }
    }

    sql.append("PASSWORD '");
    sql.append(database.escapeStringForDatabase(statement.getPassword()));
    sql.append("' ");

    if (statement.getValidUntil() != null) {
      sql.append("VALID UNTIL '");
      sql.append(statement.getValidUntil());
      sql.append("' ");
    }

    return new Sql[]{new UnparsedSql(sql.toString())};
  }

}
