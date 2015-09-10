/*
 */
package liquibase.ext.postgresql.createschema;

import java.io.IOException;
import java.util.List;

import liquibase.change.Change;
import liquibase.change.ChangeFactory;
import liquibase.change.ChangeMetaData;
import liquibase.changelog.ChangeSet;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.LiquibaseException;
import liquibase.exception.ValidationErrors;
import liquibase.ext.postgresql.BaseTestCase;
import liquibase.ext.postgresql.dropschema.DropSchemaChange;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class CreateSchemaTest extends BaseTestCase {

  @Test
  public void generateStatements() {
    // given
    CreateSchemaChange change = new CreateSchemaChange();
    change.setSchemaName("my_schema");
    change.setAuthorization("my_user");

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals("my_schema", ((CreateSchemaStatement) sqlStatements[0]).getSchemaName());
    assertEquals("my_user", ((CreateSchemaStatement) sqlStatements[0]).getAuthorization());
  }

  @Test
  public void getConfirmationMessage() {
    // given
    CreateSchemaChange change = new CreateSchemaChange();
    change.setSchemaName("my_schema");
    change.setAuthorization("my_user");

    // when
    String message = change.getConfirmationMessage();

    // then
    assertEquals("Schema " + change.getSchemaName() + " created", message);
  }

  @Test
  public void getChangeMetaData() {
    // given
    CreateSchemaChange change = new CreateSchemaChange();

    // when then
    assertEquals("createSchema", ChangeFactory.getInstance().getChangeMetaData(change).getName());
    assertEquals("Create schema", ChangeFactory.getInstance().getChangeMetaData(change).getDescription());
    assertEquals(ChangeMetaData.PRIORITY_DEFAULT, ChangeFactory.getInstance().getChangeMetaData(change).getPriority());
  }

  @Test
  public void createInverse() {
    // given
    CreateSchemaChange change = new CreateSchemaChange();
    change.setSchemaName("my_schema");

    // when
    Change[] changes = change.createInverses();

    // then
    assertEquals(1, changes.length);
    Assert.assertNotNull("reverse role is created", ((DropSchemaChange) changes[0]));
    assertEquals(change.getSchemaName(), ((DropSchemaChange) changes[0]).getSchemaName());
  }

  @Test
  public void validateNulls() {
    // given
    CreateSchemaChange change = new CreateSchemaChange();

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error schemaName", errors.getErrorMessages().contains("schemaName is required for createSchema on postgresql"));
  }

  @Test
  public void validateEmpty() {
    // given
    CreateSchemaChange change = new CreateSchemaChange();
    change.setSchemaName("");

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error schemaName", errors.getErrorMessages().contains("schemaName must not be empty"));
  }

  @Test
  public void createSchemaFull() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/createschema/changelog-full.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 2);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());

    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "CREATE SCHEMA my_schema AUTHORIZATION test", sql[0].toSql());

    change = changeSets.get(1).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "CREATE SCHEMA my_schema", sql[0].toSql());
  }

  @Test
  public void createSchemaSimple() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/createschema/changelog.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "CREATE SCHEMA my_schema", sql[0].toSql());

  }

}
