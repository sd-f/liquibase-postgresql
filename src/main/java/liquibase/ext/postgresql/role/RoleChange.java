/*
 */
package liquibase.ext.postgresql.role;

import liquibase.change.AbstractChange;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public abstract class RoleChange extends AbstractChange {

  private String roleName;
  private RoleOptions options;

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public RoleOptions getOptions() {
    return options;
  }

  public void setOptions(RoleOptions options) {
    this.options = options;
  }

}
