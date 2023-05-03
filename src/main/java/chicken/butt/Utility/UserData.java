package chicken.butt.Utility;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.NavigableMap;
import java.util.TreeMap;

import chicken.butt.App;

public class UserData implements Serializable {
    private static final long serialVersionUID = 2;

    private static transient String bathroomRole = "1102821443517030461";
    private String userID;
    private transient boolean signedOut;

    private TreeMap<Long, BRData> brData = new TreeMap<Long, BRData>();
    private transient BRData currentBRB;

    public TreeMap<Long, BRData> getUserData() {
        return brData;
    }

    public UserData(String userID) {
        this.userID = userID;
        this.signedOut = false;
    }

    public void signOut() {
        try {
            App.api.getUserById(userID).join().addRole(App.api.getRoleById(bathroomRole).get());

            currentBRB = new BRData();
            currentBRB.signOut();
            brData.put(currentBRB.getEpochID(), currentBRB);
        } catch (Error e) {
            App.api.getOwner().get().join().sendMessage("I don't have permissions to change roles T-T").join();
        }
        signedOut = true;
    }

    public void signIn() {
        try {
            App.api.getUserById(userID).join().removeRole(App.api.getRoleById(bathroomRole).get());

            currentBRB.signIn();
        } catch (Error e) {
            App.api.getOwner().get().join().sendMessage("I don't have permissions to change roles T-T").join();
        }
        signedOut = false;
    }

    public void switchSign() {
        if (signedOut) {
            signIn();
        } else {
            signOut();
        }
    }

    public void setBathroomRole(String roleID) {
        bathroomRole = roleID;
    }
}
