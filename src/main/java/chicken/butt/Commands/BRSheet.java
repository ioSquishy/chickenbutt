package chicken.butt.Commands;

import java.awt.Color;
import java.time.ZonedDateTime;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandBuilder;

import chicken.butt.App;
import chicken.butt.Utility.BRData;
import chicken.butt.Utility.Cache;
import chicken.butt.Utility.Data;

import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;

public class BRSheet {
    private static String msgLink;
    private static String hourGlass = "⌛";

    public static SlashCommandBuilder createCmd() {
        return new SlashCommandBuilder()
            .setName("createsheet")
            .setDescription("Creates embeded sign in/out sheet")
            .setDefaultDisabled()
            .setEnabledInDms(false);
    }

    public static void createEmbed(TextChannel channel) {
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Bathroom Sign In/Out Sheet")
            .addInlineField("╭⋅ User ⋅╮", "")
            .addInlineField("╭⋅ Sign Out ⋅╮", "")
            .addInlineField("╭⋅ Sign In⋅╮", "")
            .setColor(Color.YELLOW);

        msgLink = new MessageBuilder()
            .setEmbed(embed)
            .addActionRow(
                Button.create("peerex", ButtonStyle.PRIMARY, "Sign In/Out"),
                Button.create("undo", ButtonStyle.DANGER, "Remove Your Last Entry"))
            .send(channel).join().getLink().toString();
    }

    public static void updateEmbed() {
        Message msg = App.api.getMessageByLink(msgLink).get().join();

        String user = "";
        String signOut = "";
        String signIn = "";

        for (BRData brData : Data.getTodaysData().values()) {
            user += Cache.getUsername(brData.getUserID()) + "\n";
            signOut += Data.formatTime(brData.getSignOutTime()) + "\n";

            ZonedDateTime signInTime = brData.getSignInTime();
            if (signInTime != null) {
                signIn += Data.formatTime(signInTime) + "\n";
            } else {
                signIn += hourGlass + "\n";
            };
        }

        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Bathroom Sign In/Out Sheet")
            .addInlineField("╭⋅ User ⋅╮", user)
            .addInlineField("╭⋅ Sign Out ⋅╮", signOut)
            .addInlineField("╭⋅ Sign In⋅╮", signIn)
            .setColor(Color.YELLOW);

        msg.edit(embed).join();
    }
}
