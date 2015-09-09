/*
 */
package liquibase.ext.postgresql.grant;

import liquibase.ext.postgresql.alterdefaultprivileges.GrantObjects;
import liquibase.ext.postgresql.alterdefaultprivileges.Operations;
import liquibase.ext.postgresql.xml.Constants;
import liquibase.serializer.AbstractLiquibaseSerializable;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public abstract class AbstractGrantChange extends AbstractLiquibaseSerializable {

  private Operations operation;
  private GrantObjects onObjects;
  private Boolean group = false;

  @Override
  public String getSerializedObjectNamespace() {
    return Constants.NAMESPACE;
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

  public Boolean getGroup() {
    return group;
  }

  public void setGroup(Boolean group) {
    this.group = group;
  }

}
