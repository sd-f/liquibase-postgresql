/*
 */
package liquibase.ext.postgresql.role.alter;

import liquibase.ext.postgresql.xml.Constants;
import liquibase.serializer.AbstractLiquibaseSerializable;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterRoleSet extends AbstractLiquibaseSerializable {

  private String parameter;
  private String inDatabase;
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

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public String getInDatabase() {
    return inDatabase;
  }

  public void setInDatabase(String inDatabase) {
    this.inDatabase = inDatabase;
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
