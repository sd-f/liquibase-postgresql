package liquibase.ext.postgresql.alterdefaultprivileges;

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

  private PrivilegesTargetType forType;
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
    inverse.setForType(getForType());
    inverse.setInSchema(getInSchema());
    inverse.setTargetRole(getTargetRole());

    // if original grant then use revoke
    if (getGrant() != null) {
      RevokeChange inverseRevoke = new RevokeChange();
      inverseRevoke.setFromRole(getGrant().getToRole());
      inverseRevoke.setGroup(getGrant().getGroup());
      inverseRevoke.setOnObjects(getGrant().getOnObjects());
      inverseRevoke.setOperations(getGrant().getOperations());
      inverse.setRevoke(inverseRevoke);
    } else if (getRevoke() != null) {
      GrantChange inverseGrant = new GrantChange();
      inverseGrant.setToRole(getRevoke().getFromRole());
      inverseGrant.setGroup(getRevoke().getGroup());
      inverseGrant.setOnObjects(getRevoke().getOnObjects());
      inverseGrant.setOperations(getRevoke().getOperations());
      inverse.setGrant(inverseGrant);
    }

    return new Change[]{inverse,};
  }

  @Override
  public SqlStatement[] generateStatements(Database database) {
    AlterDefaultPrivilegesStatement statement = new AlterDefaultPrivilegesStatement();

    statement.setForType(getForType());
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

  private void setGrantOptions(AbstractGrantChange change, AlterDefaultPrivilegesStatement statement) {
    statement.setGroup(change.getGroup());
    statement.setOnObjects(change.getOnObjects());
    statement.setOperations(change.getOperations());
  }

  public PrivilegesTargetType getForType() {
    return forType;
  }

  public void setForType(PrivilegesTargetType forType) {
    this.forType = forType;
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
