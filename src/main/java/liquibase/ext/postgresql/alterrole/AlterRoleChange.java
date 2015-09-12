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

  @Override
  public String getConfirmationMessage() {
    return MessageFormat.format("Role {0} altered", getRoleName());
  }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    AlterRoleStatement statement = new AlterRoleStatement();

    statement.setRoleName(getRoleName());

    return new SqlStatement[]{statement};
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

}
