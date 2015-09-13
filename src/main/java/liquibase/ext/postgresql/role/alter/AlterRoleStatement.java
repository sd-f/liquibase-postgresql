package liquibase.ext.postgresql.role.alter;

import liquibase.ext.postgresql.role.alter.rename.AlterRoleRenameTo;
import liquibase.ext.postgresql.role.alter.reset.AlterRoleReset;
import liquibase.ext.postgresql.role.alter.set.AlterRoleSet;
import liquibase.ext.postgresql.role.RoleOptions;
import liquibase.statement.AbstractSqlStatement;

public class AlterRoleStatement extends AbstractSqlStatement {

  private String roleName;
  private RoleOptions options;
  private AlterRoleRenameTo renameTo;
  private AlterRoleSet set;
  private AlterRoleReset reset;

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

  public AlterRoleRenameTo getRenameTo() {
    return renameTo;
  }

  public void setRenameTo(AlterRoleRenameTo renameTo) {
    this.renameTo = renameTo;
  }

  public AlterRoleSet getSet() {
    return set;
  }

  public void setSet(AlterRoleSet set) {
    this.set = set;
  }

  public AlterRoleReset getReset() {
    return reset;
  }

  public void setReset(AlterRoleReset reset) {
    this.reset = reset;
  }

}
