/*
 */
package liquibase.ext.postgresql.validation;

import liquibase.exception.ValidationErrors;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class AdvancedValidationErrors extends ValidationErrors {

  public void checkRequired(String requiredFieldName, Object value) {
    if (value != null && (value instanceof String)) {
      String valueString = (String) value;
      if (valueString.isEmpty()) {
        addError(requiredFieldName + " must not be empty");
        return;
      }
    }
    checkRequiredField(requiredFieldName, value);
  }

}
