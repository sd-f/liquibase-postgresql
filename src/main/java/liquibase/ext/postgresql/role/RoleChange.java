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
  private RoleOptionsElement options;

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public RoleOptionsElement getOptions() {
    return options;
  }

  public void setOptions(RoleOptionsElement options) {
    this.options = options;
  }

}
