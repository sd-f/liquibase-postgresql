package liquibase.ext.postgresql.alterdefaultprivileges;

import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.postgresql.grant.PrivilegesTargetType;
import liquibase.ext.postgresql.validation.AdvancedValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.structure.DatabaseObject;

public class AlterDefaultPrivilegesGenerator extends AbstractSqlGenerator<AlterDefaultPrivilegesStatement> {

  @Override
  public boolean supports(AlterDefaultPrivilegesStatement statement, Database database) {
    return database instanceof PostgresDatabase;
  }

  @Override
  public ValidationErrors validate(AlterDefaultPrivilegesStatement statement, Database database, SqlGeneratorChain chain) {
    AdvancedValidationErrors validationErrors = new AdvancedValidationErrors();

    if (statement.getForType() != null) {
      validationErrors.checkRequired("targetRole", statement.getTargetRole());
    } else {
      if (statement.getInSchema() != null) {
        validationErrors.checkRequired("inSchema", statement.getInSchema());
      }
    }

    validateGenericAttributes(validationErrors, statement);

    if (statement.getRevoke()) {
      if (statement.getCascade() != null && statement.getRestrict() != null
          && statement.getCascade() && statement.getRestrict()) {
        validationErrors.addError("Attributes \"restrict\" and \"cascade\" are excluding");
      }
    }

    return validationErrors;
  }

  private void validateGenericAttributes(AdvancedValidationErrors validationErrors, AlterDefaultPrivilegesStatement statement) {

    validationErrors.checkRequired("operation", statement.getOperation());
    validationErrors.checkRequired("onObjects", statement.getOnObjects());

    if (statement.getGroup() != null && statement.getGroup()
        && statement.getToOrFromRole() != null && "PUBLIC".equals(statement.getToOrFromRole())) {
      validationErrors.addError("PUBLIC and group can not be set together");
    }

    validationErrors.checkRequired("toOrFromRole", statement.getToOrFromRole());

  }

  /**
   * creates statement for create role in postgres sql<br />
   * <p>
   * @param statement
   * @param database
   * @param chain
   * @return <p>
   * @see http://www.postgresql.org/docs/9.4/static/sql-alterdefaultprivileges.html where option can be:<br />
   * <pre>
   * {@code
   * ALTER DEFAULT PRIVILEGES
   *    [ FOR { ROLE | USER } target_role [, ...] ]
   *    [ IN SCHEMA schema_name [, ...] ]
   *    abbreviated_grant_or_revoke
   * <p>
   * where abbreviated_grant_or_revoke is one of:
   * <p>
   * GRANT { { SELECT | INSERT | UPDATE | DELETE | TRUNCATE | REFERENCES | TRIGGER }
   *     [,...] | ALL [ PRIVILEGES ] }
   *     ON TABLES
   *     TO { [ GROUP ] role_name | PUBLIC } [, ...] [ WITH GRANT OPTION ]
   * <p>
   * GRANT { { USAGE | SELECT | UPDATE }
   *     [,...] | ALL [ PRIVILEGES ] }
   *     ON SEQUENCES
   *     TO { [ GROUP ] role_name | PUBLIC } [, ...] [ WITH GRANT OPTION ]
   * <p>
   * GRANT { EXECUTE | ALL [ PRIVILEGES ] }
   *     ON FUNCTIONS
   *     TO { [ GROUP ] role_name | PUBLIC } [, ...] [ WITH GRANT OPTION ]
   * <p>
   * REVOKE [ GRANT OPTION FOR ]
   *     { { SELECT | INSERT | UPDATE | DELETE | TRUNCATE | REFERENCES | TRIGGER }
   *     [,...] | ALL [ PRIVILEGES ] }
   *     ON TABLES
   *     FROM { [ GROUP ] role_name | PUBLIC } [, ...]
   *     [ CASCADE | RESTRICT ]
   * <p>
   * REVOKE [ GRANT OPTION FOR ]
   *     { { USAGE | SELECT | UPDATE }
   *     [,...] | ALL [ PRIVILEGES ] }
   *     ON SEQUENCES
   *     FROM { [ GROUP ] role_name | PUBLIC } [, ...]
   *     [ CASCADE | RESTRICT ]
   * <p>
   * REVOKE [ GRANT OPTION FOR ]
   *     { EXECUTE | ALL [ PRIVILEGES ] }
   *     ON FUNCTIONS
   *    FROM { [ GROUP ] role_name | PUBLIC } [, ...]
   *     [ CASCADE | RESTRICT ]
   * }
   * </pre>
   */
  @Override
  public Sql[] generateSql(AlterDefaultPrivilegesStatement statement, Database database, SqlGeneratorChain chain
  ) {
    StringBuilder sql = new StringBuilder("ALTER DEFAULT PRIVILEGES ");

    if (statement.getForType() != null) {
      sql.append("FOR ");
      if (PrivilegesTargetType.USER.toString().equals(statement.getForType().toString())) {
        sql.append(PrivilegesTargetType.USER);
      } else {
        sql.append(PrivilegesTargetType.ROLE);
      }
      sql.append(" ");
      sql.append(database.escapeObjectName(statement.getTargetRole(), DatabaseObject.class));
      sql.append(" ");
    }

    if (statement.getInSchema() != null && !statement.getInSchema().isEmpty()) {
      sql.append("IN SCHEMA ");
      sql.append(database.escapeObjectName(statement.getInSchema(), DatabaseObject.class));
      sql.append(" ");
    }

    if (statement.getRevoke()) {
      sql.append("REVOKE ");
    } else {
      sql.append("GRANT ");
    }

    addGrantOrRevokeOptions(statement, sql, database);

    return new Sql[]{new UnparsedSql(sql.toString())};
  }

  private void addGrantOrRevokeOptions(AlterDefaultPrivilegesStatement statement, StringBuilder sql, Database database) {

    sql.append(statement.getOperation());

    sql.append(" ON ");
    sql.append(statement.getOnObjects());

    if (statement.getRevoke()) {
      sql.append(" FROM ");
    } else {
      sql.append(" TO ");
    }

    if (statement.getGroup()) {
      sql.append("GROUP ");
    }

    sql.append(database.escapeObjectName(statement.getToOrFromRole(), DatabaseObject.class));

    sql.append(" ");

    if (!statement.getRevoke() && statement.getWithGrantOption() != null && statement.getWithGrantOption()) {
      sql.append("WITH GRANT OPTION ");
    }
    addRestrictOrCascade(statement.getRestrict(), statement.getCascade(), sql);
  }

  private void addRestrictOrCascade(Boolean restrict, Boolean cascade, StringBuilder sql) {
    if (restrict != null) {
      if (restrict) {
        sql.append("RESTRICT ");
      } else {
        if (cascade != null && cascade) {
          sql.append("CASCADE ");
        }
      }
    } else {
      if (cascade != null && cascade) {
        sql.append("CASCADE ");
      }
    }
  }

}
