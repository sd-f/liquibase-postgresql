/*
 */
package liquibase.ext.postgresql.role;

import java.math.BigInteger;

import liquibase.ext.postgresql.xml.Constants;
import liquibase.serializer.AbstractLiquibaseSerializable;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class RoleOptions extends AbstractLiquibaseSerializable {

  private String password;
  private Boolean superUser;
  private Boolean createDatabase;
  private Boolean createRole;
  private Boolean inherit;
  private Boolean loginAllowed;
  private BigInteger connectionLimit;
  private Boolean encryptedPassword;
  private Boolean replication;
  private String validUntil;

  @Override
  public String getSerializedObjectName() {
    return "options";
  }

  @Override
  public String getSerializedObjectNamespace() {
    return Constants.NAMESPACE;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getSuperUser() {
    return superUser;
  }

  public void setSuperUser(Boolean superUser) {
    this.superUser = superUser;
  }

  public Boolean getCreateDatabase() {
    return createDatabase;
  }

  public void setCreateDatabase(Boolean createDatabase) {
    this.createDatabase = createDatabase;
  }

  public Boolean getCreateRole() {
    return createRole;
  }

  public void setCreateRole(Boolean createRole) {
    this.createRole = createRole;
  }

  public Boolean getInherit() {
    return inherit;
  }

  public void setInherit(Boolean inherit) {
    this.inherit = inherit;
  }

  public Boolean getLoginAllowed() {
    return loginAllowed;
  }

  public void setLoginAllowed(Boolean loginAllowed) {
    this.loginAllowed = loginAllowed;
  }

  public BigInteger getConnectionLimit() {
    return connectionLimit;
  }

  public void setConnectionLimit(BigInteger connectionLimit) {
    this.connectionLimit = connectionLimit;
  }

  public Boolean getEncryptedPassword() {
    return encryptedPassword;
  }

  public void setEncryptedPassword(Boolean encryptedPassword) {
    this.encryptedPassword = encryptedPassword;
  }

  public String getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(String validUntil) {
    this.validUntil = validUntil;
  }

  public Boolean getReplication() {
    return replication;
  }

  public void setReplication(Boolean replication) {
    this.replication = replication;
  }

}
