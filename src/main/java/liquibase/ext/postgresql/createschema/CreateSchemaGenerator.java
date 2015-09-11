package liquibase.ext.postgresql.createschema;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.postgresql.validation.AdvancedValidationErrors;
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
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();
    validationErrors.checkRequired("schemaName", statement.getSchemaName());
    return validationErrors;
  }

  /**
   * creates statement for creating schema in postgres sql
   * <br />
   * <p>
   * @param statement
   * @param database
   * @param chain
   * @return <br />
   * @see @ http://www.postgresql.org/docs/9.4/static/sql-createschema.html where option can be:<br />
   * <pre>
   * {@code
   * CREATE SCHEMA schemaname [ AUTHORIZATION username ] [ schema_element [ ... ] ]
   * }
   * </pre>
   */
  @Override
  public Sql[] generateSql(CreateSchemaStatement statement, Database database, SqlGeneratorChain chain) {
    StringBuilder sql = new StringBuilder("CREATE SCHEMA ");

    sql.append(database.escapeObjectName(statement.getSchemaName(), DatabaseObject.class));
    sql.append(" ");

    if (statement.getAuthorization() != null && !statement.getAuthorization().isEmpty()) {
      sql.append("AUTHORIZATION ");
      sql.append(database.escapeObjectName(statement.getAuthorization(), DatabaseObject.class));
      sql.append(" ");
    }

    return new Sql[]{new UnparsedSql(sql.toString())};
  }

}
