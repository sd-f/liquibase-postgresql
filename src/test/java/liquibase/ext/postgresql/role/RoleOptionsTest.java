/*
 */
package liquibase.ext.postgresql.role;

import java.text.ParseException;

import liquibase.ext.postgresql.BaseTestCase;
import liquibase.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
@RunWith(MockitoJUnitRunner.class)
public class RoleOptionsTest extends BaseTestCase {

  @Test
  public void testParser() {
    // given
    RoleOptions.LOGGER = mock(Logger.class);
    RoleOptionsElement element = new RoleOptionsElement();
    element.setValidUntil("BLABLABLA");

    // when
    RoleOptions options = new RoleOptions();
    options.setAttributesFromElement(element);

    // then
    // only logger output and date is null
    verify(RoleOptions.LOGGER).severe(eq("Error parsing options"), isA(ParseException.class));

    assertNull(options.getValidUntil());
  }

  @Test
  public void testEmptyOptions() {
    // given

    // when
    RoleOptions options = new RoleOptions();
    options.setAttributesFromElement(null);

    // then
    assertNotNull(options);
  }

}
