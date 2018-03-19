package pl.jakusz.gameeconomyspring.model;

public class UserChange {
    private String username;
    private ChangeType changeType;

    public UserChange() {
    }

    public UserChange(String username, ChangeType changeType) {
        this.username = username;
        this.changeType = changeType;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username=username;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public enum ChangeType {
        JOIN,
        LEAVE
    }
}
