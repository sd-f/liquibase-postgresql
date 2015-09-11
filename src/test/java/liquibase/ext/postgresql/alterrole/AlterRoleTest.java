/*
 */
package liquibase.ext.postgresql.alterrole;

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
public class AlterRoleTest extends BaseTestCase {

  @Test
  public void generateStatementsOwnerTo() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("my_role");
    AlterRoleOwnerToElement ownerTo = new AlterRoleOwnerToElement();
    ownerTo.setOwner("my_owner");
    change.setOwnerTo(ownerTo);

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals("my_role", ((AlterRoleStatement) sqlStatements[0]).getRoleName());
    assertEquals("my_owner", ((AlterRoleStatement) sqlStatements[0]).getOwnerTo());
  }

  @Test
  public void generateStatementsRenameTo() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("my_role");
    AlterRoleRenameToElement renameTo = new AlterRoleRenameToElement();
    renameTo.setRole("my_new_role");
    change.setRenameTo(renameTo);

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals("my_role", ((AlterRoleStatement) sqlStatements[0]).getRoleName());
    assertEquals("my_new_role", ((AlterRoleStatement) sqlStatements[0]).getRenameTo());
  }

  @Test
  public void getConfirmationMessage() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("my_role");

    // when
    String message = change.getConfirmationMessage();

    // then
    assertEquals("Role " + change.getRoleName() + " altered", message);
  }

  @Test
  public void getChangeMetaData() {
    // given
    AlterRoleChange change = new AlterRoleChange();

    // when then
    assertEquals("alterRole", ChangeFactory.getInstance().getChangeMetaData(change).getName());
    assertEquals("Alter role", ChangeFactory.getInstance().getChangeMetaData(change).getDescription());
    assertEquals(ChangeMetaData.PRIORITY_DEFAULT, ChangeFactory.getInstance().getChangeMetaData(change).getPriority());
  }

  @Test
  public void validateNulls() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName(null);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error roleName", errors.getErrorMessages().contains("roleName is required for alterRole on postgresql"));
  }

  @Test
  public void valideEmpty() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("");

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error roleName", errors.getErrorMessages().contains("roleName must not be empty"));
  }

  @Test
  public void valideEmptyRenameTo() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("my_role");
    AlterRoleRenameToElement renameTo = new AlterRoleRenameToElement();
    renameTo.setRole("");
    change.setRenameTo(renameTo);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error renameTo", errors.getErrorMessages().contains("renameTo must not be empty"));
  }

  @Test
  public void valideEmptyOwnerTo() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("my_role");
    AlterRoleOwnerToElement ownerTo = new AlterRoleOwnerToElement();
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
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("my_role");

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error roleName", errors.getErrorMessages().contains("Either \"ownerTo\" or \"renameTo\" is must be added"));
  }

  @Test
  public void valideExcluding() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("my_role");
    AlterRoleRenameToElement renameTo = new AlterRoleRenameToElement();
    renameTo.setRole("my_new_role");
    change.setRenameTo(renameTo);
    AlterRoleOwnerToElement ownerTo = new AlterRoleOwnerToElement();
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
    String changeLogFile = "/alterrole/changelog.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 2);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE my_role RENAME TO my_new_role", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(1).getChanges().size());
    change = changeSets.get(1).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE my_role OWNER TO my_owner", sql[0].toSql());

  }

  @Test
  public void changesetsEmpty() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterrole/changelog-empty.test1.xml";

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
