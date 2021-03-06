/*
 */
package liquibase.ext.postgresql.xml;

/**
 *
 * @author Lucas Reeh <lreeh@tugraz.at>
 */
public class Constants {

  public static final String NAMESPACE = "http://www.liquibase.org/xml/ns/dbchangelog-ext";

  private Constants() throws InstantiationException {
    throw new InstantiationException("Instances of this type are forbidden.");
  }

}
