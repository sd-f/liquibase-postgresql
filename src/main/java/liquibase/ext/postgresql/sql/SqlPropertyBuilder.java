/*
 */
package liquibase.ext.postgresql.sql;

import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class SqlPropertyBuilder {

  protected final StringBuilder sql;

  public SqlPropertyBuilder(StringBuilder sql) {
    this.sql = sql;
  }

  public SqlPropertyBuilder append(String value, String propertyName, Boolean addQuotes) {
    if (value != null) {
      sql.append(propertyName);
      sql.append(" ");
      if (addQuotes) {
        sql.append("'");
      }
      sql.append(value);
      if (addQuotes) {
        sql.append("'");
      }
      sql.append(" ");
    }
    return this;
  }

  public SqlPropertyBuilder append(String value, String propertyName) {
    append(value, propertyName, true);
    return this;
  }

  public SqlPropertyBuilder append(Date value, String propertyName) {
    String stringValue = null;
    if (value != null) {
      stringValue = value.toString();
    }
    append(stringValue, propertyName, true);
    return this;
  }

  public SqlPropertyBuilder append(BigInteger value, String propertyName) {
    String stringValue = null;
    if (value != null) {
      stringValue = value.toString();
    }
    append(stringValue, propertyName, false);
    return this;
  }

  public SqlPropertyBuilder append(Boolean value, String trueValue, String falseValue, Boolean defaultValue) {
    if (value != null) {
      if (value) {
        sql.append(trueValue);
      } else {
        sql.append(falseValue);
      }
      sql.append(" ");
    } else if (defaultValue != null) {
      if (defaultValue) {
        sql.append(trueValue);
      } else {
        sql.append(falseValue);
      }
      sql.append(" ");
    }
    return this;
  }

  public SqlPropertyBuilder append(Boolean value, String trueValue, String falseValue) {
    append(value, trueValue, falseValue, null);
    return this;
  }

  public StringBuilder getSql() {
    return sql;
  }

}
