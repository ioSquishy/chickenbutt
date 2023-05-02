package chicken.butt.Utility;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import chicken.butt.App;

public class UserData implements Serializable {
    private static final long serialVersionUID = 1;

    private static transient String bathroomRole = "1102821443517030461";
    private String userID;
    private transient boolean signedOut;

    private ArrayList<ZonedDateTime> peeData = new ArrayList<ZonedDateTime>();

    public UserData(String userID) {
        this.userID = userID;
        this.signedOut = false;
    }

    public void signOut() {
        try {
            App.api.getUserById(userID).join().addRole(App.api.getRoleById(bathroomRole).get());
            peeData.add(ZonedDateTime.now(ZoneId.of("America/Los_Angeles")));
        } catch (Error e) {
            App.api.getOwner().get().join().sendMessage("I don't have permissions to change roles T-T").join();
        }
        signedOut = true;
    }

    public void signIn() {
        try {
            App.api.getUserById(userID).join().removeRole(App.api.getRoleById(bathroomRole).get());
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

    public List<ZonedDateTime> getPeeData() {
        return peeData;
    }
}
