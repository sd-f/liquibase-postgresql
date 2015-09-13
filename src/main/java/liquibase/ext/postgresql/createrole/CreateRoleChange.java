package liquibase.ext.postgresql.createrole;

import java.text.MessageFormat;

import liquibase.change.AbstractChange;
import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.postgresql.droprole.DropRoleChange;
import liquibase.ext.postgresql.role.RoleOptions;
import liquibase.ext.postgresql.role.RoleOptionsElement;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "createRole", description = "Create role", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateRoleChange extends AbstractChange {

  private String roleName;
  private RoleOptionsElement options;

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

}
