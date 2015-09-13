package liquibase.ext.postgresql.schema.alter;

import liquibase.statement.AbstractSqlStatement;

public class AlterSchemaStatement extends AbstractSqlStatement {

  private String schemaName;
  private String renameTo;
  private String ownerTo;

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  public String getRenameTo() {
    return renameTo;
  }

  public void setRenameTo(String renameTo) {
    this.renameTo = renameTo;
  }

  public String getOwnerTo() {
    return ownerTo;
  }

  public void setOwnerTo(String ownerTo) {
    this.ownerTo = ownerTo;
  }

}
