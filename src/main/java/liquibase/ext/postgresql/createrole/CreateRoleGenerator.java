package liquibase.ext.postgresql.createrole;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.postgresql.role.RoleOptionsSqlBuilder;
import liquibase.ext.postgresql.validation.AdvancedValidationErrors;
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
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();
    validationErrors.checkRequired("roleName", statement.getRoleName());
    validationErrors.checkRequired("options", statement.getRoleOptions());
    if (statement.getRoleOptions() != null) {
      validationErrors.checkRequired("password", statement.getRoleOptions().getPassword());
    }
    return validationErrors;
  }

  /**
   * creates statement for create role in postgres sql
   * <p>
   * @param statement
   * @param database
   * @param chain
   * @return <br />
   * @see @ http://www.postgresql.org/docs/9.4/static/sql-createrole.html where option can be:<br />
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
   * | REPLICATION | NOREPLICATION
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

    RoleOptionsSqlBuilder roleOptionsSqlBuilder = new RoleOptionsSqlBuilder(sql, database);
    roleOptionsSqlBuilder.append(statement.getRoleOptions());

    return new Sql[]{new UnparsedSql(sql.toString())};
  }

}
