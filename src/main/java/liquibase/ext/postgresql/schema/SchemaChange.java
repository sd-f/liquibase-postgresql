package liquibase.ext.postgresql.schema;

import liquibase.change.AbstractChange;

public abstract class SchemaChange extends AbstractChange {

  private String schemaName;

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

}
