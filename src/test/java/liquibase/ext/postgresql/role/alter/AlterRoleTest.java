/*
 */
package liquibase.ext.postgresql.role.alter;

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
import liquibase.ext.postgresql.role.RoleOptionsElement;
import liquibase.ext.postgresql.xml.Constants;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterRoleTest extends BaseTestCase {

  private AlterRoleChange newFullChange() {
    AlterRoleChange change = new AlterRoleChange();

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

    AlterRoleRenameTo renameToElement = new AlterRoleRenameTo();
    renameToElement.setName("new_name");

    change.setRenameTo(renameToElement);

    AlterRoleSet setElement = new AlterRoleSet();
    setElement.setParameter("param1");
    setElement.setInDatabase("database1");

    change.setSet(setElement);

    AlterRoleReset resetElement = new AlterRoleReset();
    resetElement.setParameter("param1");
    resetElement.setInDatabase("database1");

    change.setReset(resetElement);

    return change;
  }

  @Test
  public void generateStatements() throws ParseException {
    // given
    AlterRoleChange change = newFullChange();

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals("my_role", ((AlterRoleStatement) sqlStatements[0]).getRoleName());

    assertNotNull(((AlterRoleStatement) sqlStatements[0]).getOptions());
    assertEquals("my_password", ((AlterRoleStatement) sqlStatements[0]).getOptions().getPassword());
    assertEquals(BigInteger.valueOf(1), ((AlterRoleStatement) sqlStatements[0]).getOptions().getConnectionLimit());
    assertEquals(Boolean.TRUE, ((AlterRoleStatement) sqlStatements[0]).getOptions().getCreateDatabase());
    assertEquals(Boolean.TRUE, ((AlterRoleStatement) sqlStatements[0]).getOptions().getCreateRole());
    assertEquals(Boolean.TRUE, ((AlterRoleStatement) sqlStatements[0]).getOptions().getEncryptedPassword());
    assertEquals(Boolean.TRUE, ((AlterRoleStatement) sqlStatements[0]).getOptions().getInherit());
    assertEquals(Boolean.FALSE, ((AlterRoleStatement) sqlStatements[0]).getOptions().getLoginAllowed());
    assertEquals(Boolean.TRUE, ((AlterRoleStatement) sqlStatements[0]).getOptions().getSuperUser());
    assertEquals(Boolean.TRUE, ((AlterRoleStatement) sqlStatements[0]).getOptions().getReplication());
    assertNotNull(((AlterRoleStatement) sqlStatements[0]).getOptions().getValidUntil());

    assertNotNull(((AlterRoleStatement) sqlStatements[0]).getRenameTo());
    assertEquals("new_name", ((AlterRoleStatement) sqlStatements[0]).getRenameTo().getName());

    assertNotNull(((AlterRoleStatement) sqlStatements[0]).getSet());
    assertEquals("param1", ((AlterRoleStatement) sqlStatements[0]).getSet().getParameter());
    assertEquals("database1", ((AlterRoleStatement) sqlStatements[0]).getSet().getInDatabase());

    assertNotNull(((AlterRoleStatement) sqlStatements[0]).getReset());
    assertEquals("param1", ((AlterRoleStatement) sqlStatements[0]).getReset().getParameter());
    assertEquals("database1", ((AlterRoleStatement) sqlStatements[0]).getReset().getInDatabase());
  }

  @Test
  public void elementRenameToNamespace() {
    // given
    AlterRoleRenameTo change = new AlterRoleRenameTo();

    // when
    // then
    assertEquals("renameTo", change.getSerializedObjectName());
    assertEquals(Constants.NAMESPACE, change.getSerializedObjectNamespace());
  }

  @Test
  public void elementSetNamespace() {
    // given
    AlterRoleSet change = new AlterRoleSet();

    // when
    // then
    assertEquals("set", change.getSerializedObjectName());
    assertEquals(Constants.NAMESPACE, change.getSerializedObjectNamespace());
  }

  @Test
  public void elementResetNamespace() {
    // given
    AlterRoleReset change = new AlterRoleReset();

    // when
    // then
    assertEquals("reset", change.getSerializedObjectName());
    assertEquals(Constants.NAMESPACE, change.getSerializedObjectNamespace());
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
  public void validateNullRoleName() {
    // given
    AlterRoleChange change = new AlterRoleChange();

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("errors", 1, errors.getErrorMessages().size());
    assertTrue("contains error roleName", errors.getErrorMessages().contains("roleName is required for alterRole on postgresql"));
  }

  @Test
  public void validateNullSubElementMissing() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("my_role");

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("errors", 1, errors.getErrorMessages().size());
    assertTrue("contains subelement missing error", errors.getErrorMessages().contains("subelement is missing"));
  }

  @Test
  public void validateRenameNull() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    AlterRoleRenameTo renameToElement = new AlterRoleRenameTo();
    change.setRoleName("name");
    change.setRenameTo(renameToElement);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("errors", 1, errors.getErrorMessages().size());
    assertTrue("contains name error", errors.getErrorMessages().contains("name is required"));
  }

  @Test
  public void validateRenameEmpty() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    AlterRoleRenameTo renameToElement = new AlterRoleRenameTo();
    renameToElement.setName("");
    change.setRoleName("name");
    change.setRenameTo(renameToElement);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("errors", 1, errors.getErrorMessages().size());
    assertTrue("contains name error", errors.getErrorMessages().contains("name must not be empty"));
  }

  @Test
  public void validateSetParameterNull() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("something");
    AlterRoleSet element = new AlterRoleSet();
    element.setValue("value");
    change.setSet(element);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("errors", 1, errors.getErrorMessages().size());
    assertTrue("contains name error", errors.getErrorMessages().contains("parameter is required"));
  }

  @Test
  public void validateSetNoValue() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("something");
    AlterRoleSet element = new AlterRoleSet();
    element.setParameter("param");
    change.setSet(element);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("errors", 1, errors.getErrorMessages().size());
    assertTrue("contains name error", errors.getErrorMessages().contains("use value OR fromCurrent"));
  }

  @Test
  public void validateSetEmptyValue() {
    // given
    AlterRoleChange change = new AlterRoleChange();
    change.setRoleName("something");
    AlterRoleSet element = new AlterRoleSet();
    element.setFromCurrent(Boolean.TRUE);
    element.setParameter("param");
    element.setValue("");
    element.setFromCurrent(false);
    change.setSet(element);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertEquals("errors", 1, errors.getErrorMessages().size());
    assertTrue("contains name error", errors.getErrorMessages().contains("use value OR fromCurrent"));
  }

  @Test
  public void changesetOptions() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/role/alter/changelog.test-options.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE my_role PASSWORD 'my_password'", sql[0].toSql());

  }

  @Test
  public void changesetRename() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/role/alter/changelog.test-rename.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE name RENAME TO new_name", sql[0].toSql());

  }

  @Test
  public void changesetReset() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/role/alter/changelog.test-reset.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 4);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE name RESET param1", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(1).getChanges().size());
    change = changeSets.get(1).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE ALL RESET ALL", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(2).getChanges().size());
    change = changeSets.get(2).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE name IN DATABASE database1 RESET param1", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(3).getChanges().size());
    change = changeSets.get(3).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE ALL RESET param1", sql[0].toSql());

  }

  @Test
  public void changesetSet() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/role/alter/changelog.test-set.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 6);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE name SET param1 FROM CURRENT", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(1).getChanges().size());
    change = changeSets.get(1).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE ALL SET param1 = value", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(2).getChanges().size());
    change = changeSets.get(2).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE name IN DATABASE database1 SET param1 = value", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(3).getChanges().size());
    change = changeSets.get(3).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE ALL SET param1 = value", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(4).getChanges().size());
    change = changeSets.get(4).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE ALL SET param1 TO DEFAULT", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(5).getChanges().size());
    change = changeSets.get(5).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER ROLE ALL SET param1 = value", sql[0].toSql());

  }

}
