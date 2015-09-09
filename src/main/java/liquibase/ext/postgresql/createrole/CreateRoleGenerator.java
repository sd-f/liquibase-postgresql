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
   * @return <br />
   * @see @ http://www.postgresql.org/docs/9.0/static/sql-createrole.html where option can be:<br />
   * <pre>
   * {@code
   * CREATE ROLE name [ [ WITH ] option [ ... ] ]
   * <p>
   * where option can be:
   * <p>
   * SUPERUSER | NOSUPERUSER
   * | CREATEDB | NOCREATEDB
   * | CREATEROLE | NOCREATEROLE
   * | CREATEUSER | NOCREATEUSER
   * | INHERIT | NOINHERIT
   * | LOGIN | NOLOGIN
   * | CONNECTION LIMIT connlimit
   * | [ ENCRYPTED | UNENCRYPTED ] PASSWORD 'password'
   * | VALID UNTIL 'timestamp'
   * | IN ROLE role_name [, ...]
   * | IN GROUP role_name [, ...]
   * | ROLE role_name [, ...]
   * | ADMIN role_name [, ...]
   * | USER role_name [, ...]
   * | SYSID uid
   * }
   * </pre>
   */
  @Override
  public Sql[] generateSql(CreateRoleStatement statement, Database database, SqlGeneratorChain chain) {
    StringBuilder sql = new StringBuilder("CREATE ROLE ");

    sql.append(database.escapeObjectName(statement.getRoleName(), DatabaseObject.class));
    sql.append(" ");

    appendProperties(sql, statement);

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

  private void appendProperties(StringBuilder sql, CreateRoleStatement statement) {
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
  }

}
