/*
 */
package liquibase.ext.postgresql.role;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class RoleOptionsTest {

  @Test
  public void testParser() {
    // given
    RoleOptionsElement element = new RoleOptionsElement();
    element.setValidUntil("BLABLABLA");

    // when
    RoleOptions options = new RoleOptions(element);

    // then
    // only logger output and date is null
    assertNull(options.getValidUntil());
  }

  @Test
  public void testEmptyOptions() {
    // given

    // when
    RoleOptions options = new RoleOptions(null);

    // then
    assertNotNull(options);
  }

}
