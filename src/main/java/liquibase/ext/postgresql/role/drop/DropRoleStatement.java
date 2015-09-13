package liquibase.ext.postgresql.role.drop;

import liquibase.statement.AbstractSqlStatement;

public class DropRoleStatement extends AbstractSqlStatement {

  private String roleName;

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

}
