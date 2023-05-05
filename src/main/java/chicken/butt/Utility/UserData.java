package chicken.butt.Utility;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.Map.Entry;

import chicken.butt.App;

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

    public Entry<Long, BRData> removeLastEntry() {
        Entry<Long, BRData> lastEntry = brData.lastEntry();
        if (lastEntry != null) {
            brData.remove(lastEntry.getKey());
        }
        return lastEntry;
    }

    public UserData(long userID) {
        this.chickenButts = 0;
        this.userID = userID;
        this.signedOut = false;
    }

    public long getUserID() {
        return userID;
    }

    public void addButt() {
        this.chickenButts++;
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

    public BRData switchSign() {
        if (signedOut) {
            signIn();
        } else {
            signOut();
        }
        return currentBRB;
    }

    public void setBathroomRole(String roleID) {
        bathroomRole = roleID;
    }
}
