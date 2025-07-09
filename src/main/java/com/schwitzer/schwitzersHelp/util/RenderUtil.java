package com.schwitzer.schwitzersHelp.util;

import cc.polyfrost.oneconfig.config.core.OneColor;
import net.minecraft.block.BlockBed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import com.schwitzer.schwitzersHelp.config.SchwitzerHelpConfig;

import static net.minecraft.client.gui.Gui.drawRect;

public class RenderUtil {

    private static SchwitzerHelpConfig config = SchwitzerHelpConfig.getInstance();

    private static float[] getRenderColor(OneColor baseColor) {
        if (config.isRainbow()) {
            long time = System.currentTimeMillis();
            float hue = (time % 6000) / 6000.0f;
            int rgb = java.awt.Color.HSBtoRGB(hue, 1f, 1f);
            return new float[]{
                    ((rgb >> 16) & 0xFF) / 255.0f,
                    ((rgb >> 8) & 0xFF) / 255.0f,
                    (rgb & 0xFF) / 255.0f,
                    baseColor.getAlpha() / 255.0f
            };
        }
        return new float[]{
                baseColor.getRed() / 255.0f,
                baseColor.getGreen() / 255.0f,
                baseColor.getBlue() / 255.0f,
                baseColor.getAlpha() / 255.0f
        };
    }

    public static void drawNameTag(String name, double x, double y, double z) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRendererObj;
        RenderManager renderManager = mc.getRenderManager();

