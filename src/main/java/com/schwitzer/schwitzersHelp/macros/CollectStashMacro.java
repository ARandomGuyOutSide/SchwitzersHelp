package com.schwitzer.schwitzersHelp.macros;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.schwitzer.schwitzersHelp.config.SchwitzerHelpConfig;
import com.schwitzer.schwitzersHelp.util.Chat;
import com.schwitzer.schwitzersHelp.util.Timer;

import java.util.LinkedList;
import java.util.List;

public class CollectStashMacro implements Macro {
    private SchwitzerHelpConfig config = SchwitzerHelpConfig.getInstance();

    private MacroController.MacroState macroControllerState = MacroController.MacroState.DISABLED;
    private MacroState currentState = MacroState.OPEN_STASH;
    private int tickcounter;
    private LinkedList<String> itemList = new LinkedList<>();
    private int indexOfButton;
    private List<String> betterFormOfItemList = new LinkedList<>();
    private int atItemIndex;
    private boolean itemsCollected = false; // Flag um zu verfolgen ob Items bereits gesammelt wurden

    private enum MacroState {
        OPEN_STASH, WAIT_FOR_CONTAINER_TO_OPEN, GET_ITEMS, CLOSE_MENU, FILL_INTO_SACKS, CLICK_SUPERCRAFT, OPEN_BAZZAR, OPEN_SUPER_CRAFT,
        SELECT_ITEM, DISABLED, FINAL_SUPERCRAFT_CLICK, CLICK_SUPERCRAFT_AGAIN, AFTER_SUPERCRAFT, CLOSE_SUPERCRAFT_MENU
    }

    // Hilfsmethode zum Entfernen von Farbcodes
    private String removeColorCodes(String text) {
        if (text == null) return null;
        // Entfernt alle Minecraft-Farbcodes (§ + ein Zeichen)
        return text.replaceAll("§[0-9a-fk-or]", "");
    }

    // Neue Hilfsmethode zum Verarbeiten von Ingot-Namen
    private String processItemName(String itemName) {
        if (itemName == null) return null;

        // Prüfen ob der Name mit "_ingot" endet
        if (itemName.endsWith("_ingot")) {
            // "_ingot" entfernen
            return itemName.substring(0, itemName.length() - 6); // 6 = Länge von "_ingot"
        }

        return itemName;
    }

