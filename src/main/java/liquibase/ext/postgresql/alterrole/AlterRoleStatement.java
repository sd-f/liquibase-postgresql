package liquibase.ext.postgresql.alterrole;

import liquibase.statement.AbstractSqlStatement;

public class AlterRoleStatement extends AbstractSqlStatement {

  private String roleName;
  private String renameTo;
  private String ownerTo;

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getRenameTo() {
    return renameTo;
  }

  public void setRenameTo(String renameTo) {
    this.renameTo = renameTo;
  }

  public String getOwnerTo() {
    return ownerTo;
  }

  public void setOwnerTo(String ownerTo) {
    this.ownerTo = ownerTo;
  }

}
