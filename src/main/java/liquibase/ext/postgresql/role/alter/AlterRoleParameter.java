/*
 */
package liquibase.ext.postgresql.role.alter;

import liquibase.serializer.AbstractLiquibaseSerializable;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public abstract class AlterRoleParameter extends AbstractLiquibaseSerializable {

  private String parameter;
  private String inDatabase;

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
