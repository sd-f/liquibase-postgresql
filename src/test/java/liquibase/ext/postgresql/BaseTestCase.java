package liquibase.ext.postgresql;

import java.io.IOException;
import java.net.URL;

import liquibase.Liquibase;
import liquibase.database.OfflineConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public abstract class BaseTestCase {

  private static final String TEST_BASE_PATH = "/liquibase/ext/postgresql";
  private String basePath;

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

}
