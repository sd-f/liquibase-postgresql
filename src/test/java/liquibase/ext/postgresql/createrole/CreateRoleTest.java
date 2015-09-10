/*
 */
package liquibase.ext.postgresql.createrole;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import liquibase.change.Change;
import liquibase.change.ChangeFactory;
import liquibase.change.ChangeMetaData;
import liquibase.changelog.ChangeSet;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.LiquibaseException;
import liquibase.exception.ValidationErrors;
import liquibase.ext.postgresql.BaseTestCase;
import liquibase.ext.postgresql.droprole.DropRoleChange;
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
public class CreateRoleTest extends BaseTestCase {

  @Test
  public void generateStatements() {
    // given
    CreateRoleChange change = new CreateRoleChange();

    change.setRoleName("my_role");
    change.setPassword("my_password");
    change.setConnectionLimit(1);
    change.setCreateDatabase(Boolean.TRUE);
    change.setCreateRole(Boolean.TRUE);
    change.setEncryptedPassword(Boolean.TRUE);
    change.setInherit(Boolean.TRUE);
    change.setLoginAllowed(Boolean.FALSE);
    change.setSuperUser(Boolean.TRUE);

    Date currentDate = new Date();
    change.setValidUntil(currentDate);

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals("my_role", ((CreateRoleStatement) sqlStatements[0]).getRoleName());
    assertEquals("my_password", ((CreateRoleStatement) sqlStatements[0]).getPassword());
    assertEquals(new Integer(1), ((CreateRoleStatement) sqlStatements[0]).getConnectionLimit());
    assertEquals(Boolean.TRUE, ((CreateRoleStatement) sqlStatements[0]).isCreateDatabase());
    assertEquals(Boolean.TRUE, ((CreateRoleStatement) sqlStatements[0]).isCreateRole());
    assertEquals(Boolean.TRUE, ((CreateRoleStatement) sqlStatements[0]).isEncryptedPassword());
    assertEquals(Boolean.TRUE, ((CreateRoleStatement) sqlStatements[0]).isInherit());
    assertEquals(Boolean.FALSE, ((CreateRoleStatement) sqlStatements[0]).isLoginAllowed());
    assertEquals(Boolean.TRUE, ((CreateRoleStatement) sqlStatements[0]).isSuperUser());
    assertEquals(currentDate, ((CreateRoleStatement) sqlStatements[0]).getValidUntil());
  }

  @Test
  public void getConfirmationMessage() {
    // given
    CreateRoleChange change = new CreateRoleChange();
    change.setRoleName("my_role");
    change.setPassword("my_password");

    // when
    String message = change.getConfirmationMessage();

    // then
    assertEquals("Role " + change.getRoleName() + " created", message);
  }

  @Test
  public void getChangeMetaData() {
    // given
    CreateRoleChange change = new CreateRoleChange();

    // when then
    assertEquals("createRole", ChangeFactory.getInstance().getChangeMetaData(change).getName());
    assertEquals("Create role", ChangeFactory.getInstance().getChangeMetaData(change).getDescription());
    assertEquals(ChangeMetaData.PRIORITY_DEFAULT, ChangeFactory.getInstance().getChangeMetaData(change).getPriority());
  }

  @Test
  public void createInverse() {
    // given
    CreateRoleChange change = new CreateRoleChange();
    change.setRoleName("my_role");

    // when
    Change[] changes = change.createInverses();

    // then
    assertEquals(1, changes.length);
    Assert.assertNotNull("reverse role is created", ((DropRoleChange) changes[0]));
    assertEquals(change.getRoleName(), ((DropRoleChange) changes[0]).getRoleName());
  }

  @Test
  public void validateNulls() {
    // given
    CreateRoleChange change = new CreateRoleChange();

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error roleName", errors.getErrorMessages().contains("roleName is required for createRole on postgresql"));
    assertTrue("contains error password", errors.getErrorMessages().contains("password is required for createRole on postgresql"));
  }

  @Test
  public void validateEmpty() {
    // given
    CreateRoleChange change = new CreateRoleChange();
    change.setRoleName("");
    change.setPassword("");

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error roleName", errors.getErrorMessages().contains("roleName must not be empty"));
    assertTrue("contains error password", errors.getErrorMessages().contains("password must not be empty"));
  }

  @Test
  public void createSchemaFull() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/createrole/changelog-full.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 2);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "CREATE ROLE my_role SUPERUSER CREATEDB CREATEROLE INHERIT LOGIN CONNECTION LIMIT 1 ENCRYPTED PASSWORD 'my_password' VALID UNTIL '2002-05-30 09:00:00.0'", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(1).getChanges().size());
    change = changeSets.get(1).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "CREATE ROLE my_role NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT NOLOGIN UNENCRYPTED PASSWORD 'my_password'", sql[0].toSql());

  }

  @Test
  public void createSchemaSimple() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/createrole/changelog.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "CREATE ROLE my_role NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT NOLOGIN PASSWORD 'my_password'", sql[0].toSql());

  }

}
