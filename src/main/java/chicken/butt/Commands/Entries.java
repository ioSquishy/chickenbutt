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
import chicken.butt.Utility.Excel;
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
    public static SlashCommandBuilder createDeletedEntriesCmd() {
        return new SlashCommandBuilder()
            .setName("deletedentries")
            .setDescription("who lied.")
            .setDefaultEnabledForEveryone()
            .setEnabledInDms(false);
    }
    public static SlashCommandBuilder createRemoveEntryCmd() {
        return new SlashCommandBuilder()
            .setName("removeentry")
            .setDescription("unpee someone")
            .setDefaultDisabled()
            .setEnabledInDms(false)
            .addOption(SlashCommandOption.createLongOption("epochid", "Entry to delete.", true));
    }
    public static SlashCommandBuilder createRetrieveEntryCmd() {
        return new SlashCommandBuilder()
            .setName("retrieveentry")
            .setDescription("repee someone")
            .setDefaultEnabledForEveryone()
            .setEnabledInDms(false)
            .addOption(SlashCommandOption.createLongOption("epochid", "Entry to retrieve.", true));
    }

    public static void sendAllEntiresExcel(TextChannel channel) {
        new MessageBuilder().addAttachment(new Excel(Data.getAllData()).generateExcel(), "All Entries").send(channel).join();
    }

    public static void sendDeletedEntriesEmbed(TextChannel channel) {
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Deleted Entries")
            .setDescription("Will clear every week!!")
            .setColor(Color.YELLOW);

        embedData = new PaginateData(Data.getDeletedData());
        
        embed.setFooter("1/" + embedData.getPageAmount());

        HashMap<String, String> pageEntries = embedData.getPageEntries(0);
        embed.addField("Epoch ID", pageEntries.get("epochId"), true);
        embed.addField("Username", pageEntries.get("username"), true);
        embed.addField("Time", pageEntries.get("time"), true);
        
        new MessageBuilder()
            .setEmbed(embed)
            .addComponents(ActionRow.of(
                Button.create("left", ButtonStyle.PRIMARY, "◀"),
                Button.create("right", ButtonStyle.PRIMARY, "▶")
            )).send(channel).join();
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
        embed.addField("Time", pageEntries.get("time"), true);
        
        new MessageBuilder()
            .setEmbed(embed)
            .addComponents(ActionRow.of(
                Button.create("left", ButtonStyle.PRIMARY, "◀"),
                Button.create("right", ButtonStyle.PRIMARY, "▶")
            )).send(channel).join();
    }
}
