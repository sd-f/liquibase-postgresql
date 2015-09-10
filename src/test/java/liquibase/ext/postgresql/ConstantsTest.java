/*
 */
package liquibase.ext.postgresql;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import liquibase.ext.postgresql.xml.Constants;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class ConstantsTest {

  @Test
  public void testNameSpace() throws InstantiationException, IllegalAccessException {
    final Class<?> cls = Constants.class;
    final Constructor<?> c = cls.getDeclaredConstructors()[0];
    c.setAccessible(true);

    Throwable targetException = null;
    try {
      c.newInstance((Object[]) null);
    } catch (InvocationTargetException ite) {
      targetException = ite.getTargetException();
    }

    assertNotNull(targetException);
    assertEquals(targetException.getClass(), InstantiationException.class);
  }

}
