package main;

import rest.UserProfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author esin88
 */
public class AccountService {
    private Map<Long, UserProfile> users = new HashMap<>();
    private Map<String, UserProfile> sessions = new HashMap<>();
    public UserProfile onlineUser;

    public AccountService() {
        this.onlineUser = null;
        UserProfile bufUser = new UserProfile("admin", "admin","admin@email.ru");
        users.put(bufUser.getUserID(), bufUser);
        bufUser = new UserProfile("guest", "12345","guest@email.ru");
        users.put(bufUser.getUserID(), bufUser);
    }

    public Collection<UserProfile> getAllUsers() {
        return users.values();
    }

    public boolean addUser(UserProfile user) {
        if (this.getUserByLogin(user.getLogin()) != null || this.getUserByEmail(user.getEmail()) != null)
            return false;
        user.setUserID();
        users.put(user.getUserID(), user);
        System.out.append("User created: {").append(String.valueOf(user.getUserID())).append(", ")
                .append(String.valueOf(user.getLogin())).append(", ")
                .append(String.valueOf(String.valueOf(user.getPassword()))).append(", ")
                .append(String.valueOf(String.valueOf(user.getEmail()))).append("}").append('\n');
        return true;
    }

    public UserProfile getUser(long userID) {
        return users.get(userID);
    }
    public UserProfile getUserByLogin(String login){
        Iterator<Map.Entry<Long, UserProfile>> entries = users.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Long, UserProfile> entry = entries.next();
            if (entry.getValue().getLogin().equals(login)) {
                return entry.getValue();
            }

        }
        return  null;
    }
    public UserProfile getUserByEmail(String email){
        Iterator<Map.Entry<Long, UserProfile>> entries = users.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Long, UserProfile> entry = entries.next();
            if (entry.getValue().getEmail().equals(email)) {
                return entry.getValue();
            }

        }
        return  null;
    }
    public boolean isValidUser(UserProfile user) {
        UserProfile actualUser = getUserByLogin(user.getLogin());
        return (actualUser != null && actualUser.getPassword().equals(user.getPassword()));
    }
    public void addSession(String sessionId, UserProfile user) {
        System.out.append("Session add: {").append(String.valueOf(user.getUserID())).append(", ")
                .append(String.valueOf(user.getLogin())).append(", ")
                .append(String.valueOf(String.valueOf(user.getPassword()))).append(", ")
                .append(String.valueOf(String.valueOf(user.getEmail()))).append("}").append('\n');
        sessions.put(sessionId, user);
    }


}