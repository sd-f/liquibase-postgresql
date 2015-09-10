/*
 */
package liquibase.ext.postgresql.grant;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public enum Operations {

  ALL,
  SELECT,
  INSERT,
  UPDATE,
  DELETE,
  TRUNCATE,
  REFERENCES,
  TRIGGER,
  USAGES,
  EXECUTE;

}
