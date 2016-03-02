package main;

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
    private UserProfile onlineUser;

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
    public UserProfile getOnlineUser(){
        return onlineUser;
    }
    public void setOnlineUser(@Nullable UserProfile user){
        onlineUser = user;
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

    public UserProfile getUser(long userID) {
        return users.get(userID);
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

    public boolean addSession( UserProfile user) {
        if (this.getUserByLogin(user.getLogin()).getPassword().equals(user.getPassword())){
            sessions.put(user.getLogin(),user);
            System.out.append("Session add: {").append(String.valueOf(user.getUserID())).append(", ")
                    .append(String.valueOf(user.getLogin())).append(", ")
                    .append(String.valueOf(String.valueOf(user.getPassword()))).append(", ")
                    .append(String.valueOf(String.valueOf(user.getEmail()))).append("}").append('\n');
            return true;
        }
        return false;
    }
    public void deleteSession(String login){
        sessions.remove(login);
        if(sessions.containsKey(login)) System.out.append("Не удалилось\n");
        else System.out.append("Сессия удалена\n");
    }
    public void editUser(UserProfile oldUser, UserProfile newUser) {
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

    /*public void printUser(){
        for(Map.Entry<Long,UserProfile> entry: users.entrySet()) {
            System.out.append("{ \"id\": " + entry.getValue().getUserID() + ",\n" + "\"login\": \"");
            System.out.append(entry.getValue().getLogin() + "\",\n" + "\"email\": \"" + entry.getValue().getEmail() + "\" }");
        }
    }*/

}