package liquibase.ext.postgresql.schema.create;

import java.text.MessageFormat;

import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.postgresql.schema.SchemaChange;
import liquibase.ext.postgresql.schema.drop.DropSchemaChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "createSchema", description = "Create schema", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateSchemaChange extends SchemaChange {

  private String authorization;

  @Override
  public String getConfirmationMessage() {
    return MessageFormat.format("Schema {0} created", getSchemaName());
  }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    CreateSchemaStatement statement = new CreateSchemaStatement();

    statement.setSchemaName(getSchemaName());
    statement.setAuthorization(getAuthorization());

    return new SqlStatement[]{statement};
  }

  @Override
  protected Change[] createInverses() {
    DropSchemaChange inverse = new DropSchemaChange();
    inverse.setSchemaName(getSchemaName());
    inverse.setRestrict(Boolean.TRUE);
    return new Change[]{inverse,};
  }

  public String getAuthorization() {
    return authorization;
  }

  public void setAuthorization(String authorization) {
    this.authorization = authorization;
  }

}
