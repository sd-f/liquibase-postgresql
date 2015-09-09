/*
 */
package liquibase.ext.postgresql.grant;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class RevokeChange extends AbstractGrantChange {

  private Boolean cascade;
  private Boolean restrict;
  private String fromRole;

  @Override
  public String getSerializedObjectName() {
    return "revoke";
  }

  public Boolean getCascade() {
    return cascade;
  }

  public void setCascade(Boolean cascade) {
    this.cascade = cascade;
  }

  public Boolean getRestrict() {
    return restrict;
  }

  public void setRestrict(Boolean restrict) {
    this.restrict = restrict;
  }

  public String getFromRole() {
    return fromRole;
  }

  public void setFromRole(String fromRole) {
    this.fromRole = fromRole;
  }

}
