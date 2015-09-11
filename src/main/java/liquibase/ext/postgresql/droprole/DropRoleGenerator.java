package liquibase.ext.postgresql.droprole;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.postgresql.validation.AdvancedValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.structure.DatabaseObject;

public class DropRoleGenerator extends AbstractSqlGenerator<DropRoleStatement> {

  @Override
  public boolean supports(DropRoleStatement statement, Database database) {
    return database instanceof PostgresDatabase;
  }

  @Override
  public ValidationErrors validate(DropRoleStatement statement, Database database, SqlGeneratorChain chain) {
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();
    validationErrors.checkRequired("roleName", statement.getRoleName());
    return validationErrors;
  }

  /**
   * creates statement for dropping role in postgres sql<br />
   * <p>
   * @param statement
   * @param database
   * @param chain
   * @return <p>
   * @see @ http://www.postgresql.org/docs/9.4/static/sql-droprole.html where option can be:<br />
   * <pre>
   * {@code
   * DROP ROLE name [, ...]
   * }
   * </pre>
   */
  @Override
  public Sql[] generateSql(DropRoleStatement statement, Database database, SqlGeneratorChain chain) {
    StringBuilder sql = new StringBuilder("DROP ROLE ");

    sql.append(database.escapeObjectName(statement.getRoleName(), DatabaseObject.class));
    sql.append(" ");

    return new Sql[]{new UnparsedSql(sql.toString())};
  }

}
