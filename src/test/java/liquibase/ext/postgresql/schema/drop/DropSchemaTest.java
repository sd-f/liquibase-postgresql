/*
 */
package liquibase.ext.postgresql.schema.drop;

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
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class DropSchemaTest extends BaseTestCase {

  @Test
  public void generateStatements() {
    // given
    DropSchemaChange change = new DropSchemaChange();
    change.setSchemaName("my_schema");

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals("my_schema", ((DropSchemaStatement) sqlStatements[0]).getSchemaName());
  }

  @Test
  public void getConfirmationMessage() {
    // given
    DropSchemaChange change = new DropSchemaChange();
    change.setSchemaName("my_schema");

    // when
    String message = change.getConfirmationMessage();

    // then
    assertEquals("Schema " + change.getSchemaName() + " dropped", message);
  }

  @Test
  public void getChangeMetaData() {
    // given
    DropSchemaChange change = new DropSchemaChange();

    // when then
    assertEquals("dropSchema", ChangeFactory.getInstance().getChangeMetaData(change).getName());
    assertEquals("Drop schema", ChangeFactory.getInstance().getChangeMetaData(change).getDescription());
    assertEquals(ChangeMetaData.PRIORITY_DEFAULT, ChangeFactory.getInstance().getChangeMetaData(change).getPriority());
  }

  @Test
  public void validate() {
    // given
    DropSchemaChange change = new DropSchemaChange();
    change.setSchemaName("my_schema");
    change.setRestrict(true);
    change.setCascade(true);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error cascade restrict excluding", errors.getErrorMessages().contains("Attributes \"restrict\" and \"cascade\" are excluding"));
  }

  @Test
  public void validateRestrict() {
    // given
    DropSchemaChange change = new DropSchemaChange();
    change.setSchemaName("my_schema");
    change.setRestrict(false);
    change.setCascade(true);
    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("contains no errors", 0, errors.getErrorMessages().size());
  }

  @Test
  public void validateCascade() {
    // given
    DropSchemaChange change = new DropSchemaChange();
    change.setSchemaName("my_schema");
    change.setRestrict(true);
    change.setCascade(false);
    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("contains no errors", 0, errors.getErrorMessages().size());
  }

  @Test
  public void validateMandatory() {
    // given
    DropSchemaChange change = new DropSchemaChange();

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error schemaName mandatory", errors.getErrorMessages().contains("schemaName is required for dropSchema on postgresql"));
  }

  @Test
  public void validateEmpty() {
    // given
    DropSchemaChange change = new DropSchemaChange();
    change.setSchemaName("");

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error schemaName mandatory", errors.getErrorMessages().contains("schemaName must not be empty"));
  }

  @Test
  public void generateStatement() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/schema/drop/changelog.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "DROP SCHEMA my_schema", sql[0].toSql());
  }

  @Test
  public void generateStatementCascade() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/schema/drop/changelog-cascade.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "DROP SCHEMA my_schema CASCADE", sql[0].toSql());
  }

  @Test
  public void generateStatementFull() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/schema/drop/changelog-full.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 6);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "DROP SCHEMA my_schema RESTRICT", sql[0].toSql());

    change = changeSets.get(1).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "DROP SCHEMA my_schema CASCADE", sql[0].toSql());

    change = changeSets.get(2).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "DROP SCHEMA my_schema", sql[0].toSql());

    change = changeSets.get(3).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "DROP SCHEMA my_schema", sql[0].toSql());

    change = changeSets.get(4).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "DROP SCHEMA my_schema", sql[0].toSql());

    change = changeSets.get(5).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "DROP SCHEMA my_schema CASCADE", sql[0].toSql());
  }

}
