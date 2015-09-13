/*
 */
package liquibase.ext.postgresql.role;

import java.math.BigInteger;
import java.util.Date;

import liquibase.database.core.PostgresDatabase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class RoleOptionsSqlBuilderTest {

  @Test
  public void full() {
    // given
    RoleOptionsSqlBuilder builder = new RoleOptionsSqlBuilder(new StringBuilder(), new PostgresDatabase());
    RoleOptions options = new RoleOptions();

    options.setPassword("my_password");
    options.setConnectionLimit(BigInteger.valueOf(1));
    options.setCreateDatabase(Boolean.TRUE);
    options.setCreateRole(Boolean.TRUE);
    options.setEncryptedPassword(Boolean.TRUE);
    options.setInherit(Boolean.TRUE);
    options.setLoginAllowed(Boolean.FALSE);
    options.setSuperUser(Boolean.TRUE);
    options.setReplication(Boolean.TRUE);

    Date currentDate = new Date();
    options.setValidUntil(currentDate);

    // when
    String sqlString = builder.append(options).getSql().toString();

    // then
    assertEquals("SUPERUSER CREATEDB CREATEROLE INHERIT NOLOGIN CONNECTION LIMIT 1 ENCRYPTED PASSWORD 'my_password' VALID UNTIL '" + currentDate.toString() + "' REPLICATION", sqlString.trim());
  }

  @Test
  public void minimal() {
    // given
    RoleOptionsSqlBuilder builder = new RoleOptionsSqlBuilder(new StringBuilder(), new PostgresDatabase());
    RoleOptions options = new RoleOptions();

    // when
    String sqlString = builder.append(options).getSql().toString();

    // then
    assertEquals("", sqlString.trim());
  }

}
