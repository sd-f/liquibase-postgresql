package liquibase.ext.postgresql;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import liquibase.Liquibase;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.OfflineConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorFactory;

import static junit.framework.TestCase.assertEquals;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public abstract class BaseTestCase {

  private static final String TEST_BASE_PATH = "/liquibase/ext/postgresql";
  private String basePath;
  private Database database;

  public Liquibase newLiquibase(String changeLogFile) throws LiquibaseException, IOException {
    this.setBasePath(changeLogFile);
    FileSystemResourceAccessor resourceAccessor = new FileSystemResourceAccessor(this.getBasePath());
    String connectionString = "offline:postgresql?catalog=TEST&defaultSchemaName=TEST&changeLogFile="
        + this.getBasePath() + "/databasechangelog.csv";
    OfflineConnection connection = new OfflineConnection(connectionString, resourceAccessor);
    return new Liquibase(this.getBasePath() + changeLogFile, resourceAccessor, connection);
  }

  private void setBasePath(String resource) throws IOException {
    URL resourcePath = getClass().getResource(TEST_BASE_PATH + resource);
    if (resourcePath == null) {
      throw new IOException("Changelog not found in " + TEST_BASE_PATH + resource);
    }
    basePath = resourcePath.getPath();

    basePath = basePath.replace(resource, "");
  }

  public String getBasePath() {
    return basePath;
  }

  public Sql[] generateStatements(Change change) {
    return SqlGeneratorFactory.getInstance()
        .generateSql(change.generateStatements(database)[0], database);
  }

  public List<ChangeSet> generateChangeSets(String changelogFile, Integer expectedChangesets) throws LiquibaseException, IOException {
    Liquibase liquibase = this.newLiquibase(changelogFile);
    this.setDatabase(liquibase.getDatabase());

    DatabaseChangeLog changeLog = liquibase.getDatabaseChangeLog();

    changeLog.validate(database);

    List<ChangeSet> changeSets = changeLog.getChangeSets();

    assertEquals("One changesets given", expectedChangesets.intValue(), changeSets.size());
    return changeSets;
  }

  public Database getDatabase() {
    return database;
  }

  public void setDatabase(Database database) {
    this.database = database;
  }

}
