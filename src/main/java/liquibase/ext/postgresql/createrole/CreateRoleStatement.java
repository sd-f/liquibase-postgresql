package liquibase.ext.postgresql.createrole;

import liquibase.ext.postgresql.role.RoleOptions;
import liquibase.statement.AbstractSqlStatement;

public class CreateRoleStatement extends AbstractSqlStatement {

  private String roleName;

  private RoleOptions roleOptions;

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public RoleOptions getRoleOptions() {
    return roleOptions;
  }

  public void setRoleOptions(RoleOptions roleOptions) {
    this.roleOptions = roleOptions;
  }

}
