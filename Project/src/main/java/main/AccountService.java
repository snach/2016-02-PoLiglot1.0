package main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rest.UserProfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * created by snach
 */
public class AccountService {
    private final Map<Long, UserProfile> users = new HashMap<>();
    private final Map<String, UserProfile> sessions = new HashMap<>();


    public AccountService() {
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
        System.out.append("Добавлен : ").append(users.get(user.getUserID()).getLogin()).append("\n");
        System.out.append("User created: {").append(String.valueOf(user.getUserID())).append(", ")
                .append(String.valueOf(user.getLogin())).append(", ")
                .append(String.valueOf(String.valueOf(user.getPassword()))).append(", ")
                .append(String.valueOf(String.valueOf(user.getEmail()))).append("}").append('\n');
        return true;
    }
    @Nullable
    public UserProfile getUserByID(long userID) {

        if (users.get(userID) != null){
            return users.get(userID);
        } else {
            return null;
        }
    }
    @Nullable
    public UserProfile getUserByLoginInSession(String login){
        for(Map.Entry<String,UserProfile> entry: sessions.entrySet()) {
            if (entry.getValue().getLogin().equals(login))
                return entry.getValue();
        }
        return  null;
    }

    @Nullable
    public UserProfile getUserByLogin(String login){
        for(Map.Entry<Long,UserProfile> entry: users.entrySet()) {
            if (entry.getValue().getLogin().equals(login))
                return entry.getValue();
        }
        return  null;
    }

    @Nullable
    public UserProfile getUserByEmail(String email){
        for(Map.Entry<Long,UserProfile> entry: users.entrySet()) {
            if (entry.getValue().getEmail().equals(email))
                return entry.getValue();
        }
        return  null;
    }
   // @Nullable
    public UserProfile getUserBySession(@Nullable  String sessionID){
        //if (sessions.get(sessionID) != null){
            return sessions.get(sessionID);
        //} else {
        //    return null;
        //}
    }
    public boolean isEnter(String sessionID) {
        return sessions.containsKey(sessionID);
    }
    public void addSession(String sessionID, UserProfile user) {
            sessions.put(sessionID,user);
            System.out.append("Session add: {").append(String.valueOf(user.getUserID())).append(", ")
                    .append(String.valueOf(user.getLogin())).append(", ")
                    .append(String.valueOf(String.valueOf(user.getPassword()))).append(", ")
                    .append(String.valueOf(String.valueOf(user.getEmail()))).append("}").append('\n');
        }
    public void deleteSession(String sessionID){
        sessions.remove(sessionID);
        if(sessions.containsKey(sessionID)) System.out.append("Не удалилось\n");
        else System.out.append("Сессия удалена\n");
    }
    public void editUser(@NotNull UserProfile oldUser, UserProfile newUser) {
        if(!oldUser.getLogin().equals(newUser.getLogin()) && !newUser.getLogin().isEmpty() && this.getUserByLogin(newUser.getLogin()) == null){
            users.get(oldUser.getUserID()).setLogin(newUser.getLogin());
        }
        if(!oldUser.getEmail().equals(newUser.getEmail()) && !newUser.getEmail().isEmpty() && this.getUserByEmail(newUser.getEmail()) == null){
            users.get(oldUser.getUserID()).setEmail(newUser.getEmail());
        }
        if(!oldUser.getPassword().equals(newUser.getPassword()) && !newUser.getPassword().isEmpty()){
            users.get(oldUser.getUserID()).setPassword(newUser.getPassword());
        }

    }
    public void deleteUser(long userID){
        users.remove(userID);
        if (users.containsKey(userID)) System.out.append("Пользователь не удалился \n");
        else System.out.append("Пользователь удален \n");
    }
    public boolean userIsCorrect(UserProfile testUser) {
        UserProfile realUser = this.getUserByLogin(testUser.getLogin());
        return (realUser != null && realUser.getPassword().equals(testUser.getPassword()));
    }
    public void printSession(){
        for(Map.Entry<String,UserProfile> entry: sessions.entrySet()) {
            System.out.append(" " + entry.getKey() + " " + entry.getValue().getLogin());
        }
    }

}