        float scale = 0.02F;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.5, z);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);

        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();

        int width = fontRenderer.getStringWidth(name) / 2;
        drawRect(-width - 2, -2, width + 2, 10, 0x80000000);
        fontRenderer.drawStringWithShadow(name, -width, 0, 0xFFFFFF);

        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    public static void drawBedBox(BlockPos pos, OneColor bed_ESP_color) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        float[] color = getRenderColor(bed_ESP_color);
        GlStateManager.color(color[0], color[1], color[2], color[3]);

        BlockBed bedBlock = (BlockBed) Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        BlockBed.EnumPartType partType = Minecraft.getMinecraft().theWorld.getBlockState(pos).getValue(BlockBed.PART);
        boolean isFoot = partType == BlockBed.EnumPartType.FOOT;

        if (isFoot) {
            EnumFacing facing = Minecraft.getMinecraft().theWorld.getBlockState(pos).getValue(BlockBed.FACING);

            double width = 1.0D;
            double height = 1.0D;
            double length = 2.0D;

            double x1 = x;
            double z1 = z;
            double x2 = x;
            double z2 = z;

            switch (facing) {
                case NORTH:
                    x1 = x;
                    z1 = z - 1;
                    x2 = x + width;
                    z2 = z + length - 1;
                    break;
                case SOUTH:
                    x1 = x;
                    z1 = z;
                    x2 = x + width;
                    z2 = z + length;
                    break;
                case WEST:
                    x1 = x - 1;
                    z1 = z;
                    x2 = x + length - 1;
                    z2 = z + width;
                    break;
                case EAST:
                    x1 = x;
                    z1 = z;
                    x2 = x + length;
                    z2 = z + width;
                    break;
            }

            GL11.glBegin(GL11.GL_LINES);

            GL11.glVertex3d(x1, y, z1);
            GL11.glVertex3d(x2, y, z1);
            GL11.glVertex3d(x2, y, z1);
            GL11.glVertex3d(x2, y, z2);
            GL11.glVertex3d(x2, y, z2);
            GL11.glVertex3d(x1, y, z2);
            GL11.glVertex3d(x1, y, z2);
            GL11.glVertex3d(x1, y, z1);

            GL11.glVertex3d(x1, y + height, z1);
            GL11.glVertex3d(x2, y + height, z1);
            GL11.glVertex3d(x2, y + height, z1);
            GL11.glVertex3d(x2, y + height, z2);
            GL11.glVertex3d(x2, y + height, z2);
            GL11.glVertex3d(x1, y + height, z2);
            GL11.glVertex3d(x1, y + height, z2);
            GL11.glVertex3d(x1, y + height, z1);

            GL11.glVertex3d(x1, y, z1);
            GL11.glVertex3d(x1, y + height, z1);
            GL11.glVertex3d(x2, y, z1);
            GL11.glVertex3d(x2, y + height, z1);
            GL11.glVertex3d(x2, y, z2);
            GL11.glVertex3d(x2, y + height, z2);
            GL11.glVertex3d(x1, y, z2);
            GL11.glVertex3d(x1, y + height, z2);

            GL11.glEnd();
        }
    }

    public static void drawEntityBox(double x, double y, double z, double width, double height, OneColor color, double yOffset) {
        float[] col = getRenderColor(color);
        GlStateManager.color(col[0], col[1], col[2], col[3]);

        GL11.glBegin(GL11.GL_LINES);

        // Bottom
        GL11.glVertex3d(x - width, y + yOffset, z - width);
        GL11.glVertex3d(x + width, y + yOffset, z - width);
        GL11.glVertex3d(x + width, y + yOffset, z - width);
        GL11.glVertex3d(x + width, y + yOffset, z + width);
        GL11.glVertex3d(x + width, y + yOffset, z + width);
        GL11.glVertex3d(x - width, y + yOffset, z + width);
        GL11.glVertex3d(x - width, y + yOffset, z + width);
        GL11.glVertex3d(x - width, y + yOffset, z - width);

        // Top
        GL11.glVertex3d(x - width, y + height + yOffset, z - width);
        GL11.glVertex3d(x + width, y + height + yOffset, z - width);
        GL11.glVertex3d(x + width, y + height + yOffset, z - width);
        GL11.glVertex3d(x + width, y + height + yOffset, z + width);
        GL11.glVertex3d(x + width, y + height + yOffset, z + width);
        GL11.glVertex3d(x - width, y + height + yOffset, z + width);
        GL11.glVertex3d(x - width, y + height + yOffset, z + width);
        GL11.glVertex3d(x - width, y + height + yOffset, z - width);

        // Verticals
        GL11.glVertex3d(x - width, y + yOffset, z - width);
        GL11.glVertex3d(x - width, y + height + yOffset, z - width);
        GL11.glVertex3d(x + width, y + yOffset, z - width);
        GL11.glVertex3d(x + width, y + height + yOffset, z - width);
        GL11.glVertex3d(x + width, y + yOffset, z + width);
        GL11.glVertex3d(x + width, y + height + yOffset, z + width);
        GL11.glVertex3d(x - width, y + yOffset, z + width);
        GL11.glVertex3d(x - width, y + height + yOffset, z + width);

        GL11.glEnd();
    }

    public static void drawFilledBox(AxisAlignedBB box, OneColor color) {
        float[] col = getRenderColor(color);
        drawFilledBox(box, col[0], col[1], col[2], col[3]);
    }

    public static void drawFilledBox(AxisAlignedBB box, float red, float green, float blue, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();

        boolean onlyOutline = config.isBlockOutline();

        if (!onlyOutline) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();

            GlStateManager.color(red, green, blue, alpha);
            worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

            worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
            worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();

            worldrenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
            worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();

            worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
            worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();

            worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
            worldrenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
            worldrenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();

            worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
            worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
            worldrenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
            worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();

            worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
            worldrenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
            worldrenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();

            tessellator.draw();
        }

        if (!(red == 0.0f && green == 0.0f && blue == 0.0f)) {
            if (onlyOutline) {
                GlStateManager.color(red, green, blue, alpha);
            } else {
                float outlineRed = Math.min(1.0f, red + 0.2f);
                float outlineGreen = Math.min(1.0f, green + 0.2f);
                float outlineBlue = Math.min(1.0f, blue + 0.2f);
                GlStateManager.color(outlineRed, outlineGreen, outlineBlue, 1.0f);
            }

            RenderGlobal.drawSelectionBoundingBox(box);
        }

        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
