package com.schwitzer.schwitzersHelp.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InventorySlots {

    private static final Logger LOGGER = LogManager.getLogger("InventoryLogger");
    private int tickCounter = 0;
    private final int TICK_INTERVAL = 40; // 40 ticks = 2 Sekunden (20 TPS)

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("Inventory Logger Mod geladen!");
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        tickCounter++;
        if (tickCounter >= TICK_INTERVAL) {
            tickCounter = 0;
            logInventories();
        }
    }

    private void logInventories() {
        Minecraft mc = Minecraft.getMinecraft();

        LOGGER.info("=== INVENTAR LOG ===");

        // Spieler Inventar loggen
        logPlayerInventory();

        // Container loggen (falls geöffnet)
        if (mc.currentScreen instanceof GuiContainer) {
            GuiContainer guiContainer = (GuiContainer) mc.currentScreen;
            Container container = guiContainer.inventorySlots;
            logContainer(container);
        }

        LOGGER.info("===================");
    }

    private void logPlayerInventory() {
        Minecraft mc = Minecraft.getMinecraft();
        IInventory playerInv = mc.thePlayer.inventory;

        LOGGER.info("--- SPIELER INVENTAR ---");

        // Hauptinventar (Slots 0-35)
        LOGGER.info("Hauptinventar:");
        for (int i = 0; i < 36; i++) {
            ItemStack stack = playerInv.getStackInSlot(i);
            if (stack != null) {
                LOGGER.info("  Slot {}: {} x{}", i, stack.getDisplayName(), stack.stackSize);
            }
        }

        // Rüstung (Slots 36-39)
        LOGGER.info("Rüstung:");
        for (int i = 36; i < 40; i++) {
            ItemStack stack = playerInv.getStackInSlot(i);
            if (stack != null) {
                String armorType = getArmorSlotName(i - 36);
                LOGGER.info("  {}: {} x{}", armorType, stack.getDisplayName(), stack.stackSize);
            }
        }
    }

    private void logContainer(Container container) {
        if (container == null) return;

        LOGGER.info("--- CONTAINER ---");
        LOGGER.info("Container Typ: {}", container.getClass().getSimpleName());

        // Alle Slots im Container durchgehen
        for (int i = 0; i < container.inventorySlots.size(); i++) {
            ItemStack stack = container.getSlot(i).getStack();
            if (stack != null) {
                LOGGER.info("  Container Slot {}: {} x{}", i, stack.getDisplayName(), stack.stackSize);
            }
        }
    }

    private String getArmorSlotName(int armorSlot) {
        switch (armorSlot) {
            case 0: return "Helm";
            case 1: return "Brustpanzer";
            case 2: return "Beinschutz";
            case 3: return "Stiefel";
            default: return "Unbekannt";
        }
    }
}