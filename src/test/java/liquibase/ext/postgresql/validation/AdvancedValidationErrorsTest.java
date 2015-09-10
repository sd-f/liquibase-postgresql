/*
 */
package liquibase.ext.postgresql.validation;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AdvancedValidationErrorsTest {

  @Test
  public void testEmptyStringValidation() {
    // given
    String fieldName = "my_field";
    String fieldValue = "";

    // when
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();
    validationErrors.checkRequired(fieldName, fieldValue);

    // then
    assertTrue("empty string error added", validationErrors.getErrorMessages().contains("my_field must not be empty"));

  }

  @Test
  public void testEmptyStringValidationNull() {
    // given
    String fieldName = "my_field";
    String fieldValue = null;

    // when
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();
    validationErrors.checkRequired(fieldName, fieldValue);

    // then
    assertTrue("empty string error added", validationErrors.getErrorMessages().contains("my_field is required"));

  }

  @Test
  public void testEmptyStringValidationValue() {
    // given
    String fieldName = "my_field";
    String fieldValue = "value";

    // when
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();
    validationErrors.checkRequired(fieldName, fieldValue);

    // then
    assertEquals("no error added", 0, validationErrors.getErrorMessages().size());

  }

  @Test
  public void testEmptyStringValidationNoString() {
    // given
    String fieldName = "my_field";
    Integer fieldValue = 1;

    // when
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();
    validationErrors.checkRequired(fieldName, fieldValue);

    // then
    assertEquals("no error added", 0, validationErrors.getErrorMessages().size());

  }

}
