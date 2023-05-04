package chicken.butt.Commands;

import java.awt.Color;
import java.util.List;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.EmbedField;
import org.javacord.api.interaction.SlashCommandBuilder;

import chicken.butt.App;

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
            .addInlineField("╭⋅ User ⋅╮", "a")
            .addInlineField("╭⋅ Sign Out ⋅╮", "b")
            .addInlineField("╭⋅ Sign In⋅╮", "c")
            .setColor(Color.YELLOW);

        msgLink = new MessageBuilder()
            .setEmbed(embed)
            .addActionRow(Button.create("peerex", ButtonStyle.PRIMARY, "Sign In/Out"))
            .send(channel).join().getLink().toString();
    }

    public static void updateEmbed(String userName, String timeOut, String timeIn) {
        Message msg = App.api.getMessageByLink(msgLink).get().join();
        List<EmbedField> fields = msg.getEmbeds().get(0).getFields();

        System.out.println(fields.get(0).getValue());
    }

    public static void signOut(long userID, String timeOut) {
        Message msg = App.api.getMessageByLink(msgLink).get().join();
        List<EmbedField> fields = msg.getEmbeds().get(0).getFields();

        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Bathroom Sign In/Out Sheet")
            .addInlineField("╭⋅ User ⋅╮", fields.get(0).getValue() + "\n" + App.api.getUserById(userID).join().getName())
            .addInlineField("╭⋅ Sign Out ⋅╮", fields.get(1).getValue() + "\n" + timeOut)
            .addInlineField("╭⋅ Sign In⋅╮", fields.get(2).getValue() + "\n" + hourGlass)
            .setColor(Color.YELLOW);
        
        msg.edit(embed).join();
    }

    public static void signIn(long userID, String timeIn) {
        Message msg = App.api.getMessageByLink(msgLink).get().join();
        List<EmbedField> fields = msg.getEmbeds().get(0).getFields();


    }
}
