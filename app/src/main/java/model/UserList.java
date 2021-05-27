package model;

import java.util.ArrayList;

public class UserList {
    public static ArrayList<User> userList = new ArrayList<>();

    public static User getUser(String email, String password) {
        for (int i = 0; i < userList.size(); i++) {
            User tmpUser = userList.get(i);
            if (email.equalsIgnoreCase(tmpUser.getEmail()) && password.equals(tmpUser.getPassword())) {
                return tmpUser;
            }
        }
        return null;
    }

    public static void addUserToUserList(User tmpUser) {
        userList.add(tmpUser);
    }

    public static void deleteUser(String username) {
        for (int i = 0; i < userList.size(); i++) {
            User tmpUser = userList.get(i);
            if (tmpUser.getUsername().equalsIgnoreCase(username)) {
                userList.remove(i);
                break;
            }
        }
    }
}
