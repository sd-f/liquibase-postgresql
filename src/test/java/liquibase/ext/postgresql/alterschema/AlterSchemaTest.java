/*
 */
package liquibase.ext.postgresql.alterschema;

import java.io.IOException;
import java.util.List;

import liquibase.change.Change;
import liquibase.change.ChangeFactory;
import liquibase.change.ChangeMetaData;
import liquibase.changelog.ChangeSet;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.LiquibaseException;
import liquibase.exception.ValidationErrors;
import liquibase.exception.ValidationFailedException;
import liquibase.ext.postgresql.BaseTestCase;
import liquibase.ext.postgresql.xml.Constants;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterSchemaTest extends BaseTestCase {

  @Test
  public void generateStatementsOwnerTo() {
    // given
    AlterSchemaChange change = new AlterSchemaChange();
    change.setSchemaName("my_schema");
    AlterSchemaOwnerToElement ownerTo = new AlterSchemaOwnerToElement();
    ownerTo.setOwner("my_owner");
    change.setOwnerTo(ownerTo);

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals("my_schema", ((AlterSchemaStatement) sqlStatements[0]).getSchemaName());
    assertEquals("my_owner", ((AlterSchemaStatement) sqlStatements[0]).getOwnerTo());
  }

  @Test
  public void generateStatementsRenameTo() {
    // given
    AlterSchemaChange change = new AlterSchemaChange();
    change.setSchemaName("my_schema");
    AlterSchemaRenameToElement renameTo = new AlterSchemaRenameToElement();
    renameTo.setSchema("my_new_schema");
    change.setRenameTo(renameTo);

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals("my_schema", ((AlterSchemaStatement) sqlStatements[0]).getSchemaName());
    assertEquals("my_new_schema", ((AlterSchemaStatement) sqlStatements[0]).getRenameTo());
  }

  @Test
  public void getConfirmationMessage() {
    // given
    AlterSchemaChange change = new AlterSchemaChange();
    change.setSchemaName("my_schema");

    // when
    String message = change.getConfirmationMessage();

    // then
    assertEquals("Schema " + change.getSchemaName() + " altered", message);
  }

  @Test
  public void getChangeMetaData() {
    // given
    AlterSchemaChange change = new AlterSchemaChange();

    // when then
    assertEquals("alterSchema", ChangeFactory.getInstance().getChangeMetaData(change).getName());
    assertEquals("Alter schema", ChangeFactory.getInstance().getChangeMetaData(change).getDescription());
    assertEquals(ChangeMetaData.PRIORITY_DEFAULT, ChangeFactory.getInstance().getChangeMetaData(change).getPriority());
  }

  @Test
  public void validateNulls() {
    // given
    AlterSchemaChange change = new AlterSchemaChange();
    change.setSchemaName(null);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error schemaName", errors.getErrorMessages().contains("schemaName is required for alterSchema on postgresql"));
  }

  @Test
  public void valideEmpty() {
    // given
    AlterSchemaChange change = new AlterSchemaChange();
    change.setSchemaName("");

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error schemaName", errors.getErrorMessages().contains("schemaName must not be empty"));
  }

  @Test
  public void valideEmptyRenameTo() {
    // given
    AlterSchemaChange change = new AlterSchemaChange();
    change.setSchemaName("my_schema");
    AlterSchemaRenameToElement renameTo = new AlterSchemaRenameToElement();
    renameTo.setSchema("");
    change.setRenameTo(renameTo);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error renameTo", errors.getErrorMessages().contains("renameTo must not be empty"));
  }

  @Test
  public void valideEmptyOwnerTo() {
    // given
    AlterSchemaChange change = new AlterSchemaChange();
    change.setSchemaName("my_schema");
    AlterSchemaOwnerToElement ownerTo = new AlterSchemaOwnerToElement();
    ownerTo.setOwner("");
    change.setOwnerTo(ownerTo);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error ownerTo", errors.getErrorMessages().contains("ownerTo must not be empty"));
  }

  @Test
  public void valideRequired() {
    // given
    AlterSchemaChange change = new AlterSchemaChange();
    change.setSchemaName("my_schema");

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error roleName", errors.getErrorMessages().contains("Either \"ownerTo\" or \"renameTo\" is must be added"));
  }

  @Test
  public void valideExcluding() {
    // given
    AlterSchemaChange change = new AlterSchemaChange();
    change.setSchemaName("my_schema");
    AlterSchemaRenameToElement renameTo = new AlterSchemaRenameToElement();
    renameTo.setSchema("my_new_schema");
    change.setRenameTo(renameTo);
    AlterSchemaOwnerToElement ownerTo = new AlterSchemaOwnerToElement();
    ownerTo.setOwner("my_owner");
    change.setOwnerTo(ownerTo);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("ownerTo", ownerTo.getSerializedObjectName());
    assertEquals("renameTo", renameTo.getSerializedObjectName());
    assertEquals(Constants.NAMESPACE, ownerTo.getSerializedObjectNamespace());
    assertEquals(Constants.NAMESPACE, renameTo.getSerializedObjectNamespace());
    assertTrue("contains error roleName", errors.getErrorMessages().contains("Elements \"ownerTo\" and \"renameTo\" are excluding"));
  }

  @Test
  public void changesets() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterschema/changelog.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 2);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER SCHEMA my_schema RENAME TO my_new_schema", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(1).getChanges().size());
    change = changeSets.get(1).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER SCHEMA my_schema OWNER TO my_owner", sql[0].toSql());

  }

  @Test
  public void changesetsEmpty() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterschema/changelog-empty.test1.xml";

    // when
    try {
      List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);
    } catch (ValidationFailedException ex) {
      assertTrue("validation failed", ex.getMessage().contains("ownerTo must not be empty"));
      return;
    }
    assertTrue("validation failed - no exception", false);
  }

}
