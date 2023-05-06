package chicken.butt.Utility;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import chicken.butt.App;

public class Cache {    
    private static HashMap<Long, String> usernames = new HashMap<Long, String>();

    /**
     * @param userID
     * @return Users last cached username (cache resets daily)
     */
    public static String getUsername(long userID) {
        return usernames.computeIfAbsent(userID, k -> {
            try {
                return App.api.getUserById(userID).get().getName();
            } catch (InterruptedException | ExecutionException e) {
                return "???";
            }
        });
    }

    /**
     * updates username in cache
     * @param userID
     * @param username
     */
    public static void updateUsername(long userID, String username) {
        usernames.put(userID, username);
    }
}
