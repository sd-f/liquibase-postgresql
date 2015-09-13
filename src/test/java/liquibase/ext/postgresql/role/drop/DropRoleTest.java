/*
 */
package liquibase.ext.postgresql.role.drop;

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
  public void validateNulls() {
    // given
    DropRoleChange change = new DropRoleChange();

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error roleName", errors.getErrorMessages().contains("roleName is required for dropRole on postgresql"));
  }

  @Test
  public void validateEmpty() {
    // given
    DropRoleChange change = new DropRoleChange();
    change.setRoleName("");

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error roleName", errors.getErrorMessages().contains("roleName must not be empty"));
  }

  @Test
  public void changeset() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/role/drop/changelog.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "DROP ROLE my_role", sql[0].toSql());
  }

}
