/*
 */
package liquibase.ext.postgresql.dropschema;

import java.io.IOException;
import java.util.List;

import liquibase.Liquibase;
import liquibase.change.Change;
import liquibase.change.ChangeFactory;
import liquibase.change.ChangeMetaData;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.LiquibaseException;
import liquibase.ext.postgresql.BaseTestCase;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.SqlStatement;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

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
  public void generateStatement() throws LiquibaseException, IOException {
    // given
    Liquibase liquibase = this.newLiquibase("/dropschema/changelog.test.xml");
    Database database = liquibase.getDatabase();
    DatabaseChangeLog changeLog = liquibase.getDatabaseChangeLog();

    // when
    changeLog.validate(database);
    List<ChangeSet> changeSets = changeLog.getChangeSets();

    // then
    assertEquals("One changeset given", 1, changeSets.size());

    ChangeSet changeSet = changeSets.get(0);

    assertEquals("One change given", 1, changeSet.getChanges().size());

    Change change = changeSet.getChanges().get(0);

    // when
    Sql[] sql = SqlGeneratorFactory.getInstance()
        .generateSql(change.generateStatements(database)[0], database);

    assertEquals("One statement generated", 1, sql.length);

    // then
    assertEquals("Matching statement", "DROP SCHEMA my_schema", sql[0].toSql());
  }

  @Test
  public void generateStatementFull() throws LiquibaseException, IOException {
    // given
    Liquibase liquibase = this.newLiquibase("/dropschema/changelog-full.test.xml");
    Database database = liquibase.getDatabase();
    DatabaseChangeLog changeLog = liquibase.getDatabaseChangeLog();

    // when
    changeLog.validate(database);
    List<ChangeSet> changeSets = changeLog.getChangeSets();

    // then
    assertEquals("Two changesets given", 2, changeSets.size());
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Sql[] sql = SqlGeneratorFactory.getInstance()
        .generateSql(changeSets.get(0).getChanges().get(0).generateStatements(database)[0], database);

    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "DROP SCHEMA my_schema RESTRICT", sql[0].toSql());

    assertEquals("One change given", 1, changeSets.get(1).getChanges().size());
    sql = SqlGeneratorFactory.getInstance()
        .generateSql(changeSets.get(1).getChanges().get(0).generateStatements(database)[0], database);

    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "DROP SCHEMA my_schema CASCADE", sql[0].toSql());
  }

  @Test
  public void generateStatementExcludingProperties() throws LiquibaseException, IOException {
    // given
    Liquibase liquibase = this.newLiquibase("/dropschema/changelog-excluding.test.xml");
    Database database = liquibase.getDatabase();
    DatabaseChangeLog changeLog = liquibase.getDatabaseChangeLog();

    // when
    try {
      changeLog.validate(database);
    } catch (LiquibaseException ex) {
      // then
      // expect exception
      Assert.assertTrue("Validation error", ex.getMessage().contains("Attributes \"restrict\" and \"cascade\" are excluding"));

    }

  }

}
