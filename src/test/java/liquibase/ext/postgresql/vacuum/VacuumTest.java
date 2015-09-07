package liquibase.ext.postgresql.vacuum;

import java.io.IOException;
import java.util.List;

import liquibase.Liquibase;
import liquibase.change.Change;
import liquibase.change.ChangeFactory;
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
  public void changeset() throws LiquibaseException, IOException {
    // given
    Liquibase liquibase = this.newLiquibase("/vacuum/changelog-full.test.xml");
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
    assertEquals("Matching statement", "VACUUM my_schema.my_table", sql[0].toSql());
  }

}
