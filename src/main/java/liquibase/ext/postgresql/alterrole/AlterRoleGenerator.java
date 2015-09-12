package liquibase.ext.postgresql.alterrole;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.postgresql.validation.AdvancedValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.structure.DatabaseObject;

public class AlterRoleGenerator extends AbstractSqlGenerator<AlterRoleStatement> {

  @Override
  public boolean supports(AlterRoleStatement statement, Database database) {
    return database instanceof PostgresDatabase;
  }

  @Override
  public ValidationErrors validate(AlterRoleStatement statement, Database database, SqlGeneratorChain chain) {
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();
    validationErrors.checkRequired("roleName", statement.getRoleName());
    return validationErrors;
  }

  /**
   * creates statement for altering role in postgres sql<br />
   * <p>
   * @param statement
   * @param database
   * @param chain
   * @return <p>
   * @see @ http://www.postgresql.org/docs/9.4/static/sql-alterrole.html where option can be:<br />
   * <pre>
   * {@code
   * ALTER ROLE name [ [ WITH ] option [ ... ] ]
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
   * <p>
   * ALTER ROLE name RENAME TO new_name
   * <p>
   * ALTER ROLE name [ IN DATABASE database_name ] SET configuration_parameter { TO | = } { value | DEFAULT }
   * ALTER ROLE { name | ALL } [ IN DATABASE database_name ] SET configuration_parameter FROM CURRENT
   * ALTER ROLE { name | ALL } [ IN DATABASE database_name ] RESET configuration_parameter
   * ALTER ROLE { name | ALL } [ IN DATABASE database_name ] RESET ALL
   * }
   * </pre>
   */
  @Override
  public Sql[] generateSql(AlterRoleStatement statement, Database database, SqlGeneratorChain chain) {
    StringBuilder sql = new StringBuilder("ALTER ROLE ");

    sql.append(database.escapeObjectName(statement.getRoleName(), DatabaseObject.class));
    sql.append(" ");

    return new Sql[]{new UnparsedSql(sql.toString())};
  }

}
