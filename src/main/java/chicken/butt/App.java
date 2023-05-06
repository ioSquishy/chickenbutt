package chicken.butt;

import java.util.concurrent.ExecutionException;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;

import chicken.butt.Commands.BRSheet;
import chicken.butt.Commands.BRSign;
import chicken.butt.Commands.ChickenButtRanks;
import chicken.butt.Commands.Entries;
import chicken.butt.Utility.Cache;
import chicken.butt.Utility.Data;
import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static final DiscordApi api = new DiscordApiBuilder().setToken(Dotenv.load().get("TOKEN")).setAllNonPrivilegedIntentsAnd(Intent.MESSAGE_CONTENT, Intent.DIRECT_MESSAGES, Intent.GUILD_PRESENCES).login().join();
    
    public static void main( String[] args )
    {
        System.out.println("logged in :O");
        //init data
        new Data();

        // slash commands
        long climbMaxing = 1069042405388583053L;
        BRSign.createPeerexCmd().createForServer(api, climbMaxing).join();
        BRSheet.createCmd().createForServer(api, climbMaxing).join();
        ChickenButtRanks.createCmd().createForServer(api, climbMaxing).join();
        Entries.createAllEntriesCmd().createForServer(api, climbMaxing).join();
        Entries.createRemoveEntryCmd().createForServer(api, climbMaxing).join();
        Entries.createRetrieveEntryCmd().createForServer(api, climbMaxing).join();
        Entries.createDeletedEntriesCmd().createForServer(api, climbMaxing).join();
        
        /*try {
            api.getServerSlashCommands(api.getServerById(climbMaxing).get()).get().forEach(cmd -> {
                System.out.println(cmd.getName() + ": " + cmd.getId());
            });
        } catch (InterruptedException | ExecutionException e) {} */
        //api.getServerSlashCommandById(api.getServerById(climbMaxing).get(), 1104244545216057416L).join().delete().join();

        // listeners
        api.addMessageCreateListener(event -> {
            String msg = event.getMessage().getContent().toLowerCase();

            if (msg.contains("what") || msg.contains("wut")) {
                try {
                    if (msg.contains("what")) {
                        event.getMessage().reply("chicken butt").get();
                    } else {
                        event.getMessage().reply("chicken but").get();
                    }
                    Data.addChickenButt(event.getMessageAuthor().getId());
                } catch (Error | InterruptedException | ExecutionException e) {
                    try {
                        if (msg.contains("what")) {
                            event.getMessageAuthor().asUser().get().sendMessage("chicken butt").get();
                        } else {
                            event.getMessageAuthor().asUser().get().sendMessage("chicken but").get();
                        }
                    } catch (Error | InterruptedException | ExecutionException ee) {}
                }
            }
            
            switch (msg) {
                case "/chickenbutts" :
                    event.getMessage().reply(ChickenButtRanks.createEmbed()).join();
                    break;
                case "/peerex" :
                    Data.brbUpdate(event.getMessageAuthor().getId());
                    break;
                case "/downloaddata" :
                    break;
            }
            
        });

        api.addSlashCommandCreateListener(event -> {
            String cmd = event.getSlashCommandInteraction().getCommandName();
            switch (cmd) {
                case "peerex" :
                    Data.brbUpdate(event.getInteraction().getUser().getId());
                    event.getInteraction().createImmediateResponder().setContent("gogogogo").setFlags(MessageFlag.EPHEMERAL).respond().join();
                    break;
                case "createsheet" :
                    BRSheet.createEmbed(event.getInteraction().getChannel().get());
                    event.getInteraction().createImmediateResponder().setContent(":D").setFlags(MessageFlag.EPHEMERAL).respond().join();
                    break;
                case "chickenbutts" :
                    event.getInteraction().createImmediateResponder().addEmbed(ChickenButtRanks.createEmbed()).respond().join();
                    break;
                case "allentries" :
                    event.getInteraction().createImmediateResponder().setContent("Compiling data. . .").setFlags(MessageFlag.EPHEMERAL).respond().join();
                    Entries.sendAllEntriesEmbed(event.getInteraction().getChannel().get());
                    break;
                case "deletedentries" :
                    event.getInteraction().createImmediateResponder().setContent("Compiling data. . .").setFlags(MessageFlag.EPHEMERAL).respond().join();
                    Entries.sendDeletedEntriesEmbed(event.getInteraction().getChannel().get());
                    break;
                case "removeentry" :
                    long entryToRemove = event.getSlashCommandInteraction().getArgumentLongValueByIndex(0).get();
                    boolean removedEntry = Data.removeEntry(entryToRemove);
                    if (removedEntry) {
                        event.getInteraction().createImmediateResponder().setContent("Removed entry: " + entryToRemove).respond().join();
                    } else {
                        event.getInteraction().createImmediateResponder().setContent("Could not find/remove entry: " + entryToRemove).respond().join();
                    }
                    break;
                case "retrieveentry" :
                    long entryToRetrieve = event.getSlashCommandInteraction().getArgumentLongValueByIndex(0).get();
                    boolean retrievedEntry = Data.retrieveEntry(entryToRetrieve);
                    if (retrievedEntry) {
                        event.getInteraction().createImmediateResponder().setContent("Retrieved entry: " + entryToRetrieve).respond().join();
                    } else {
                        event.getInteraction().createImmediateResponder().setContent("Could not find/retrieve entry: " + entryToRetrieve).respond().join();
                    }
                    break;
            }
        });

        api.addButtonClickListener(event -> {
            String id = event.getButtonInteraction().getCustomId();
            switch (id) {
                case "peerex" :
                    Data.brbUpdate(event.getInteraction().getUser().getId());
                    event.getButtonInteraction().acknowledge().join();
                    break;
                case "undo" :
                    Data.removeLastBRB(event.getInteraction().getUser().getId());
                    event.getButtonInteraction().acknowledge().join();
                    break;
                case "left" :
                    event.getButtonInteraction().acknowledge().join();
                    break;
                case "right" :
                    event.getButtonInteraction().acknowledge().join();
                    break;
            }
        });

        api.addUserChangeNameListener(event -> {
            Cache.updateUsername(event.getUser().getId(), event.getUser().getName());
        });
    }
}
