package chicken.butt.Utility;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.TreeMap;

import chicken.butt.App;
import chicken.butt.Commands.BRSheet;

public class UserData implements Serializable {
    private static final long serialVersionUID = 1;

    private static transient String bathroomRole = "1102821443517030461";
    private long userID;
    private transient boolean signedOut;

    private TreeMap<Long, BRData> brData = new TreeMap<Long, BRData>();
    private int chickenButts;
    private transient BRData currentBRB;

    public TreeMap<Long, BRData> getBRData() {
        return brData;
    }

    public UserData(long userID) {
        this.chickenButts = 0;
        this.userID = userID;
        this.signedOut = false;
    }

    public void addButt() {
        this.chickenButts+=1;
    }

    public int getChickenButts() {
        return this.chickenButts;
    }

    public void signOut() {
        System.out.println("signed out"); //
        try {
            App.api.getUserById(userID).join().addRole(App.api.getRoleById(bathroomRole).get());

            currentBRB = new BRData(userID);
            currentBRB.signOut();
            brData.put(currentBRB.getEpochID(), currentBRB);
            BRSheet.signOut(userID, currentBRB.getSignOutTime().format(DateTimeFormatter.ofPattern("h:mm a")));
        } catch (Error e) {
            App.api.getOwner().get().join().sendMessage("I don't have permissions to change roles T-T").join();
        }
        signedOut = true;
    }

    public void signIn() {
        System.out.println("signed in"); //
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
