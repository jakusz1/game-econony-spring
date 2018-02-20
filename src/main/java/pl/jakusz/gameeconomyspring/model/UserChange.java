package pl.jakusz.gameeconomyspring.model;

public class UserChange {
    private String username;
    private ChangeType changeType;

    public UserChange(String username, ChangeType changeType) {
        this.username = username;
        this.changeType = changeType;
    }

    public enum ChangeType {
        JOIN,
        LEAVE
    }
}
