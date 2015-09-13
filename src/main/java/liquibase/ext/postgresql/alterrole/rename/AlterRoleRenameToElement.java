/*
 */
package liquibase.ext.postgresql.alterrole.rename;

import liquibase.ext.postgresql.xml.Constants;
import liquibase.serializer.AbstractLiquibaseSerializable;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterRoleRenameToElement extends AbstractLiquibaseSerializable {

  private String name;

  @Override
  public String getSerializedObjectNamespace() {
    return Constants.NAMESPACE;
  }

  @Override
  public String getSerializedObjectName() {
    return "renameTo";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
