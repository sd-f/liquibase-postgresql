package liquibase.ext.postgresql.droprole;

import java.text.MessageFormat;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "dropRole", description = "Drop role", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class DropRoleChange extends AbstractChange {

  private String roleName;

  @Override
  public String getConfirmationMessage() {
    return MessageFormat.format("Role {0} dropped", getRoleName());
  }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    DropRoleStatement statement = new DropRoleStatement();

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
