package chicken.butt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

import chicken.butt.Commands.BRSign;
import chicken.butt.Utility.UserData;

import io.github.cdimascio.dotenv.Dotenv;

public class App implements Serializable {
    public static final DiscordApi api = new DiscordApiBuilder().setToken(Dotenv.load().get("TOKEN")).setAllNonPrivilegedIntentsAnd(Intent.MESSAGE_CONTENT, Intent.DIRECT_MESSAGES).login().join();
    // serialize stuff
    private static final long serialVersionUID = 0;
    private static ArrayList<UserData> ppl = new ArrayList<UserData>();

    private static transient ScheduledExecutorService autoSave = Executors.newSingleThreadScheduledExecutor();
    private static transient Runnable backup = () -> {
        try {
            saveData();
        } catch(IOException e) {
            api.getOwner().get().join().sendMessage("could save data :0").join();
        }
    };
    
    public static void main( String[] args )
    {
        System.out.println("logged in :O");

        try {
            retrieveData();
        } catch (ClassNotFoundException | IOException e) {
            api.getOwner().get().join().sendMessage("could not retrieve data :(").join();
        }

        long climbMaxing = 1069042405388583053L;
        new BRSign().createPeerexCmd().createForServer(api, climbMaxing).join();

        api.addMessageCreateListener(event -> {
            if (event.getMessage().getContent().toLowerCase().contains("what")) {
                try {
                    event.getMessage().reply("chicken butt").get();
                } catch (Error | InterruptedException | ExecutionException e) {
                    try {
                        event.getMessageAuthor().asUser().get().sendMessage("chicken butt").get();
                    } catch (Error | InterruptedException | ExecutionException ee) {}
                }
            }
        });

        api.addSlashCommandCreateListener(event -> {
            String cmd = event.getSlashCommandInteraction().getCommandName();

            switch (cmd) {
                case "peerex" :
                    BRSign.runCmd(event.getInteraction().getUser().getIdAsString());
                    break;
            }
        });


        
    }

    //serialize stuff
    private static void saveData() throws IOException {
        File file = new File("userData.ser");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(ppl);
        out.close();
        fos.close();
    }

    private static void retrieveData() throws ClassNotFoundException, IOException {
        FileInputStream fin = new FileInputStream("userData.ser");
        ObjectInputStream in = new ObjectInputStream(fin);
        ppl = (ArrayList) in.readObject();
        in.close();
        fin.close();

        System.out.println("data retrieved");
    }
}
