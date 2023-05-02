package chicken.butt.Utility;

import chicken.butt.App;

public class UserData {
    private static String bathroomRole = "1102821443517030461";
    private String userID;
    private boolean signedOut;

    public UserData(String userID) {
        this.userID = userID;
        this.signedOut = false;
    }

    public void signOut() {
        try {
            App.api.getUserById(userID).join().addRole(App.api.getRoleById(bathroomRole).get());
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
}
