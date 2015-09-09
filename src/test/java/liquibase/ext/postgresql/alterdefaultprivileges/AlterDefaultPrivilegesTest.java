/*
 */
package liquibase.ext.postgresql.alterdefaultprivileges;

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
import liquibase.ext.postgresql.grant.AbstractGrantChange;
import liquibase.ext.postgresql.grant.GrantChange;
import liquibase.ext.postgresql.grant.RevokeChange;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.SqlStatement;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AlterDefaultPrivilegesTest extends BaseTestCase {

  private AlterDefaultPrivilegesChange getNewGrantChange(Boolean revoke) {
    AlterDefaultPrivilegesChange change = new AlterDefaultPrivilegesChange();

    change.setForType(PrivilegesTargetType.ROLE);
    change.setTargetRole("my_role");
    change.setInSchema("my_schema");

    if (revoke) {
      RevokeChange grant = new RevokeChange();
      setGenericGrantOptions(grant);
      grant.setCascade(true);
      grant.setRestrict(false);
      grant.setFromRole("my_other_role");
      change.setRevoke(grant);
    } else {
      GrantChange grant = new GrantChange();
      setGenericGrantOptions(grant);
      grant.setWithGrantOption(true);
      grant.setToRole("my_other_role");
      change.setGrant(grant);
    }

    return change;
  }

  private void setGenericGrantOptions(AbstractGrantChange grant) {
    grant.setOnObjects(GrantObjects.TABLES);
    grant.setOperations(Operations.ALL);
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
    assertEquals(Boolean.TRUE, ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getRevoke());
    assertEquals("my_other_role", ((AlterDefaultPrivilegesStatement) sqlStatements[0]).getToOrFromRole());
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
    Liquibase liquibase = this.newLiquibase("/alterdefaultprivileges/changelog-grant.test.xml");
    Database database = liquibase.getDatabase();
    DatabaseChangeLog changeLog = liquibase.getDatabaseChangeLog();

    // when
    changeLog.validate(database);
    List<ChangeSet> changeSets = changeLog.getChangeSets();

    // then
    assertEquals("One changesets given", 1, changeSets.size());

    ChangeSet changeSet = changeSets.get(0);

    assertEquals("One change given", 1, changeSet.getChanges().size());

    Change change = changeSet.getChanges().get(0);

    // when
    Sql[] sql = SqlGeneratorFactory.getInstance()
        .generateSql(change.generateStatements(database)[0], database);

    assertEquals("One statement generated", 1, sql.length);

    // then
    assertEquals("Matching statement", "ALTER DEFAULT PRIVILEGES FOR ROLE my_role IN SCHEMA my_schema GRANT ALL ON TABLES TO PUBLIC WITH GRANT OPTION", sql[0].toSql());

  }

  @Test
  public void alterDefaultPrivilegesRevoke() throws LiquibaseException, IOException {
    // given
    Liquibase liquibase = this.newLiquibase("/alterdefaultprivileges/changelog-revoke.test.xml");
    Database database = liquibase.getDatabase();
    DatabaseChangeLog changeLog = liquibase.getDatabaseChangeLog();

    // when
    changeLog.validate(database);
    List<ChangeSet> changeSets = changeLog.getChangeSets();

    // then
    assertEquals("One changesets given", 1, changeSets.size());

    ChangeSet changeSet = changeSets.get(0);

    assertEquals("One change given", 1, changeSet.getChanges().size());

    Change change = changeSet.getChanges().get(0);

    // when
    Sql[] sql = SqlGeneratorFactory.getInstance()
        .generateSql(change.generateStatements(database)[0], database);

    assertEquals("One statement generated", 1, sql.length);

    // then
    assertEquals("Matching statement", "ALTER DEFAULT PRIVILEGES FOR ROLE my_role IN SCHEMA my_schema REVOKE ALL ON TABLES FROM PUBLIC CASCADE", sql[0].toSql());

  }

}