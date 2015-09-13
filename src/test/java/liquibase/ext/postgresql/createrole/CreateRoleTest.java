/*
 */
package liquibase.ext.postgresql.createrole;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import liquibase.ext.postgresql.role.RoleOptionsElement;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import liquibase.util.ISODateFormat;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class CreateRoleTest extends BaseTestCase {

  @Test
  public void generateStatements() throws ParseException {
    // given
    CreateRoleChange change = new CreateRoleChange();

    change.setRoleName("my_role");

    RoleOptionsElement options = new RoleOptionsElement();

    options.setPassword("my_password");
    options.setConnectionLimit(BigInteger.valueOf(1));
    options.setCreateDatabase(Boolean.TRUE);
    options.setCreateRole(Boolean.TRUE);
    options.setEncryptedPassword(Boolean.TRUE);
    options.setInherit(Boolean.TRUE);
    options.setLoginAllowed(Boolean.FALSE);
    options.setSuperUser(Boolean.TRUE);
    options.setReplication(Boolean.TRUE);

    Date validUntilDate = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    options.setValidUntil(format.format(validUntilDate));

    change.setOptions(options);

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals("my_role", ((CreateRoleStatement) sqlStatements[0]).getRoleName());
    assertNotNull(((CreateRoleStatement) sqlStatements[0]).getRoleOptions());
    assertEquals("my_password", ((CreateRoleStatement) sqlStatements[0]).getRoleOptions().getPassword());
    assertEquals(BigInteger.valueOf(1), ((CreateRoleStatement) sqlStatements[0]).getRoleOptions().getConnectionLimit());
    assertEquals(Boolean.TRUE, ((CreateRoleStatement) sqlStatements[0]).getRoleOptions().getCreateDatabase());
    assertEquals(Boolean.TRUE, ((CreateRoleStatement) sqlStatements[0]).getRoleOptions().getCreateRole());
    assertEquals(Boolean.TRUE, ((CreateRoleStatement) sqlStatements[0]).getRoleOptions().getEncryptedPassword());
    assertEquals(Boolean.TRUE, ((CreateRoleStatement) sqlStatements[0]).getRoleOptions().getInherit());
    assertEquals(Boolean.FALSE, ((CreateRoleStatement) sqlStatements[0]).getRoleOptions().getLoginAllowed());
    assertEquals(Boolean.TRUE, ((CreateRoleStatement) sqlStatements[0]).getRoleOptions().getSuperUser());
    assertEquals(Boolean.TRUE, ((CreateRoleStatement) sqlStatements[0]).getRoleOptions().getReplication());
    assertEquals(new ISODateFormat().parse(format.format(validUntilDate)), ((CreateRoleStatement) sqlStatements[0]).getRoleOptions().getValidUntil());
  }

  @Test
  public void getConfirmationMessage() {
    // given
    CreateRoleChange change = new CreateRoleChange();
    change.setRoleName("my_role");
    RoleOptionsElement options = new RoleOptionsElement();
    options.setPassword("my_password");
    change.setOptions(options);

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
  public void validateNullRoleName() {
    // given
    CreateRoleChange change = new CreateRoleChange();

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("errors", 1, errors.getErrorMessages().size());
    assertTrue("contains error roleName", errors.getErrorMessages().contains("roleName is required for createRole on postgresql"));
  }

  @Test
  public void validateNullOptionsMissing() {
    // given
    CreateRoleChange change = new CreateRoleChange();
    change.setRoleName("my_role");

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("errors", 1, errors.getErrorMessages().size());
    assertTrue("contains error options", errors.getErrorMessages().contains("options is required"));
  }

  @Test
  public void validateNullPasswordMissing() {
    // given
    CreateRoleChange change = new CreateRoleChange();
    change.setRoleName("my_role");
    RoleOptionsElement options = new RoleOptionsElement();
    change.setOptions(options);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("errors", 1, errors.getErrorMessages().size());
    assertTrue("contains error password", errors.getErrorMessages().contains("password is required"));
  }

  @Test
  public void validateEmpty() {
    // given
    CreateRoleChange change = new CreateRoleChange();
    change.setRoleName("");
    RoleOptionsElement options = new RoleOptionsElement();
    options.setPassword("");
    change.setOptions(options);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error roleName", errors.getErrorMessages().contains("roleName must not be empty"));
    assertTrue("contains error password", errors.getErrorMessages().contains("password must not be empty"));
  }

  @Test
  public void changesetFull() throws LiquibaseException, IOException {
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
  public void changesetSimple() throws LiquibaseException, IOException {
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
    assertEquals("Matching statement", "CREATE ROLE my_role NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT NOLOGIN PASSWORD 'my_password' REPLICATION", sql[0].toSql());

  }

}
