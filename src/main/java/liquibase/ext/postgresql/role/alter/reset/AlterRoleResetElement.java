/*
 */
package liquibase.ext.postgresql.role.alter.reset;

import liquibase.ext.postgresql.xml.Constants;
import liquibase.serializer.AbstractLiquibaseSerializable;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterRoleResetElement extends AbstractLiquibaseSerializable {

  private String parameter;
  private String inDatabase;

  @Override
  public String getSerializedObjectNamespace() {
    return Constants.NAMESPACE;
  }

  @Override
  public String getSerializedObjectName() {
    return "reset";
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
