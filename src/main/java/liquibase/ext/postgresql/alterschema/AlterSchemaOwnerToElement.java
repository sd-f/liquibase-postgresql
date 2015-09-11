/*
 */
package liquibase.ext.postgresql.alterschema;

import liquibase.ext.postgresql.xml.Constants;
import liquibase.serializer.AbstractLiquibaseSerializable;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterSchemaOwnerToElement extends AbstractLiquibaseSerializable {

  private String owner;

  @Override
  public String getSerializedObjectNamespace() {
    return Constants.NAMESPACE;
  }

  @Override
  public String getSerializedObjectName() {
    return "ownerTo";
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

}
