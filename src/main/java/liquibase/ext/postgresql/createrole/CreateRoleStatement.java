package liquibase.ext.postgresql.createrole;

import java.util.Date;

import liquibase.statement.AbstractSqlStatement;

public class CreateRoleStatement extends AbstractSqlStatement {

  private String roleName;
  private String password;
  private Boolean superUser = false;
  private Boolean createRole = false;
  private Boolean createDatabase = false;
  private Boolean inherit = false;
  private Boolean loginAllowed = false;
  private Integer connectionLimit;
  private Boolean encryptedPassword;
  private Date validUntil;

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

  public Boolean isCreateDatabase() {
    return createDatabase;
  }

  public void setCreateDatabase(Boolean createDatabase) {
    this.createDatabase = createDatabase;
  }

  public Boolean isSuperUser() {
    return superUser;
  }

  public void setSuperUser(Boolean superUser) {
    this.superUser = superUser;
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
