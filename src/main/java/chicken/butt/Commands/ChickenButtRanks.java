package chicken.butt.Commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandBuilder;

import chicken.butt.Utility.Data;

public class ChickenButtRanks {
    public static SlashCommandBuilder createCmd() {
        return new SlashCommandBuilder()
            .setName("chickenbutts")
            .setDescription("Who's winning?")
            .setDefaultEnabledForEveryone()
            .setEnabledInDms(true);
    }

    public static EmbedBuilder createEmbed() {
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Chicken Butt Leaderboard :D");
        
        
        Data.getChickenRanks().forEach((username, chickenbutts) -> {
            embed.addField(username, ""+chickenbutts, false);
        });

        return embed;
    }
}
