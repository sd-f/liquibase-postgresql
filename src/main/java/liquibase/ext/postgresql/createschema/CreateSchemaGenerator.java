package liquibase.ext.postgresql.createschema;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.structure.DatabaseObject;

public class CreateSchemaGenerator extends AbstractSqlGenerator<CreateSchemaStatement> {

  @Override
  public boolean supports(CreateSchemaStatement statement, Database database) {
    return database instanceof PostgresDatabase;
  }

  @Override
  public ValidationErrors validate(CreateSchemaStatement statement, Database database, SqlGeneratorChain chain) {
    ValidationErrors validationErrors = new ValidationErrors();
    validationErrors.checkRequiredField("schemaName", statement.getSchemaName());
    return validationErrors;
  }

  /**
   * creates statement for dropping role in postgres sql
   * <p>
   * @param statement
   * @param database
   * @param chain
   * @return <p>
   * @see @ http://www.postgresql.org/docs/8.1/static/sql-createschema.html where option can be:
   * <p>
   * CREATE SCHEMA schemaname [ AUTHORIZATION username ] [ schema_element [ ... ] ]
   */
  @Override
  public Sql[] generateSql(CreateSchemaStatement statement, Database database, SqlGeneratorChain chain) {
    StringBuilder sql = new StringBuilder("CREATE SCHEMA ");

    sql.append(database.escapeObjectName(statement.getSchemaName(), DatabaseObject.class));
    sql.append(" ");

    if (statement.getAuthorization() != null) {
      sql.append("AUTHORIZATION ");
      sql.append(database.escapeObjectName(statement.getAuthorization(), DatabaseObject.class));
      sql.append(" ");
    }

    return new Sql[]{new UnparsedSql(sql.toString())};
  }

}
