package util;

import cc.polyfrost.oneconfig.config.core.OneColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import org.polyfrost.schwitzersHelp.config.SchwitzerHelpConfig;

public class BlockUtil {

    private static SchwitzerHelpConfig config = SchwitzerHelpConfig.getInstance();

    // Neue Methode mit OneColor Support und Rainbow
    public static void drawFilledBox(AxisAlignedBB box, OneColor color, boolean rainbow) {
        float red, green, blue, alpha;

        if (rainbow) {
            // Rainbow-Farben basierend auf der aktuellen Zeit
            long time = System.currentTimeMillis();
            float hue = (time % 6000) / 6000.0f; // 6 Sekunden für einen kompletten Zyklus

            int rgbColor = java.awt.Color.HSBtoRGB(hue, 1f, 1f);
            red = ((rgbColor >> 16) & 0xFF) / 255.0f;
            green = ((rgbColor >> 8) & 0xFF) / 255.0f;
            blue = (rgbColor & 0xFF) / 255.0f;
            // Alpha von OneColor verwenden für Transparenz
            alpha = color.getAlpha() / 255.0f;
        } else {
            // Normale OneColor verwenden
            red = color.getRed() / 255.0f;
            green = color.getGreen() / 255.0f;
            blue = color.getBlue() / 255.0f;
            alpha = color.getAlpha() / 255.0f;
        }

        drawFilledBox(box, red, green, blue, alpha);
    }

    // Überladung ohne Rainbow (für Rückwärtskompatibilität)
    public static void drawFilledBox(AxisAlignedBB box, OneColor color) {
        drawFilledBox(box, color, false);
    }

    public static void drawFilledBox(AxisAlignedBB box, float red, float green, float blue, float alpha) {
        // Setup OpenGL states
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull(); // Wichtig für ESP!

        // Konfiguration prüfen - nur Outline oder voller Block?
        boolean onlyOutline = config.isBlockOutline();

        if (!onlyOutline) {
            // Voller Block rendern
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();

            GlStateManager.color(red, green, blue, alpha);

            // Alle Seiten in einem Draw-Call
            worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

            // Bottom face (Y-)
            worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
            worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();

            // Top face (Y+)
            worldrenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
            worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();

            // North face (Z-)
            worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
            worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();

            // South face (Z+)
            worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
            worldrenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
            worldrenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();

            // West face (X-)
            worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
            worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
            worldrenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
            worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();

            // East face (X+)
            worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
            worldrenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();

            tessellator.draw();
        }

        // Wireframe outline rendern (immer oder nur wenn onlyOutline true ist)
        if (!(red == 0.0f && green == 0.0f && blue == 0.0f)) {
            if (onlyOutline) {
                // Bei nur Outline: gleiche Farbe verwenden
                GlStateManager.color(red, green, blue, alpha);
            } else {
                // Bei vollem Block: Farbe etwas heller machen für den Rand
                float outlineRed = Math.min(1.0f, red + 0.2f);
                float outlineGreen = Math.min(1.0f, green + 0.2f);
                float outlineBlue = Math.min(1.0f, blue + 0.2f);
                GlStateManager.color(outlineRed, outlineGreen, outlineBlue, 1.0f);
            }

            RenderGlobal.drawSelectionBoundingBox(box);
        }

        // Restore OpenGL states
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}