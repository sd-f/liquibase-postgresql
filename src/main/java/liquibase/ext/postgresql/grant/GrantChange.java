/*
 */
package liquibase.ext.postgresql.grant;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class GrantChange extends AbstractGrantChange {

  private Boolean withGrantOption;
  private String toRole;

  @Override
  public String getSerializedObjectName() {
    return "grant";
  }

  public Boolean getWithGrantOption() {
    return withGrantOption;
  }

  public void setWithGrantOption(Boolean withGrantOption) {
    this.withGrantOption = withGrantOption;
  }

  public String getToRole() {
    return toRole;
  }

  public void setToRole(String toRole) {
    this.toRole = toRole;
  }

}
