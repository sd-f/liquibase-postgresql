package liquibase.ext.postgresql.droprole;

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
