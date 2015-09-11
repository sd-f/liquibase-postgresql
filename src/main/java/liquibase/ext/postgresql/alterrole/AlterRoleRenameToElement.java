/*
 */
package liquibase.ext.postgresql.alterrole;

import liquibase.ext.postgresql.xml.Constants;
import liquibase.serializer.AbstractLiquibaseSerializable;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterRoleRenameToElement extends AbstractLiquibaseSerializable {

  private String role;

  @Override
  public String getSerializedObjectNamespace() {
    return Constants.NAMESPACE;
  }

  @Override
  public String getSerializedObjectName() {
    return "renameTo";
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

}
