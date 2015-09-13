package liquibase.ext.postgresql.schema.drop;

import java.text.MessageFormat;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.postgresql.schema.SchemaChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "dropSchema", description = "Drop schema", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class DropSchemaChange extends SchemaChange {

  private Boolean cascade = false;
  private Boolean restrict = true;

  @Override
  public String getConfirmationMessage() {
    return MessageFormat.format("Schema {0} dropped", getSchemaName());
  }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    DropSchemaStatement statement = new DropSchemaStatement();

    statement.setSchemaName(getSchemaName());
    statement.setRestrict(isRestrict());
    statement.setCascade(isCascade());

    return new SqlStatement[]{statement};
  }

  public Boolean isCascade() {
    return cascade;
  }

  public void setCascade(Boolean cascade) {
    this.cascade = cascade;
  }

  public Boolean isRestrict() {
    return restrict;
  }

  public void setRestrict(Boolean restrict) {
    this.restrict = restrict;
  }

}
