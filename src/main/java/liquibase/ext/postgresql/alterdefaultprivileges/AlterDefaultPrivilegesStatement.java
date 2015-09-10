package liquibase.ext.postgresql.alterdefaultprivileges;

import liquibase.ext.postgresql.grant.Operations;
import liquibase.ext.postgresql.grant.PrivilegesTargetType;
import liquibase.ext.postgresql.grant.GrantObjects;
import liquibase.statement.AbstractSqlStatement;

public class AlterDefaultPrivilegesStatement extends AbstractSqlStatement {

  private PrivilegesTargetType forType;
  private String inSchema;
  private String targetRole;

  private Boolean revoke = false;

  private Operations operation;
  private GrantObjects onObjects;
  private String toOrFromRole;
  private Boolean group = false;
  private Boolean withGrantOption;
  private Boolean cascade;
  private Boolean restrict;

  public PrivilegesTargetType getForType() {
    return forType;
  }

  public void setForType(PrivilegesTargetType forType) {
    this.forType = forType;
  }

  public String getInSchema() {
    return inSchema;
  }

  public void setInSchema(String inSchema) {
    this.inSchema = inSchema;
  }

  public String getTargetRole() {
    return targetRole;
  }

  public void setTargetRole(String targetRole) {
    this.targetRole = targetRole;
  }

  public Boolean getRevoke() {
    return revoke;
  }

  public void setRevoke(Boolean revoke) {
    this.revoke = revoke;
  }

  public Operations getOperation() {
    return operation;
  }

  public void setOperation(Operations operation) {
    this.operation = operation;
  }

  public GrantObjects getOnObjects() {
    return onObjects;
  }

  public void setOnObjects(GrantObjects onObjects) {
    this.onObjects = onObjects;
  }

  public String getToOrFromRole() {
    return toOrFromRole;
  }

  public void setToOrFromRole(String toOrFromRole) {
    this.toOrFromRole = toOrFromRole;
  }

  public Boolean getGroup() {
    return group;
  }

  public void setGroup(Boolean group) {
    this.group = group;
  }

  public Boolean getWithGrantOption() {
    return withGrantOption;
  }

  public void setWithGrantOption(Boolean withGrantOption) {
    this.withGrantOption = withGrantOption;
  }

  public Boolean getCascade() {
    return cascade;
  }

  public void setCascade(Boolean cascade) {
    this.cascade = cascade;
  }

  public Boolean getRestrict() {
    return restrict;
  }

  public void setRestrict(Boolean restrict) {
    this.restrict = restrict;
  }

}
