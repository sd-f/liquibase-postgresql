/*
 */
package liquibase.ext.postgresql.droprole;

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
public class DropRoleTest extends BaseTestCase {

  @Test
  public void generateStatements() {
    // given
    DropRoleChange change = new DropRoleChange();
    change.setRoleName("my_role");

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals("my_role", ((DropRoleStatement) sqlStatements[0]).getRoleName());
  }

  @Test
  public void getConfirmationMessage() {
    // given
    DropRoleChange change = new DropRoleChange();
    change.setRoleName("my_role");

    // when
    String message = change.getConfirmationMessage();

    // then
    assertEquals("Role " + change.getRoleName() + " dropped", message);
  }

  @Test
  public void getChangeMetaData() {
    // given
    DropRoleChange change = new DropRoleChange();

    // when then
    assertEquals("dropRole", ChangeFactory.getInstance().getChangeMetaData(change).getName());
    assertEquals("Drop role", ChangeFactory.getInstance().getChangeMetaData(change).getDescription());
    assertEquals(ChangeMetaData.PRIORITY_DEFAULT, ChangeFactory.getInstance().getChangeMetaData(change).getPriority());
  }

  @Test
  public void changeset() throws LiquibaseException, IOException {
    // given
    Liquibase liquibase = this.newLiquibase("/droprole/changelog.test.xml");
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
    assertEquals("Matching statement", "DROP ROLE my_role", sql[0].toSql());
  }

}
