/*
 */
package liquibase.ext.postgresql.role.alter.set;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterRoleSet {

  private String parameter;
  private String inDatabase;
  private String value;
  private Boolean fromCurrent;

  public AlterRoleSet() {
  }

  public void setAttributesFromElement(AlterRoleSetElement element) {
    setParameter(element.getParameter());
    setInDatabase(element.getInDatabase());
    setValue(element.getValue());
    setFromCurrent(element.getFromCurrent());
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
