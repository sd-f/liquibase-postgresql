package liquibase.ext.postgresql.dropschema;

import liquibase.statement.AbstractSqlStatement;

public class DropSchemaStatement extends AbstractSqlStatement {

  private String schemaName;
  private Boolean cascade = false;
  private Boolean restrict = true;

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  public Boolean isCascade() {
    return cascade;
  }

  public void setCascade(Boolean cascade) {
    this.cascade = cascade;
  }

  public Boolean isRestrict() {
    return restrict;
  }

  public void setRestrict(Boolean restrict) {
    this.restrict = restrict;
  }

}
