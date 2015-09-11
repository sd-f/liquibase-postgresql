package liquibase.ext.postgresql.alterrole;

import java.text.MessageFormat;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "alterRole", description = "Alter role", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class AlterRoleChange extends AbstractChange {

  private String roleName;
  private AlterRoleRenameToElement renameTo;
  private AlterRoleOwnerToElement ownerTo;

  @Override
  public String getConfirmationMessage() {
    return MessageFormat.format("Role {0} altered", getRoleName());
  }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    AlterRoleStatement statement = new AlterRoleStatement();

    statement.setRoleName(getRoleName());

    if (getRenameTo() != null) {
      statement.setRenameTo(getRenameTo().getRole());
    }
    if (getOwnerTo() != null) {
      statement.setOwnerTo(getOwnerTo().getOwner());
    }

    return new SqlStatement[]{statement};
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public AlterRoleRenameToElement getRenameTo() {
    return renameTo;
  }

  public void setRenameTo(AlterRoleRenameToElement renameTo) {
    this.renameTo = renameTo;
  }

  public AlterRoleOwnerToElement getOwnerTo() {
    return ownerTo;
  }

  public void setOwnerTo(AlterRoleOwnerToElement ownerTo) {
    this.ownerTo = ownerTo;
  }

}
