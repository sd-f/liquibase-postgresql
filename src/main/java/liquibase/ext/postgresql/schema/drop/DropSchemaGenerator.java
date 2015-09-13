package liquibase.ext.postgresql.schema.drop;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.postgresql.validation.AdvancedValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.structure.DatabaseObject;

public class DropSchemaGenerator extends AbstractSqlGenerator<DropSchemaStatement> {

  @Override
  public boolean supports(DropSchemaStatement statement, Database database) {
    return database instanceof PostgresDatabase;
  }

  @Override
  public ValidationErrors validate(DropSchemaStatement statement, Database database, SqlGeneratorChain chain) {
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();
    validationErrors.checkRequired("schemaName", statement.getSchemaName());
    if (statement.isCascade() != null && statement.isRestrict() != null
        && statement.isCascade() && statement.isRestrict()) {
      validationErrors.addError("Attributes \"restrict\" and \"cascade\" are excluding");
    }
    return validationErrors;
  }

  /**
   * creates statement for dropping role in postgres sql<br />
   * <p>
   * @param statement
   * @param database
   * @param chain
   * @return <p>
   * @see @ http://www.postgresql.org/docs/9.4/static/sql-dropschema.html where option can be:<br />
   * <pre>
   * {@code
   * DROP SCHEMA name [, ...] [ CASCADE | RESTRICT ]
   * }
   * </pre>
   */
  @Override
  public Sql[] generateSql(DropSchemaStatement statement, Database database, SqlGeneratorChain chain) {
    StringBuilder sql = new StringBuilder("DROP SCHEMA ");

    sql.append(database.escapeObjectName(statement.getSchemaName(), DatabaseObject.class));
    sql.append(" ");

    Boolean restrict = false;
    if (statement.isRestrict() != null) {
      restrict = statement.isRestrict();
    }

    if (restrict) {
      sql.append("RESTRICT ");
    } else {
      if (statement.isCascade() != null && statement.isCascade()) {
        sql.append("CASCADE ");
      }
    }

    return new Sql[]{new UnparsedSql(sql.toString())};
  }

}
