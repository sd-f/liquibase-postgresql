package liquibase.ext.postgresql.vacuum;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

public class VacuumPostgres extends AbstractSqlGenerator<VacuumStatement> {

  @Override
  public int getPriority() {
    return 15;
  }

  @Override
  public boolean supports(VacuumStatement vacuumStatement, Database database) {
    return database instanceof PostgresDatabase;
  }

  @Override
  public ValidationErrors validate(VacuumStatement vacuumStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
    ValidationErrors validationErrors = new ValidationErrors();
    validationErrors.checkRequiredField("tableName", vacuumStatement.getTableName());
    return new ValidationErrors();
  }

  @Override
  public Sql[] generateSql(VacuumStatement vacuumStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
    StringBuilder sql = new StringBuilder("VACUUM ");
    sql.append(database.escapeTableName(vacuumStatement.getCatalogName(), vacuumStatement.getSchemaName(), vacuumStatement.getTableName()));
    return new Sql[]{
      new UnparsedSql(sql.toString())
    };
  }

}
