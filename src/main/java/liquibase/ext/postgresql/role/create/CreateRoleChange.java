package liquibase.ext.postgresql.role.create;

import java.text.MessageFormat;

import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.postgresql.role.drop.DropRoleChange;
import liquibase.ext.postgresql.role.RoleChange;
import liquibase.ext.postgresql.role.RoleOptions;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "createRole", description = "Create role", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateRoleChange extends RoleChange {

  @Override
  protected Change[] createInverses() {
    DropRoleChange inverse = new DropRoleChange();
    inverse.setRoleName(getRoleName());
    return new Change[]{inverse,};
  }

  @Override
  public String getConfirmationMessage() {
    return MessageFormat.format("Role {0} created", getRoleName());
  }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    CreateRoleStatement statement = new CreateRoleStatement();

    statement.setRoleName(getRoleName());

    if (getOptions() != null) {
      RoleOptions roleOptions = new RoleOptions();
      roleOptions.setAttributesFromElement(getOptions());
      statement.setRoleOptions(roleOptions);
    }

    return new SqlStatement[]{statement};
  }

}
