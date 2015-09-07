/*
 */
package liquibase.ext.postgresql.createschema;

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
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

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
  public void createSchemaFull() throws LiquibaseException, IOException {
    // given
    Liquibase liquibase = this.newLiquibase("/createschema/changelog-full.test.xml");
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
    assertEquals("Matching statement", "CREATE SCHEMA my_schema AUTHORIZATION test", sql[0].toSql());
  }

  @Test
  public void createSchemaSimple() throws LiquibaseException, IOException {
    // given
    Liquibase liquibase = this.newLiquibase("/createschema/changelog.test.xml");
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
    assertEquals("Matching statement", "CREATE SCHEMA my_schema", sql[0].toSql());

  }

}
