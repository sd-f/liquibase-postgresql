/*
 */
package liquibase.ext.postgresql.createrole;

import java.io.IOException;
import java.util.Date;
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
  public void createSchemaFull() throws LiquibaseException, IOException {
    // given
    Liquibase liquibase = this.newLiquibase("/createrole/changelog-full.test.xml");
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
    assertEquals("Matching statement", "CREATE ROLE my_role SUPERUSER CREATEDB NOCREATEROLE INHERIT LOGIN CONNECTION LIMIT 1 ENCRYPTED PASSWORD 'my_password' VALID UNTIL '2002-05-30 09:00:00.0'", sql[0].toSql());
  }

  @Test
  public void createSchemaSimple() throws LiquibaseException, IOException {
    // given
    Liquibase liquibase = this.newLiquibase("/createrole/changelog.test.xml");
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
    assertEquals("Matching statement", "CREATE ROLE my_role NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT NOLOGIN PASSWORD 'my_password'", sql[0].toSql());

  }

}
