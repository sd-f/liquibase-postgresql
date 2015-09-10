package liquibase.ext.postgresql.alterdefaultprivileges;

import liquibase.ext.postgresql.grant.PrivilegesTargetType;
import liquibase.change.AbstractChange;
import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.postgresql.grant.AbstractGrantChange;
import liquibase.ext.postgresql.grant.GrantChange;
import liquibase.ext.postgresql.grant.RevokeChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "alterDefaultPrivileges", description = "Alter default privileges", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class AlterDefaultPrivilegesChange extends AbstractChange {

  private String forPrivilegeType;
  private String inSchema;
  private String targetRole;
  private GrantChange grant;
  private RevokeChange revoke;

  @Override
  public String getConfirmationMessage() {
    return "Default privileges altered";
  }

  @Override
  protected Change[] createInverses() {
    AlterDefaultPrivilegesChange inverse = new AlterDefaultPrivilegesChange();
    inverse.setForPrivilegeType(getForPrivilegeType());
    inverse.setInSchema(getInSchema());
    inverse.setTargetRole(getTargetRole());

    // if original grant then use revoke
    if (getRevoke() != null) {
      GrantChange inverseGrant = new GrantChange();
      inverseGrant.setToRole(getRevoke().getFromRole());
      inverseGrant.setGroup(getRevoke().getGroup());
      inverseGrant.setOnObjects(getRevoke().getOnObjects());
      inverseGrant.setOperation(getRevoke().getOperation());
      inverse.setGrant(inverseGrant);
    } else {
      RevokeChange inverseRevoke = new RevokeChange();
      inverseRevoke.setFromRole(getGrant().getToRole());
      inverseRevoke.setGroup(getGrant().getGroup());
      inverseRevoke.setOnObjects(getGrant().getOnObjects());
      inverseRevoke.setOperation(getGrant().getOperation());
      inverse.setRevoke(inverseRevoke);
    }

    return new Change[]{inverse,};
  }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    AlterDefaultPrivilegesStatement statement = new AlterDefaultPrivilegesStatement();

    setForTypeFromString(statement, getForPrivilegeType());
    statement.setInSchema(getInSchema());
    statement.setTargetRole(getTargetRole());

    if (getRevoke() != null) {
      setGrantOptions(getRevoke(), statement);
      statement.setCascade(getRevoke().getCascade());
      statement.setRestrict(getRevoke().getRestrict());
      statement.setToOrFromRole(getRevoke().getFromRole());
      statement.setRevoke(true);
    }

    if (getGrant() != null) {
      setGrantOptions(getGrant(), statement);
      statement.setWithGrantOption(getGrant().getWithGrantOption());
      statement.setToOrFromRole(getGrant().getToRole());
      statement.setRevoke(false);
    }

    return new SqlStatement[]{statement};
  }

  private void setForTypeFromString(AlterDefaultPrivilegesStatement statement, String forTypeString) {
    if (forTypeString != null && !forTypeString.isEmpty()) {
      if (PrivilegesTargetType.USER.toString().equals(forTypeString)) {
        statement.setForType(PrivilegesTargetType.USER);
      } else {
        statement.setForType(PrivilegesTargetType.ROLE);
      }
    }
  }

  private void setGrantOptions(AbstractGrantChange change, AlterDefaultPrivilegesStatement statement) {
    statement.setGroup(change.getGroup());
    statement.setOnObjects(change.getOnObjects());
    statement.setOperation(change.getOperation());
  }

  public String getForPrivilegeType() {
    return forPrivilegeType;
  }

  public void setForPrivilegeType(String forPrivilegeType) {
    this.forPrivilegeType = forPrivilegeType;
  }

  public String getInSchema() {
    return inSchema;
  }

  public void setInSchema(String inSchema) {
    this.inSchema = inSchema;
  }

  public String getTargetRole() {
    return targetRole;
  }

  public void setTargetRole(String targetRole) {
    this.targetRole = targetRole;
  }

  public GrantChange getGrant() {
    return grant;
  }

  public void setGrant(GrantChange grant) {
    this.grant = grant;
  }

  public RevokeChange getRevoke() {
    return revoke;
  }

  public void setRevoke(RevokeChange revoke) {
    this.revoke = revoke;
  }

}
