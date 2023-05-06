package chicken.butt.Commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;

import chicken.butt.Utility.Data;
import chicken.butt.Utility.PaginateData;

public class Entries {
    private static PaginateData embedData;

    public static SlashCommandBuilder createAllEntriesCmd() {
        return new SlashCommandBuilder()
            .setName("allentries")
            .setDescription("whos peed?")
            .setDefaultEnabledForEveryone()
            .addOption(SlashCommandOption.createBooleanOption("asExcel", "If true, returns an excel of the data instead of an embed.", false))
			.setEnabledInDms(false);
    }

    public static void sendAllEntriesEmbed(TextChannel channel) {
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("All Entries")
            .setColor(Color.YELLOW);

        embedData = new PaginateData(Data.getAllData());
        
        embed.setFooter("1/" + embedData.getPageAmount());

        HashMap<String, String> pageEntries = embedData.getPageEntries(0);
        embed.addField("Epoch ID", pageEntries.get("epochId"), true);
        embed.addField("Username", pageEntries.get("username"), true);
        embed.addField("Sign Out", pageEntries.get("signOut"), true);
        embed.addField("Sign In", pageEntries.get("signIn"), true);
        embed.addField("Total Time", pageEntries.get("totalTime"), true);
        
        new MessageBuilder()
            .setEmbed(embed)
            .addComponents(ActionRow.of(
                Button.create("left", ButtonStyle.PRIMARY, "◀"),
                Button.create("right", ButtonStyle.PRIMARY, "▶")
            )).send(channel).join();
    }
}
