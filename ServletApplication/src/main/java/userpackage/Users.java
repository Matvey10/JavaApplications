package userpackage;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private static List<User> usersList = new ArrayList<User>();
    private static Users users;

    private Users() {
    }

    public static Users getUsers() {
        if (users == null) {
            users = new Users();
        }
        return users;
    }

    public static List<User> getListOfUsers() {
        return usersList;
    }

    public static void setUser(User user) {
        usersList.add(user);
    }
    public static User getUserByName(String name){
        User user = null;
        for(int i = 0; i < usersList.size(); i++){
            System.out.println(usersList.get(i).getUserName());
            if (usersList.get(i).getUserName().equals(name)){
                System.out.println("yes");
                user = usersList.get(i);
            }
        }
        return user;
    }
}