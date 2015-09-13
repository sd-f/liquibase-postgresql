/*
 */
package liquibase.ext.postgresql.role.alter;

import liquibase.ext.postgresql.xml.Constants;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterRoleSet extends AlterRoleParameter {

  private String value;
  private Boolean fromCurrent;

  @Override
  public String getSerializedObjectNamespace() {
    return Constants.NAMESPACE;
  }

  @Override
  public String getSerializedObjectName() {
    return "set";
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Boolean getFromCurrent() {
    return fromCurrent;
  }

  public void setFromCurrent(Boolean fromCurrent) {
    this.fromCurrent = fromCurrent;
  }

}