    // Hilfsmethode für Shift+Click
    private void performShiftClick(int slotIndex, int mouseButton, int ShiftButton, String actionDescription) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiContainer && slotIndex >= 0) {
            GuiContainer gui = (GuiContainer) mc.currentScreen;
            Chat.debugMessage("Attempting to " + actionDescription + " slot: " + slotIndex);

            // Shift-Taste aktivieren
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);

            // Click ausführen
            mc.playerController.windowClick(
                    gui.inventorySlots.windowId,
                    slotIndex,
                    mouseButton,
                    ShiftButton, // Click Type (1 = shift click)
                    mc.thePlayer
            );

            // Shift-Taste deaktivieren
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);

            Chat.debugMessage(actionDescription + " executed!");
        } else {
            Chat.debugMessage("Cannot click: GUI not open or button index invalid (" + slotIndex + ")");
        }
    }

    @Override
    public void onEnable() {
        currentState = MacroState.OPEN_STASH;
        macroControllerState = MacroController.MacroState.ENABLED;
        tickcounter = 0;
        atItemIndex = 0;
        itemsCollected = false;
        // Listen leeren beim neuen Start
        itemList.clear();
        betterFormOfItemList.clear();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        currentState = MacroState.DISABLED;
        macroControllerState = MacroController.MacroState.DISABLED;
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public String getName() {
        return "Stash item collector";
    }

    @Override
    public MacroController.MacroState getState() {
        return macroControllerState;
    }

    @Override
    public void setState(MacroController.MacroState state) {
        this.macroControllerState = state;
        if (state == MacroController.MacroState.DISABLED) {
            onDisable();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player != Minecraft.getMinecraft().thePlayer) return;
        if (macroControllerState != MacroController.MacroState.ENABLED) return;
        if (currentState == MacroState.DISABLED) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (tickcounter > 0) {
            if (tickcounter % 20 == 0) { // Jede Sekunde ausgeben
                Chat.debugMessage("Noch " + (tickcounter / 20) + " Sekunden zu warten");
            }
            tickcounter--;
            return;
        }

        switch (currentState) {
            case OPEN_STASH:
                Chat.sendMessage("/viewstash material");
                tickcounter = Timer.secondsToTicks(1);
                currentState = MacroState.WAIT_FOR_CONTAINER_TO_OPEN;
                break;

            case WAIT_FOR_CONTAINER_TO_OPEN:
                if (mc.currentScreen instanceof GuiContainer) {
                    tickcounter = Timer.milisecondsToTicks(500);
                    currentState = MacroState.GET_ITEMS;
                }
                break;

            case GET_ITEMS:
                GuiContainer guiContainer = (GuiContainer) mc.currentScreen;
                Container container = guiContainer.inventorySlots;

                int playerInventorySize = mc.thePlayer.inventory.getSizeInventory();
                int containerSize = container.inventorySlots.size() - playerInventorySize;

                indexOfButton = -1; // reset each time

                // Nur Items sammeln wenn sie noch nicht gesammelt wurden
                if (!itemsCollected) {
                    Chat.debugMessage("Sammle Items zum ersten Mal...");
                    itemList.clear();
                    betterFormOfItemList.clear();

                    for (int i = 0; i < containerSize + 2; i++) {
                        ItemStack stack = container.getSlot(i).getStack();

                        if (stack != null && stack.getItem() != null) {
                            String registryName = stack.getItem().getRegistryName();
                            String displayName = stack.getDisplayName();
                            String cleanDisplayName = removeColorCodes(displayName);

                            if (registryName != null && (
                                    registryName.contains("stained_glass_pane") ||
                                            registryName.contains("barrier") ||
                                            registryName.equals("minecraft:barrier")
                            )) {
                                continue;
                            }

                            if (cleanDisplayName != null && (
                                    cleanDisplayName.equals("Close") ||
                                            cleanDisplayName.equals("Fill Inventory") ||
                                            cleanDisplayName.equals("Sell Stash Now")
                            )) {
                                continue;
                            }

                            if (cleanDisplayName != null && cleanDisplayName.contains("Insert Into Sacks")) {
                                indexOfButton = i;
                                Chat.debugMessage("Found 'Insert Into Sacks' at slot: " + i);
                                continue;
                            }

                            if (registryName != null && !registryName.equals(" ")) {
                                String[] data = cleanDisplayName.split(" x");
                                cleanDisplayName = data[0];
                                cleanDisplayName = cleanDisplayName.toLowerCase();
                                cleanDisplayName = cleanDisplayName.replace(" ", "_");

                                // Ingot-Verarbeitung anwenden
                                cleanDisplayName = processItemName(cleanDisplayName);

                                Chat.debugMessage(cleanDisplayName + " at spot: " + i);
                                itemList.add(cleanDisplayName);
                                if(config.getItemVariantsOptions() == 0) {
                                    betterFormOfItemList.add("enchanted_" + cleanDisplayName);
                                } else {
                                    betterFormOfItemList.add("enchanted_" + cleanDisplayName + "_block");
                                }
                                Chat.debugMessage("Item added : " + cleanDisplayName);
                            }
                        }
                    }
                    itemsCollected = true;
                    Chat.debugMessage("Insgesamt " + betterFormOfItemList.size() + " Items gefunden!");
                } else {
                    // Nur nach dem "Insert Into Sacks" Button suchen
                    for (int i = 0; i < containerSize + 2; i++) {
                        ItemStack stack = container.getSlot(i).getStack();
                        if (stack != null && stack.getItem() != null) {
                            String displayName = stack.getDisplayName();
                            String cleanDisplayName = removeColorCodes(displayName);
                            if (cleanDisplayName != null && cleanDisplayName.contains("Insert Into Sacks")) {
                                indexOfButton = i;
                                Chat.debugMessage("Found 'Insert Into Sacks' at slot: " + i);
                                break;
                            }
                        }
                    }
                }

                if (indexOfButton >= 0) {
                    Chat.debugMessage("Button found at index: " + indexOfButton);
                } else {
                    Chat.debugMessage("Insert Into Sacks button not found!");
                }

                tickcounter = Timer.secondsToTicks(1);
                currentState = MacroState.FILL_INTO_SACKS;
                break;

            case FILL_INTO_SACKS:
                if (mc.currentScreen instanceof GuiContainer && indexOfButton >= 0) {
                    GuiContainer gui = (GuiContainer) mc.currentScreen;
                    Chat.debugMessage("Attempting to click slot: " + indexOfButton);

                    mc.playerController.windowClick(
                            gui.inventorySlots.windowId,
                            indexOfButton,
                            0,
                            0,
                            mc.thePlayer
                    );
                    Chat.debugMessage("Click executed!");
                } else {
                    Chat.debugMessage("Cannot click: GUI not open or button index invalid (" + indexOfButton + ")");
                }

                tickcounter = Timer.secondsToTicks(1);
                currentState = MacroState.CLOSE_MENU;
                break;

            case CLOSE_MENU:
                mc.thePlayer.closeScreen();

                // Nach dem Schließen des Stash-Menüs: Beginne mit Supercraft für alle Items
                if (betterFormOfItemList.size() > 0) {
                    atItemIndex = 0; // Beginne mit dem ersten Item
                    currentState = MacroState.OPEN_SUPER_CRAFT;
                } else {
                    Chat.debugMessage("Keine Items zum Verarbeiten gefunden!");
                    currentState = MacroState.DISABLED;
                }

                tickcounter = Timer.secondsToTicks(1);
                break;

            case OPEN_SUPER_CRAFT:
                // Prüfen ob noch Items zu verarbeiten sind
                if (atItemIndex >= betterFormOfItemList.size()) {
                    Chat.debugMessage("Alle Items wurden verarbeitet! Starte neue Runde...");
                    // Zurück zum Anfang für die nächste Runde
                    atItemIndex = 0;
                    itemsCollected = false; // Items müssen neu gesammelt werden
                    currentState = MacroState.OPEN_STASH;
                    tickcounter = Timer.secondsToTicks(2);
                    return;
                }

                Chat.debugMessage("Verarbeite Item " + (atItemIndex + 1) + " von " + betterFormOfItemList.size() + ": " + betterFormOfItemList.get(atItemIndex));
                Chat.sendMessage("/recipe " + betterFormOfItemList.get(atItemIndex));
                tickcounter = Timer.secondsToTicks(1);
                currentState = MacroState.SELECT_ITEM;
                break;

            case SELECT_ITEM:
                guiContainer = (GuiContainer) mc.currentScreen;
                container = guiContainer.inventorySlots;

                playerInventorySize = mc.thePlayer.inventory.getSizeInventory();
                containerSize = container.inventorySlots.size() - playerInventorySize;
                indexOfButton = -1;

                for (int i = 0; i < containerSize; i++) {
                    ItemStack stack = container.getSlot(i).getStack();

                    if (stack != null && stack.getItem() != null) {
                        String displayName = stack.getDisplayName();
                        String cleanDisplayName = removeColorCodes(displayName);
                        cleanDisplayName = cleanDisplayName.toLowerCase();
                        cleanDisplayName = cleanDisplayName.replace(" ", "_");

                        if(cleanDisplayName.equals(betterFormOfItemList.get(atItemIndex))) {
                            indexOfButton = i;
                            Chat.debugMessage("Found Item: " + cleanDisplayName);
                            break;
                        }
                    }
                }

                if (indexOfButton >= 0) {
                    Chat.debugMessage("Item found at index: " + indexOfButton);

                    if (mc.currentScreen instanceof GuiContainer) {
                        GuiContainer gui = (GuiContainer) mc.currentScreen;
                        mc.playerController.windowClick(
                                gui.inventorySlots.windowId,
                                indexOfButton,
                                0,
                                0,
                                mc.thePlayer
                        );
                        Chat.debugMessage("Item selected!");
                    }
                } else {
                    Chat.debugMessage("Item not found: " + betterFormOfItemList.get(atItemIndex));
                }

                tickcounter = Timer.secondsToTicks(1);
                currentState = MacroState.CLICK_SUPERCRAFT;
                break;

            case CLICK_SUPERCRAFT:
                guiContainer = (GuiContainer) mc.currentScreen;
                container = guiContainer.inventorySlots;

                playerInventorySize = mc.thePlayer.inventory.getSizeInventory();
                containerSize = container.inventorySlots.size() - playerInventorySize;
                indexOfButton = -1;

                for (int i = 0; i < containerSize; i++) {
                    ItemStack stack = container.getSlot(i).getStack();

                    if (stack != null && stack.getItem() != null) {
                        String displayName = stack.getDisplayName();
                        String cleanDisplayName = removeColorCodes(displayName);
                        cleanDisplayName = cleanDisplayName.toLowerCase();

                        if(cleanDisplayName.equals("supercraft")) {
                            indexOfButton = i;
                            Chat.debugMessage("Found Supercraft button");
                            break;
                        }
                    }
                }

                if (indexOfButton >= 0) {
                    Chat.debugMessage("Supercraft button found at index: " + indexOfButton);
                    // Verwende die vereinfachte Methode für Shift+Rechtsklick
                    performShiftClick(indexOfButton, 1, 1, "Shift+Right-click");
                } else {
                    Chat.debugMessage("Supercraft button not found!");
                }

                Chat.debugMessage("Menü geschlossen nach Supercraft.");

                tickcounter = Timer.milisecondsToTicks(500);
                currentState = MacroState.CLICK_SUPERCRAFT_AGAIN;
                break;

            case CLICK_SUPERCRAFT_AGAIN:

                performShiftClick(indexOfButton, 0, 0, "Left-click");

                tickcounter = Timer.milisecondsToTicks(350);
                currentState = MacroState.AFTER_SUPERCRAFT;

                break;

            case AFTER_SUPERCRAFT:

                mc.thePlayer.closeScreen();

                atItemIndex++; // Nächstes Item

                // Prüfen ob noch Items zu verarbeiten sind
                if (atItemIndex < betterFormOfItemList.size()) {
                    Chat.debugMessage("Nächstes Item wird verarbeitet: " + betterFormOfItemList.get(atItemIndex));
                    currentState = MacroState.OPEN_SUPER_CRAFT;
                } else {
                    Chat.debugMessage("Alle Items dieser Runde wurden verarbeitet! Starte neue Runde...");
                    // Zurück zum Anfang für die nächste Runde
                    atItemIndex = 0;
                    itemsCollected = false; // Items müssen neu gesammelt werden
                    currentState = MacroState.OPEN_STASH;
                }

                tickcounter = Timer.secondsToTicks(1);
                break;
        }
    }
}