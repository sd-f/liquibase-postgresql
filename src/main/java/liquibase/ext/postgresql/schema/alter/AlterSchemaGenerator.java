package liquibase.ext.postgresql.schema.alter;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.postgresql.validation.AdvancedValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.structure.DatabaseObject;

public class AlterSchemaGenerator extends AbstractSqlGenerator<AlterSchemaStatement> {

  @Override
  public boolean supports(AlterSchemaStatement statement, Database database) {
    return database instanceof PostgresDatabase;
  }

  @Override
  public ValidationErrors validate(AlterSchemaStatement statement, Database database, SqlGeneratorChain chain) {
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();
    validationErrors.checkRequired("schemaName", statement.getSchemaName());

    Boolean owner = false;
    Boolean rename = false;

    if (statement.getRenameTo() != null) {
      validationErrors.checkRequired("renameTo", statement.getRenameTo());
      rename = true;
    }
    if (statement.getOwnerTo() != null) {
      validationErrors.checkRequired("ownerTo", statement.getOwnerTo());
      owner = true;
    }

    if (owner && rename) {
      validationErrors.addError("Elements \"ownerTo\" and \"renameTo\" are excluding");
    }

    if (!owner && !rename) {
      validationErrors.addError("Either \"ownerTo\" or \"renameTo\" is must be added");
    }

    return validationErrors;
  }

  /**
   * creates statement for altering schema in postgres sql<br />
   * <p>
   * @param statement
   * @param database
   * @param chain
   * @return <p>
   * @see http://www.postgresql.org/docs/9.4/static/sql-alterschema.html where option can be:<br />
   * <pre>
   * {@code
   * ALTER SCHEMA name RENAME TO new_name
   * ALTER SCHEMA name OWNER TO new_owner
   * }
   * </pre>
   */
  @Override
  public Sql[] generateSql(AlterSchemaStatement statement, Database database, SqlGeneratorChain chain) {
    StringBuilder sql = new StringBuilder("ALTER SCHEMA ");

    sql.append(database.escapeObjectName(statement.getSchemaName(), DatabaseObject.class));
    sql.append(" ");

    if (statement.getOwnerTo() != null) {
      sql.append("OWNER TO ");
      sql.append(database.escapeObjectName(statement.getOwnerTo(), DatabaseObject.class));
      sql.append(" ");
    } else {
      sql.append("RENAME TO ");
      sql.append(database.escapeObjectName(statement.getRenameTo(), DatabaseObject.class));
      sql.append(" ");
    }

    return new Sql[]{new UnparsedSql(sql.toString())};
  }

}
