package liquibase.ext.postgresql.vacuum;

import java.io.IOException;
import java.util.List;

import liquibase.change.Change;
import liquibase.change.ChangeFactory;
import liquibase.changelog.ChangeSet;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.LiquibaseException;
import liquibase.ext.postgresql.BaseTestCase;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class VacuumTest extends BaseTestCase {

  @Test
  public void generateStatements() {
    // given
    VacuumChange change = new VacuumChange();
    change.setSchemaName("my_schema");
    change.setTableName("my_table");
    change.setCatalogName("my_catalog");

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals("my_schema", ((VacuumStatement) sqlStatements[0]).getSchemaName());
    assertEquals("my_table", ((VacuumStatement) sqlStatements[0]).getTableName());
    assertEquals("my_catalog", ((VacuumStatement) sqlStatements[0]).getCatalogName());
  }

  @Test
  public void getChangeMetaData() {
    // given
    VacuumChange change = new VacuumChange();

    // when then
    assertEquals("vacuum", ChangeFactory.getInstance().getChangeMetaData(change).getName());
    assertEquals("Vacuum Database", ChangeFactory.getInstance().getChangeMetaData(change).getDescription());
    assertEquals(15, ChangeFactory.getInstance().getChangeMetaData(change).getPriority());
  }

  @Test
  public void getConfirmationMessage() {
    // given
    VacuumChange change = new VacuumChange();

    // when
    String message = change.getConfirmationMessage();

    // then
    assertEquals("Database vacuumed", message);
  }

  @Test
  public void changeset() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/vacuum/changelog-full.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "VACUUM my_schema.my_table", sql[0].toSql());

  }

  @Test
  public void changesetNulls() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/vacuum/changelog-nulls.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "VACUUM", sql[0].toSql());

  }

  @Test
  public void changesetEmpty() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/vacuum/changelog-empty.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "VACUUM", sql[0].toSql());

  }

}
