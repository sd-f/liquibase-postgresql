/*
 */
package liquibase.ext.postgresql.role.alter;

import liquibase.ext.postgresql.xml.Constants;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterRoleReset extends AlterRoleParameter {

  @Override
  public String getSerializedObjectNamespace() {
    return Constants.NAMESPACE;
  }

  @Override
  public String getSerializedObjectName() {
    return "reset";
  }

}
