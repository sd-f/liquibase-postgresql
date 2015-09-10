/*
 */
package liquibase.ext.postgresql.alterdefaultprivileges;

import liquibase.ext.postgresql.grant.PrivilegesTargetType;
import liquibase.ext.postgresql.grant.GrantObjects;
import liquibase.ext.postgresql.grant.Operations;

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
import liquibase.ext.postgresql.grant.AbstractGrantChange;
import liquibase.ext.postgresql.grant.GrantChange;
import liquibase.ext.postgresql.grant.RevokeChange;
import liquibase.ext.postgresql.xml.Constants;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterDefaultPrivilegesTest extends BaseTestCase {

  private AlterDefaultPrivilegesChange getNewGrantChange(Boolean revoke) {
    AlterDefaultPrivilegesChange change = new AlterDefaultPrivilegesChange();

    change.setForPrivilegeType("ROLE");
    change.setTargetRole("my_role");
    change.setInSchema("my_schema");

    if (revoke) {
      RevokeChange grant = new RevokeChange();
      setGenericGrantOptions(grant);
      grant.setCascade(true);
      grant.setRestrict(false);
      grant.setFromRole("my_other_role");
      change.setRevoke(grant);

      assertEquals(Constants.NAMESPACE, grant.getSerializedObjectNamespace());
      assertEquals("revoke", grant.getSerializedObjectName());
    } else {
      GrantChange grant = new GrantChange();
      setGenericGrantOptions(grant);
      grant.setWithGrantOption(true);
      grant.setToRole("my_other_role");
      change.setGrant(grant);

      assertEquals(Constants.NAMESPACE, grant.getSerializedObjectNamespace());
      assertEquals("grant", grant.getSerializedObjectName());
    }

    return change;
  }

  private void setGenericGrantOptions(AbstractGrantChange grant) {
    grant.setOnObjects(GrantObjects.TABLES);
    grant.setOperation(Operations.ALL);
    grant.setGroup(false);

  }

  @Test
  public void generateStatements() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(false);

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals(PrivilegesTargetType.ROLE, ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getForType());
    assertEquals("my_role", ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getTargetRole());
    assertEquals("my_schema", ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getInSchema());
    assertEquals("my_other_role", ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getToOrFromRole());
    assertEquals(GrantObjects.TABLES, ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getOnObjects());
    assertEquals(Boolean.FALSE, ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getGroup());
    assertEquals(Boolean.FALSE, ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getRevoke());
  }

  @Test
  public void generateStatementsRevoke() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(true);

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals(PrivilegesTargetType.ROLE, ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getForType());
    assertEquals("my_role", ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getTargetRole());
    assertEquals("my_schema", ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getInSchema());
    assertEquals("my_other_role", ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getToOrFromRole());
    assertEquals(GrantObjects.TABLES, ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getOnObjects());
    assertEquals(Boolean.FALSE, ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getGroup());
    assertEquals(Boolean.TRUE, ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getRevoke());
  }

  @Test
  public void createInverseRevoke() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(false);

    // when
    Change[] changes = change.createInverses();

    // then
    assertEquals(1, changes.length);
    Assert.assertNotNull("reverse grant is created", ((AlterDefaultPrivilegesChange) changes[0]).getRevoke());
    assertEquals(change.getGrant().getToRole(), ((AlterDefaultPrivilegesChange) changes[0]).getRevoke().getFromRole());
  }

  @Test
  public void createInverseGrant() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(true);
    change.getRevoke().setCascade(null);

    // when
    Change[] changes = change.createInverses();

    // then
    assertEquals(1, changes.length);
    Assert.assertNotNull("reverse grant is created", ((AlterDefaultPrivilegesChange) changes[0]).getGrant());
    assertEquals(change.getRevoke().getFromRole(), ((AlterDefaultPrivilegesChange) changes[0]).getGrant().getToRole());
  }

  @Test
  public void generateStatementsGrant() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(true);

    // when
    SqlStatement[] sqlStatements = change.generateStatements(new PostgresDatabase());

    // then
    assertEquals(1, sqlStatements.length);
    assertEquals(Boolean.TRUE, ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getRevoke());
    assertEquals("my_other_role", ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getToOrFromRole());
  }

  @Test
  public void validate() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(true);

    change.setTargetRole(null);
    change.getRevoke().setCascade(true);
    change.getRevoke().setRestrict(true);

    change.getRevoke().setOnObjects(null);
    change.getRevoke().setGroup(null);
    change.getRevoke().setFromRole("PUBLIC");
    change.getRevoke().setOperation(null);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error targetRole", errors.getErrorMessages().contains("targetRole is required"));
    assertTrue("contains error cascade restrict excluding", errors.getErrorMessages().contains("Attributes \"restrict\" and \"cascade\" are excluding"));
    assertTrue("contains error on objects mandatory", errors.getErrorMessages().contains("onObjects is required"));
    assertTrue("contains error operation mandatory", errors.getErrorMessages().contains("operation is required"));
  }

  @Test
  public void validateGrant() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(false);
    change.setTargetRole(null);

    change.getGrant().setOnObjects(null);
    change.getGrant().setGroup(true);
    change.getGrant().setToRole("PUBLIC");
    change.getGrant().setOperation(null);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error targetRole", errors.getErrorMessages().contains("targetRole is required"));
    assertTrue("contains error on objects mandatory", errors.getErrorMessages().contains("onObjects is required"));
    assertTrue("contains error public is no group", errors.getErrorMessages().contains("PUBLIC and group can not be set together"));
    assertTrue("contains error operation mandatory", errors.getErrorMessages().contains("operation is required"));
  }

  @Test
  public void validatePublicGroup() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(true);
    change.setTargetRole(null);
    change.getRevoke().setCascade(true);
    change.getRevoke().setRestrict(true);

    change.getRevoke().setOnObjects(null);
    change.getRevoke().setGroup(false);
    change.getRevoke().setFromRole(null);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error targetRole", errors.getErrorMessages().contains("toOrFromRole is required"));
  }

  @Test
  public void validatePublicRevoke() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(true);
    change.getRevoke().setFromRole("PUBLIC");
    change.getRevoke().setGroup(true);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error public is no group", errors.getErrorMessages().contains("PUBLIC and group can not be set together"));
  }

  @Test
  public void validatePublicGrant() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(false);
    change.getGrant().setToRole("PUBLIC");
    change.getGrant().setGroup(true);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertTrue("contains error public is no group", errors.getErrorMessages().contains("PUBLIC and group can not be set together"));
  }

  @Test
  public void validatePublicNullRole() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(false);
    change.getGrant().setToRole(null);
    change.getGrant().setGroup(true);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertFalse("contains error public is no group", errors.getErrorMessages().contains("PUBLIC and group can not be set together"));
  }

  @Test
  public void validateCascadeRestrictFallback() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(true);
    change.getRevoke().setCascade(null);
    change.getRevoke().setRestrict(true);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertFalse("not contains error cascade", errors.getErrorMessages().contains("Attributes \"restrict\" and \"cascade\" are excluding"));
  }

  @Test
  public void validateCascadeCascadeFallback() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(true);
    change.getRevoke().setCascade(true);
    change.getRevoke().setRestrict(null);

    // when
    ValidationErrors errors = change.validate(new PostgresDatabase());

    // then
    assertFalse("not contains error cascade", errors.getErrorMessages().contains("Attributes \"restrict\" and \"cascade\" are excluding"));
  }

  @Test
  public void getConfirmationMessage() {
    // given
    AlterDefaultPrivilegesChange change = getNewGrantChange(true);

    // when
    String message = change.getConfirmationMessage();

    // then
    assertEquals("Default privileges altered", message);
  }

  @Test
  public void getChangeMetaData() {
    // given
    AlterDefaultPrivilegesChange change = new AlterDefaultPrivilegesChange();

    // when then
    assertEquals("alterDefaultPrivileges", ChangeFactory.getInstance().getChangeMetaData(change).getName());
    assertEquals("Alter default privileges", ChangeFactory.getInstance().getChangeMetaData(change).getDescription());
    assertEquals(ChangeMetaData.PRIORITY_DEFAULT, ChangeFactory.getInstance().getChangeMetaData(change).getPriority());
  }

  @Test
  public void alterDefaultPrivilegesGrant() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterdefaultprivileges/changelog-grant.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER DEFAULT PRIVILEGES FOR USER my_role GRANT ALL ON TABLES TO PUBLIC", sql[0].toSql());

  }

  @Test
  public void alterDefaultPrivilegesRevoke() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterdefaultprivileges/changelog-revoke.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 3);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER DEFAULT PRIVILEGES FOR ROLE my_role IN SCHEMA my_schema REVOKE ALL ON TABLES FROM PUBLIC CASCADE", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(1).getChanges().size());
    change = changeSets.get(1).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER DEFAULT PRIVILEGES FOR ROLE my_role IN SCHEMA my_schema REVOKE ALL ON TABLES FROM PUBLIC", sql[0].toSql());

    // then
    assertEquals("One change given", 1, changeSets.get(2).getChanges().size());
    change = changeSets.get(2).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER DEFAULT PRIVILEGES FOR ROLE my_role IN SCHEMA my_schema REVOKE ALL ON TABLES FROM PUBLIC", sql[0].toSql());

  }

  @Test
  public void alterDefaultPrivilegesRevokeGroupRestrict() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterdefaultprivileges/changelog-revoke-group.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER DEFAULT PRIVILEGES FOR ROLE my_role IN SCHEMA my_schema REVOKE ALL ON TABLES FROM GROUP my_group RESTRICT", sql[0].toSql());

  }

  @Test
  public void alterDefaultPrivilegesGrantNulls() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterdefaultprivileges/changelog-grant-nulls1.test.xml";

    // when
    try {
      List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);
    } catch (ValidationFailedException ex) {
      assertTrue("validation failed", ex.getMessage().contains("targetRole is required"));
      return;
    }
    assertTrue("validation failed - no exception", false);
  }

  @Test
  public void alterDefaultPrivilegesGrantNulls2() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterdefaultprivileges/changelog-grant-nulls2.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);
    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER DEFAULT PRIVILEGES IN SCHEMA my_schema GRANT ALL ON TABLES TO PUBLIC", sql[0].toSql());
  }

  @Test
  public void alterDefaultPrivilegesGrantNulls3() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterdefaultprivileges/changelog-grant-nulls3.test.xml";

    // when
    try {
      List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);
    } catch (ValidationFailedException ex) {
      assertTrue("validation failed", ex.getMessage().contains("toOrFromRole must not be empty"));
      return;
    }
    assertTrue("validation failed - no exception", false);
  }

  @Test
  public void alterDefaultPrivilegesGrantNulls4() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterdefaultprivileges/changelog-grant-nulls4.test.xml";

    // when
    try {
      List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);
    } catch (ValidationFailedException ex) {
      assertTrue("validation failed", ex.getMessage().contains("toOrFromRole is required"));
      return;
    }
    assertTrue("validation failed - no exception", false);
  }

  @Test
  public void alterDefaultPrivilegesGrantNulls5() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterdefaultprivileges/changelog-grant-nulls5.test.xml";

    // when
    try {
      List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 1);
    } catch (ValidationFailedException ex) {
      assertTrue("validation failed", ex.getMessage().contains("inSchema must not be empty"));
      return;
    }
    assertTrue("validation failed - no exception", false);
  }

  @Test
  public void alterDefaultPrivilegesFull() throws LiquibaseException, IOException {
    // given
    String changeLogFile = "/alterdefaultprivileges/changelog-full.test.xml";

    // when
    List<ChangeSet> changeSets = generateChangeSets(changeLogFile, 3);

    // then
    assertEquals("One change given", 1, changeSets.get(0).getChanges().size());
    Change change = changeSets.get(0).getChanges().get(0);

    // when
    Sql[] sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER DEFAULT PRIVILEGES FOR USER my_role IN SCHEMA my_schema GRANT SELECT ON TABLES TO my_other_role WITH GRANT OPTION", sql[0].toSql());

    assertEquals("One change given", 1, changeSets.get(1).getChanges().size());
    change = changeSets.get(1).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER DEFAULT PRIVILEGES FOR USER my_role IN SCHEMA my_schema REVOKE SELECT ON TABLES FROM my_other_role", sql[0].toSql());

    assertEquals("One change given", 1, changeSets.get(2).getChanges().size());
    change = changeSets.get(2).getChanges().get(0);

    // when
    sql = generateStatements(change);

    // then
    assertEquals("One statement generated", 1, sql.length);
    assertEquals("Matching statement", "ALTER DEFAULT PRIVILEGES FOR ROLE my_role REVOKE SELECT ON TABLES FROM my_other_role CASCADE", sql[0].toSql());

  }

}
