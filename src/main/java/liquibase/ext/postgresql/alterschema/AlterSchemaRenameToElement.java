/*
 */
package liquibase.ext.postgresql.alterschema;

import liquibase.ext.postgresql.xml.Constants;
import liquibase.serializer.AbstractLiquibaseSerializable;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterSchemaRenameToElement extends AbstractLiquibaseSerializable {

  private String schema;

  @Override
  public String getSerializedObjectNamespace() {
    return Constants.NAMESPACE;
  }

  @Override
  public String getSerializedObjectName() {
    return "renameTo";
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

}
