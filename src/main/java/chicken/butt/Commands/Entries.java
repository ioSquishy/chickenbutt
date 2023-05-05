package chicken.butt.Commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandBuilder;

import chicken.butt.Utility.BRData;
import chicken.butt.Utility.Data;

public class Entries {
    public static SlashCommandBuilder createPeerexCmd() {
        return new SlashCommandBuilder()
            .setName("viewallentries")
            .setDescription("whos peed?")
            .setDefaultEnabledForEveryone()
			.setEnabledInDms(false);
    }

    public EmbedBuilder getAllDataEmbed() {
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("All Entries")
            .setColor(Color.YELLOW);

        ArrayList<String> epochIds = new ArrayList<String>();
        ArrayList<String> userNames = new ArrayList<String>();
        ArrayList<String> signOut = new ArrayList<String>();
        ArrayList<String> signIn = new ArrayList<String>();
        ArrayList<String> totalTime = new ArrayList<String>();
        for (Entry<Long, BRData> entry : Data.getAllData().entrySet()) {
            
        }
        return embed;
    }
}
