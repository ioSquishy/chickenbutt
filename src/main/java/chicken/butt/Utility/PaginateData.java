package chicken.butt.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class PaginateData {
    private static final int maxStringLength = 1024;
    private TreeMap<Integer, HashMap<String, String>> pages = new TreeMap<Integer, HashMap<String, String>>();
    private ArrayList<String> epochIds = new ArrayList<String>();
    private ArrayList<String> usernames = new ArrayList<String>();
    private ArrayList<String> signOuts = new ArrayList<String>();
    private ArrayList<String> signIns = new ArrayList<String>();
    private ArrayList<String> totalTimes = new ArrayList<String>();

    /**
     * <strong>Keys:</strong><p>
     * epochId<p>
     * username<p>
     * signOut<p>
     * signIn<p>
     * totalTime
     * @param index
     * @return
     */
    public HashMap<String, String> getPageEntries(int index) {
        return pages.get(index);
    }

    public int getPageAmount() {
        return pages.size();
    }

    /**
     * <strong>Keys:</strong><p>
     * epochId<p>
     * username<p>
     * signOut<p>
     * signIn<p>
     * totalTime
     * @param currentPageIndex
     * @return New Page, Entries
     */
    public HashMap<Integer, HashMap<String, String>> getRightPageEntries(int currentPageIndex) {
        HashMap<Integer, HashMap<String, String>> map = new HashMap<Integer, HashMap<String, String>>();
        if (currentPageIndex+1 > pages.size()) {
            //go back to first page if next page dne
            map.put(0, getPageEntries(0));
        } else {
            //otherwise get next page
            map.put(currentPageIndex+1, getPageEntries(currentPageIndex+1));
        }
        return map;
    }

    /**
     * <strong>Keys:</strong><p>
     * epochId<p>
     * username<p>
     * signOut<p>
     * signIn<p>
     * totalTime
     * @param currentPageIndex
     * @return New Page, Entries
     */
    public HashMap<Integer, HashMap<String, String>> getLeftPageEntries(int currentPageIndex) {
        HashMap<Integer, HashMap<String, String>> map = new HashMap<Integer, HashMap<String, String>>();
        if (currentPageIndex-1 >= 0) {
            //if page is still >= 0 then return new page
            map.put(currentPageIndex-1, getPageEntries(currentPageIndex-1));
        } else {
            //otherwise go to last page
            map.put(pages.size(), getPageEntries(pages.size()));
        }
        return map;
    }

    public PaginateData(TreeMap<Long, BRData> data) {
        for (Entry<Long, BRData> entry : Data.getAllData().entrySet()) {
            epochIds.add(entry.getKey() +"");
            usernames.add(Cache.getUsername(entry.getValue().getUserID()));
            signOuts.add(Data.formatTime(entry.getValue().getSignOutTime()));
            signIns.add(Data.formatTime(entry.getValue().getSignInTime()));
            totalTimes.add(entry.getValue().getBreakLength() +"");
        }
        
        int entiresPerPage = data.size()/calculatePages(data.size());
        int pageIndex = 0;
        HashMap<String, String> pageEntry = new HashMap<String, String>(5);
        for (int i = 0; i < data.size(); i++) {
            if (i % entiresPerPage == 0 && i > 0) {
                pageEntry.put("epochId", pageEntry.get("epochId") + "\n" + epochIds.get(i));
                pageEntry.put("username", pageEntry.get("username") + "\n" + usernames.get(i));
                pageEntry.put("signOut", pageEntry.get("signOut") + "\n" + signOuts.get(i));
                pageEntry.put("signIn", pageEntry.get("signIn") + "\n" + signIns.get(i));
                pageEntry.put("totalTime", pageEntry.get("totalTime") + "\n" + totalTimes.get(i));

                this.pages.put(pageIndex, pageEntry);
                pageEntry = new HashMap<String, String>(5);
                pageIndex++;
            } else {
                pageEntry.put("epochId", pageEntry.get("epochId") + "\n" + epochIds.get(i));
                pageEntry.put("username", pageEntry.get("username") + "\n" + usernames.get(i));
                pageEntry.put("signOut", pageEntry.get("signOut") + "\n" + signOuts.get(i));
                pageEntry.put("signIn", pageEntry.get("signIn") + "\n" + signIns.get(i));
                pageEntry.put("totalTime", pageEntry.get("totalTime") + "\n" + totalTimes.get(i));
            }
        }
        this.pages.put(pageIndex, pageEntry);
    }

    private int calculatePages(int dataSize) {
        int pages = 1;
        int epochLength = 0;
        int usernameLength = 0;
        int signOutLength = 0;
        int signInLength = 0;
        int totalTimeLength = 0;

        for (int i = 0; i < dataSize; i++) {
            epochLength += epochIds.get(i).length();
            usernameLength += usernames.get(i).length();
            signOutLength += signOuts.get(i).length();
            signInLength += signIns.get(i).length();
            totalTimeLength += totalTimes.get(i).length();

            boolean newPage = false;
            if (epochLength > maxStringLength) {
                newPage = true;
            } else if (usernameLength > maxStringLength) {
                newPage = true;
            } else if (signOutLength > maxStringLength) {
                newPage = true;
            } else if (signInLength > maxStringLength) {
                newPage = true;
            } else if (totalTimeLength > maxStringLength) {
                newPage = true;
            }

            if (newPage) {
                System.out.println("new page :O");
                pages++;
                epochLength = epochIds.get(i).length();
                usernameLength = usernames.get(i).length();
                signOutLength = signOuts.get(i).length();
                signInLength = signIns.get(i).length();
                totalTimeLength = totalTimes.get(i).length();
            }
        }
        return pages;
    }
}
