package liquibase.ext.postgresql.createschema;

import liquibase.statement.AbstractSqlStatement;

public class CreateSchemaStatement extends AbstractSqlStatement {

  private String schemaName;
  private String authorization;

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  public String getAuthorization() {
    return authorization;
  }

  public void setAuthorization(String authorization) {
    this.authorization = authorization;
  }

}
