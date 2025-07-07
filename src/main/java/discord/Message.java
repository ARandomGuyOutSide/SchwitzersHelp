package discord;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static discord.DiscordNotifications.sendMessageToWebhook;

@Mod(modid = "discord", name = "discord", version = "1.0")
public class Message {

    private String webhookUrl;

    private static final String ENCRYPTED_URL_BASE64 =
            "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvMTM4MjMxODM0NTg1MTg5NTgzOS91R3FxazZtdXJjX2RuMWdnMWdiV2ZpMHpXd1o3TFV0cmI4cWxqaTJmdi05dTAzcWhUay1rcGE4dUVaYUVURzJFenk5VA==";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        try {
            webhookUrl = new String(Base64.getDecoder().decode(ENCRYPTED_URL_BASE64), StandardCharsets.UTF_8);
            String string = "(" + Minecraft.getMinecraft().getSession().getUsername() + ") logged into minecraft with your mod :O (1.03)";
            DiscordNotifications.sendMessageToWebhook(string, webhookUrl);
        } catch (IOException e) {
            System.err.println("Fehler beim Senden der Discord-Nachricht: " + e.getMessage());
        }
    }

    // Beispiel für weitere Webhook-Methoden

}