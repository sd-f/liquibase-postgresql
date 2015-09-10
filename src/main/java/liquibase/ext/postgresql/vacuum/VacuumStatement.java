package liquibase.ext.postgresql.vacuum;

import liquibase.statement.AbstractSqlStatement;

public class VacuumStatement extends AbstractSqlStatement {

  private String tableName;
  private String schemaName;
  private String catalogName;

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  public String getCatalogName() {
    return catalogName;
  }

  public void setCatalogName(String catalogName) {
    this.catalogName = catalogName;
  }

}
