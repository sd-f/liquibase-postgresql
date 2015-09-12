/*
 */
package liquibase.ext.postgresql.sql;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class SqlBuilderTest {

  @Test
  public void append() {
    // given
    SqlBuilder builder = new SqlBuilder(new StringBuilder());
    String value = "A String";
    String propertyName = "PROPERTY";

    // when
    String sqlString = builder.append(value, propertyName).getSql().toString();

    // then
    assertEquals("PROPERTY 'A String'", sqlString.trim());
  }

  @Test
  public void appendEmptyString() {
    // given
    SqlBuilder builder = new SqlBuilder(new StringBuilder());
    String value = "";
    String propertyName = "PROPERTY";

    // when
    String sqlString = builder.append(value, propertyName).getSql().toString();

    // then
    assertEquals("PROPERTY ''", sqlString.trim());
  }

  @Test
  public void appendNull() {
    // given
    SqlBuilder builder = new SqlBuilder(new StringBuilder());
    String value = null;
    String propertyName = "PROPERTY";

    // when
    String sqlString = builder.append(value, propertyName).getSql().toString();

    // then
    assertEquals("", sqlString.trim());
  }

  @Test
  public void appendDate() {
    // given
    SqlBuilder builder = new SqlBuilder(new StringBuilder());
    Date value = new Date();
    String propertyName = "PROPERTY";

    // when
    String sqlString = builder.append(value, propertyName).getSql().toString();

    // then
    assertEquals("PROPERTY '" + value.toString() + "'", sqlString.trim());
  }

  @Test
  public void appendDateNull() {
    // given
    SqlBuilder builder = new SqlBuilder(new StringBuilder());
    Date value = null;
    String propertyName = "PROPERTY";

    // when
    String sqlString = builder.append(value, propertyName).getSql().toString();

    // then
    assertEquals("", sqlString.trim());
  }

  @Test
  public void appendBooleanTrue() {
    // given
    SqlBuilder builder = new SqlBuilder(new StringBuilder());
    Boolean value = true;
    String trueValue = "PROPERTY";
    String valueFalse = "NOPROPERTY";

    // when
    String sqlString = builder.append(value, trueValue, valueFalse).getSql().toString();

    // then
    assertEquals("PROPERTY", sqlString.trim());
  }

  @Test
  public void appendBooleanFalse() {
    // given
    SqlBuilder builder = new SqlBuilder(new StringBuilder());
    Boolean value = false;
    String trueValue = "PROPERTY";
    String valueFalse = "NOPROPERTY";

    // when
    String sqlString = builder.append(value, trueValue, valueFalse).getSql().toString();

    // then
    assertEquals("NOPROPERTY", sqlString.trim());
  }

  @Test
  public void appendBooleanNull() {
    // given
    SqlBuilder builder = new SqlBuilder(new StringBuilder());
    Boolean value = null;
    String trueValue = "PROPERTY";
    String valueFalse = "NOPROPERTY";

    // when
    String sqlString = builder.append(value, trueValue, valueFalse).getSql().toString();

    // then
    assertEquals("", sqlString.trim());
  }

  @Test
  public void appendBooleanDefaultTrue() {
    // given
    SqlBuilder builder = new SqlBuilder(new StringBuilder());
    Boolean value = null;
    String trueValue = "PROPERTY";
    String valueFalse = "NOPROPERTY";

    // when
    String sqlString = builder.append(value, trueValue, valueFalse, true).getSql().toString();

    // then
    assertEquals("PROPERTY", sqlString.trim());
  }

  @Test
  public void appendBooleanDefaultFalse() {
    // given
    SqlBuilder builder = new SqlBuilder(new StringBuilder());
    Boolean value = null;
    String trueValue = "PROPERTY";
    String valueFalse = "NOPROPERTY";

    // when
    String sqlString = builder.append(value, trueValue, valueFalse, false).getSql().toString();

    // then
    assertEquals("NOPROPERTY", sqlString.trim());
  }

}
