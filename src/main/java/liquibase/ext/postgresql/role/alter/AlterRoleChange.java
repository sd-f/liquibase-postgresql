package liquibase.ext.postgresql.role.alter;

import java.text.MessageFormat;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.postgresql.role.RoleChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "alterRole", description = "Alter role", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class AlterRoleChange extends RoleChange {

  private AlterRoleRenameTo renameTo;
  private AlterRoleSet set;
  private AlterRoleReset reset;

  @Override
  public String getConfirmationMessage() {
    return MessageFormat.format("Role {0} altered", getRoleName());
  }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    AlterRoleStatement statement = new AlterRoleStatement();

    statement.setRoleName(getRoleName());

    statement.setOptions(getOptions());
    statement.setRenameTo(getRenameTo());
    statement.setSet(getSet());
    statement.setReset(getReset());

    return new SqlStatement[]{statement};
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
