/*
 */
package liquibase.ext.postgresql.alterrole.reset;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterRoleReset {

  private String parameter;
  private String inDatabase;

  public AlterRoleReset() {
  }

  public void setAttributesFromElement(AlterRoleResetElement element) {
    setParameter(element.getParameter());
    setInDatabase(element.getInDatabase());
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

}
