package liquibase.ext.postgresql.role.alter;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.postgresql.role.alter.reset.AlterRoleReset;
import liquibase.ext.postgresql.role.alter.set.AlterRoleSet;
import liquibase.ext.postgresql.role.RoleOptionsSqlBuilder;
import liquibase.ext.postgresql.validation.AdvancedValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.structure.DatabaseObject;

public class AlterRoleGenerator extends AbstractSqlGenerator<AlterRoleStatement> {

  @Override
  public boolean supports(AlterRoleStatement statement, Database database) {
    return database instanceof PostgresDatabase;
  }

  @Override
  public ValidationErrors validate(AlterRoleStatement statement, Database database, SqlGeneratorChain chain) {
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();
    validationErrors.checkRequired("roleName", statement.getRoleName());
    Integer subElementFound = 0;
    if (statement.getOptions() != null) {
      subElementFound++;
    }

    if (statement.getRenameTo() != null) {
      subElementFound++;
      validationErrors.checkRequired("name", statement.getRenameTo().getName());
    }

    if (statement.getSet() != null) {
      subElementFound++;
      validateSet(validationErrors, statement.getSet());
    }

    if (statement.getReset() != null) {
      subElementFound++;
      validationErrors.checkRequired("parameter", statement.getReset().getParameter());
    }

    if (subElementFound == 0) {
      validationErrors.addError("subelement is missing");
    }

    return validationErrors;
  }

  private void validateSet(AdvancedValidationErrors validationErrors, AlterRoleSet set) {
    validationErrors.checkRequired("parameter", set.getParameter());

    Integer valuesFound = 0;
    if (set.getFromCurrent() != null && set.getFromCurrent()) {
      valuesFound++;
    }
    if (set.getValue() != null && !set.getValue().isEmpty()) {
      valuesFound++;
    }
    if (valuesFound != 1) {
      validationErrors.addError("use value OR fromCurrent");
    }
  }

  /**
   * creates statement for altering role in postgres sql<br />
   * <p>
   * @param statement
   * @param database
   * @param chain
   * @return <p>
   * @see @ http://www.postgresql.org/docs/9.4/static/sql-alterrole.html where option can be:<br />
   * <pre>
   * {@code
   * ALTER ROLE name [ [ WITH ] option [ ... ] ]
   * <p>
   * where option can be:
   * <p>
   * SUPERUSER | NOSUPERUSER
   * | CREATEDB | NOCREATEDB
   * | CREATEROLE | NOCREATEROLE
   * | CREATEUSER | NOCREATEUSER
   * | INHERIT | NOINHERIT
   * | LOGIN | NOLOGIN
   * | REPLICATION | NOREPLICATION
   * | CONNECTION LIMIT connlimit
   * | [ ENCRYPTED | UNENCRYPTED ] PASSWORD 'password'
   * | VALID UNTIL 'timestamp'
   * <p>
   * ALTER ROLE name RENAME TO new_name
   * <p>
   * ALTER ROLE name [ IN DATABASE database_name ] SET configuration_parameter { TO | = } { value | DEFAULT }
   * ALTER ROLE { name | ALL } [ IN DATABASE database_name ] SET configuration_parameter FROM CURRENT
   * ALTER ROLE { name | ALL } [ IN DATABASE database_name ] RESET configuration_parameter
   * ALTER ROLE { name | ALL } [ IN DATABASE database_name ] RESET ALL
   * }
   * </pre>
   */
  @Override
  public Sql[] generateSql(AlterRoleStatement statement, Database database, SqlGeneratorChain chain) {
    StringBuilder sql = new StringBuilder("ALTER ROLE ");

    appendAllOrString(sql, statement.getRoleName(), "ALL", database);

    if (statement.getOptions() != null) {
      RoleOptionsSqlBuilder roleOptionsSqlBuilder = new RoleOptionsSqlBuilder(sql, database);
      roleOptionsSqlBuilder.append(statement.getOptions());
    }

    if (statement.getRenameTo() != null) {
      sql.append("RENAME TO ");
      sql.append(database.escapeObjectName(statement.getRenameTo().getName(), DatabaseObject.class));
      sql.append(" ");
    }

    appendSet(sql, statement.getSet(), database);

    appendReset(sql, statement.getReset(), database);

    return new Sql[]{new UnparsedSql(sql.toString())};
  }

  private void appendSet(StringBuilder sql, AlterRoleSet set, Database database) {
    if (set != null) {
      appendInDatabase(sql, set.getInDatabase(), database);

      sql.append("SET ");

      appendAllOrString(sql, set.getParameter(), "ALL", database);

      if ("DEFAULT".equals(set.getValue())) {
        sql.append("TO DEFAULT");
      } else if (set.getFromCurrent() != null && set.getFromCurrent()) {
        sql.append("FROM CURRENT");
      } else {
        sql.append("= ");
        sql.append(set.getValue());
      }

      sql.append(" ");
    }
  }

  private void appendReset(StringBuilder sql, AlterRoleReset reset, Database database) {
    if (reset != null) {
      appendInDatabase(sql, reset.getInDatabase(), database);
      sql.append("RESET ");
      appendAllOrString(sql, reset.getParameter(), "ALL", database);
    }
  }

  private void appendAllOrString(StringBuilder sql, String value, String constant, Database database) {
    if (constant.equals(value)) {
      sql.append(value);
    } else {
      sql.append(database.escapeObjectName(value, DatabaseObject.class));
    }
    sql.append(" ");
  }

  private void appendInDatabase(StringBuilder sql, String value, Database database) {
    if (value != null && !value.isEmpty()) {
      sql.append("IN DATABASE ");
      sql.append(database.escapeObjectName(value, DatabaseObject.class));
      sql.append(" ");
    }
  }

}
