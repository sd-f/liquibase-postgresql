/*
 */
package liquibase.ext.postgresql.role;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

import liquibase.logging.LogFactory;
import liquibase.logging.Logger;
import liquibase.util.ISODateFormat;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class RoleOptions {

  static Logger LOGGER = LogFactory.getInstance().getLog();

  private String password;
  private Boolean superUser;
  private Boolean createDatabase;
  private Boolean createRole;
  private Boolean inherit;
  private Boolean loginAllowed;
  private BigInteger connectionLimit;
  private Boolean encryptedPassword;
  private Date validUntil;

  public RoleOptions() {
  }

  public void setAttributesFromElement(RoleOptionsElement optionsElement) {
    if (optionsElement != null) {
      password = optionsElement.getPassword();
      connectionLimit = optionsElement.getConnectionLimit();
      createDatabase = optionsElement.getCreateDatabase();
      createRole = optionsElement.getCreateRole();
      encryptedPassword = optionsElement.getEncryptedPassword();
      inherit = optionsElement.getInherit();
      loginAllowed = optionsElement.getLoginAllowed();
      superUser = optionsElement.getSuperUser();
      validUntil = null;
      if (optionsElement.getValidUntil() != null) {
        try {
          validUntil = new ISODateFormat().parse(optionsElement.getValidUntil());
        } catch (ParseException ex) {
          LOGGER.severe("Error parsing options", ex);
        }
      }
    }
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

  public Date getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(Date validUntil) {
    this.validUntil = validUntil;
  }

}
