/*
 */
package liquibase.ext.postgresql.role.alter.rename;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterRoleRenameTo {

  private String name;

  public void setAttributesFromElement(AlterRoleRenameToElement element) {
    setName(element.getName());
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
