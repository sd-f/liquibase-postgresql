package liquibase.ext.postgresql.createrole;

import java.text.MessageFormat;
import java.util.Date;

import liquibase.change.AbstractChange;
import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.postgresql.droprole.DropRoleChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "createRole", description = "Create role", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateRoleChange extends AbstractChange {

  private String roleName;
  private String password;
  private Boolean superUser = false;
  private Boolean createDatabase = false;
  private Boolean createRole = false;
  private Boolean inherit = false;
  private Boolean loginAllowed = false;
  private Integer connectionLimit;
  private Boolean encryptedPassword;
  private Date validUntil;

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
    statement.setPassword(getPassword());

    statement.setSuperUser(isSuperUser());
    statement.setLoginAllowed(isLoginAllowed());

    statement.setCreateRole(isCreateRole());

    statement.setEncryptedPassword(isEncryptedPassword());
    statement.setConnectionLimit(getConnectionLimit());
    statement.setValidUntil(getValidUntil());
    statement.setInherit(isInherit());
    statement.setCreateDatabase(isCreateDatabase());

    return new SqlStatement[]{statement};
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean isSuperUser() {
    return superUser;
  }

  public void setSuperUser(Boolean superUser) {
    this.superUser = superUser;
  }

  public Boolean isCreateDatabase() {
    return createDatabase;
  }

  public void setCreateDatabase(Boolean createDatabase) {
    this.createDatabase = createDatabase;
  }

  public Boolean isCreateRole() {
    return createRole;
  }

  public void setCreateRole(Boolean createRole) {
    this.createRole = createRole;
  }

  public Boolean isInherit() {
    return inherit;
  }

  public void setInherit(Boolean inherit) {
    this.inherit = inherit;
  }

  public Boolean isLoginAllowed() {
    return loginAllowed;
  }

  public void setLoginAllowed(Boolean loginAllowed) {
    this.loginAllowed = loginAllowed;
  }

  public Integer getConnectionLimit() {
    return connectionLimit;
  }

  public void setConnectionLimit(Integer connectionLimit) {
    this.connectionLimit = connectionLimit;
  }

  public Boolean isEncryptedPassword() {
    return encryptedPassword;
  }

  public void setEncryptedPassword(Boolean encryptedPassword) {
    this.encryptedPassword = encryptedPassword;
  }

  public Date getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(Date validUntil) {
    this.validUntil = validUntil;
  }

}
