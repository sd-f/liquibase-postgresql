/*
 */
package liquibase.ext.postgresql.role;

import liquibase.ext.postgresql.xml.Constants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class RoleOptionsElementTest {

  @Test
  public void testNamespace() {
    // given
    RoleOptionsElement element = new RoleOptionsElement();

    // when
    String namespace = element.getSerializedObjectNamespace();

    // then
    assertEquals("namespace set", Constants.NAMESPACE, namespace);
  }

  @Test
  public void testObjectName() {
    // given
    RoleOptionsElement element = new RoleOptionsElement();

    // when
    String objectName = element.getSerializedObjectName();

    // then
    assertEquals("object name set", "options", objectName);
  }

}
