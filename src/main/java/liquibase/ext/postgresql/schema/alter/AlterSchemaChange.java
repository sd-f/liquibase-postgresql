package liquibase.ext.postgresql.schema.alter;

import java.text.MessageFormat;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.postgresql.schema.SchemaChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "alterSchema", description = "Alter schema", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class AlterSchemaChange extends SchemaChange {

  private AlterSchemaRenameToElement renameTo;
  private AlterSchemaOwnerToElement ownerTo;

  @Override
  public String getConfirmationMessage() {
    return MessageFormat.format("Schema {0} altered", getSchemaName());
  }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    AlterSchemaStatement statement = new AlterSchemaStatement();

    statement.setSchemaName(getSchemaName());

    if (getRenameTo() != null) {
      statement.setRenameTo(getRenameTo().getSchema());
    }
    if (getOwnerTo() != null) {
      statement.setOwnerTo(getOwnerTo().getOwner());
    }

    return new SqlStatement[]{statement};
  }

  public AlterSchemaRenameToElement getRenameTo() {
    return renameTo;
  }

  public void setRenameTo(AlterSchemaRenameToElement renameTo) {
    this.renameTo = renameTo;
  }

  public AlterSchemaOwnerToElement getOwnerTo() {
    return ownerTo;
  }

  public void setOwnerTo(AlterSchemaOwnerToElement ownerTo) {
    this.ownerTo = ownerTo;
  }

}
