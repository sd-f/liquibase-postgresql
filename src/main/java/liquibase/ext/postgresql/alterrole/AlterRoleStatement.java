package liquibase.ext.postgresql.alterrole;

import liquibase.statement.AbstractSqlStatement;

public class AlterRoleStatement extends AbstractSqlStatement {

  private String roleName;

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

}
