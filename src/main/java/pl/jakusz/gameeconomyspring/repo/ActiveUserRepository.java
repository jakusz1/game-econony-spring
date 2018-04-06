package pl.jakusz.gameeconomyspring.repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveUserRepository {
    private Map<String,String> activeUsers = new HashMap<>();

    public ActiveUserRepository() {
    }



    public Map<String,String> getActiveUsers() {
        return activeUsers;
    }
    public int getSize(){
        return this.activeUsers.size();
    }
    public String getUsername(String sessionId){
        return this.activeUsers.get(sessionId);
    }

    public void addActiveUser(String sessionId, String username) {
        this.activeUsers.put(sessionId, username);
    }
    public void removeActiveUser(String sessionId) {
        this.activeUsers.remove(sessionId);
    }
}

