package liquibase.ext.postgresql.droprole;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
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
    ValidationErrors validationErrors = new ValidationErrors();
    validationErrors.checkRequiredField("roleName", statement.getRoleName());
    return validationErrors;
  }

  /**
   * creates statement for dropping role in postgres sql
   * <p>
   * @param statement
   * @param database
   * @param chain
   * @return <p>
   * @see @ http://www.postgresql.org/docs/8.1/static/sql-droprole.html where option can be:
   * <p>
   * DROP ROLE name [, ...]
   */
  @Override
  public Sql[] generateSql(DropRoleStatement statement, Database database, SqlGeneratorChain chain) {
    StringBuilder sql = new StringBuilder("DROP ROLE ");

    sql.append(database.escapeObjectName(statement.getRoleName(), DatabaseObject.class));
    sql.append(" ");

    return new Sql[]{new UnparsedSql(sql.toString())};
  }

}
