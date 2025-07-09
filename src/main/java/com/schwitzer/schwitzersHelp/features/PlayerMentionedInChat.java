package com.schwitzer.schwitzersHelp.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.schwitzer.schwitzersHelp.config.SchwitzerHelpConfig;
import com.schwitzer.schwitzersHelp.util.Timer;

import java.awt.*;

public class PlayerMentionedInChat {
    private final SchwitzerHelpConfig config = SchwitzerHelpConfig.getInstance();
    private Minecraft mc = Minecraft.getMinecraft();
    private static String title = "";
    private static int timer = 0;
    private static float scale = 2.0f;
    private static Color color = Color.WHITE;

    public static void showTitle(String text, int durationTicks, float scaleValue, Color textColor) {
        title = text;
        timer = durationTicks;
        scale = scaleValue;
        color = textColor;
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player != Minecraft.getMinecraft().thePlayer) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (timer > 0) {
            timer--;
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (timer > 0 && !title.isEmpty()) {
            ScaledResolution res = new ScaledResolution(mc);
            FontRenderer fr = mc.fontRendererObj;

            GlStateManager.pushMatrix();
            GlStateManager.translate(res.getScaledWidth() / 2f, res.getScaledHeight() / 4f, 0);
            GlStateManager.scale(scale, scale, scale);

            int x = -fr.getStringWidth(title) / 2;
            fr.drawStringWithShadow(title, x, 0, color.getRGB());

            GlStateManager.popMatrix();
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event)
    {
        if(!config.isChatmention()) return;

        String username = mc.thePlayer.getName();
        String message = event.message.getUnformattedText();

        // Check if @username is in the message
        if (message.contains("@" + username)) {
            // @username rot einfärben
            String formattedMessage = message.replace(
                    "@" + username,
                    EnumChatFormatting.RED + "@" + username + EnumChatFormatting.RESET
            );

            // Ursprüngliche Nachricht blockieren
            event.setCanceled(true);

            // Neue, formatierte Nachricht in Chat einfügen
            mc.thePlayer.addChatMessage(new ChatComponentText(formattedMessage));

            // Titel anzeigen mit der korrekten Client-Methode
            showTitle("You have been mentioned in chat", Timer.secondsToTicks(config.getTitelDuration()), 2.5f, Color.RED);
        }
    }
}