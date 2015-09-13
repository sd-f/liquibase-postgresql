package liquibase.ext.postgresql.alterrole;

import java.text.MessageFormat;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.postgresql.alterrole.rename.AlterRoleRenameTo;
import liquibase.ext.postgresql.alterrole.rename.AlterRoleRenameToElement;
import liquibase.ext.postgresql.alterrole.reset.AlterRoleReset;
import liquibase.ext.postgresql.alterrole.reset.AlterRoleResetElement;
import liquibase.ext.postgresql.alterrole.set.AlterRoleSet;
import liquibase.ext.postgresql.alterrole.set.AlterRoleSetElement;
import liquibase.ext.postgresql.role.RoleOptions;
import liquibase.ext.postgresql.role.RoleOptionsElement;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "alterRole", description = "Alter role", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class AlterRoleChange extends AbstractChange {

  private String roleName;
  private RoleOptionsElement options;
  private AlterRoleRenameToElement renameTo;
  private AlterRoleSetElement set;
  private AlterRoleResetElement reset;

  @Override
  public String getConfirmationMessage() {
    return MessageFormat.format("Role {0} altered", getRoleName());
  }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    AlterRoleStatement statement = new AlterRoleStatement();

    statement.setRoleName(getRoleName());

    if (getOptions() != null) {
      RoleOptions optionsData = new RoleOptions();
      optionsData.setAttributesFromElement(getOptions());
      statement.setOptions(optionsData);
    }

    if (getRenameTo() != null) {
      AlterRoleRenameTo renameToData = new AlterRoleRenameTo();
      renameToData.setAttributesFromElement(getRenameTo());
      statement.setRenameTo(renameToData);
    }

    if (getSet() != null) {
      AlterRoleSet setData = new AlterRoleSet();
      setData.setAttributesFromElement(getSet());
      statement.setSet(setData);
    }

    if (getReset() != null) {
      AlterRoleReset resetData = new AlterRoleReset();
      resetData.setAttributesFromElement(getReset());
      statement.setReset(resetData);
    }

    return new SqlStatement[]{statement};
  }

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

  public AlterRoleRenameToElement getRenameTo() {
    return renameTo;
  }

  public void setRenameTo(AlterRoleRenameToElement renameTo) {
    this.renameTo = renameTo;
  }

  public AlterRoleSetElement getSet() {
    return set;
  }

  public void setSet(AlterRoleSetElement set) {
    this.set = set;
  }

  public AlterRoleResetElement getReset() {
    return reset;
  }

  public void setReset(AlterRoleResetElement reset) {
    this.reset = reset;
  }

}
