package com.schwitzer.schwitzersHelp.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtil {
    private static GuiContainer guiContainer;
    private static Container container;
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void clickItemInInv(int indexOfButton)
    {
        if (mc.currentScreen instanceof GuiContainer && indexOfButton >= 0) {
            GuiContainer gui = (GuiContainer) mc.currentScreen;
            ChatUtil.debugMessage("Attempting to click slot: " + indexOfButton);

            mc.playerController.windowClick(
                    gui.inventorySlots.windowId,
                    indexOfButton,
                    0,
                    0,
                    mc.thePlayer
            );
            ChatUtil.debugMessage("Click executed!");
        }
    }

    public static int findItemInInventoryAndContainer(String itemName)
    {
        itemName = itemName.toLowerCase();
        guiContainer = (GuiContainer) mc.currentScreen;
        container = guiContainer.inventorySlots;

        int playerInventorySize = mc.thePlayer.inventory.getSizeInventory();
        int containerSize = container.inventorySlots.size() - playerInventorySize;
        int indexOfButton = -1;

        for (int i = 0; i < containerSize; i++) {
            ItemStack stack = container.getSlot(i).getStack();

            if (stack != null && stack.getItem() != null) {
                String displayName = stack.getDisplayName();
                String cleanDisplayName = ChatUtil.removeColorCodes(displayName);
                cleanDisplayName = cleanDisplayName.toLowerCase();

                if(cleanDisplayName.equals(itemName)) {
                    indexOfButton = i;
                    ChatUtil.debugMessage("Found " + itemName + " button");
                    break;
                }
            }
        }

        return indexOfButton;
    }

    public static boolean isItemInInventory(String itemName) {
        boolean isInInventory = false;
        itemName = itemName.toLowerCase();

        // Korrekte Methode für 1.8.9
        for (int i = 0; i < mc.thePlayer.inventory.mainInventory.length; i++) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[i];

            if (stack != null && stack.getItem() != null) {
                String displayName = stack.getDisplayName();
                String cleanDisplayName = ChatUtil.removeColorCodes(displayName);
                cleanDisplayName = cleanDisplayName.toLowerCase();

                if (cleanDisplayName.equals(itemName)) {
                    ChatUtil.debugMessage("Found " + itemName + " in inventory");
                    isInInventory = true;
                    break;
                }
            }
        }

        return isInInventory;
    }

    public static ItemStack findItemAtIndex(int index) {
        if (mc.thePlayer.openContainer != null
                && index >= 0
                && index < mc.thePlayer.openContainer.inventorySlots.size()) {

            Slot slot = (Slot) mc.thePlayer.openContainer.inventorySlots.get(index);

            if (slot != null && slot.getHasStack()) {
                return slot.getStack();
            }
        }
        return null;
    }


    public static void setHotbarSlotToItemName(String itemName)
    {
        for(int i = 0; i < InventoryPlayer.getHotbarSize(); i++)
        {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[i];

            if(stack != null && stack.getItem() != null)
            {
                String cleanDisplayName = ChatUtil.removeColorCodes(stack.getDisplayName()).toLowerCase();
                System.out.println(cleanDisplayName);

                if(cleanDisplayName.equals(itemName))
                {
                    System.out.println(cleanDisplayName + " " + itemName);
                    mc.thePlayer.inventory.currentItem = i;
                }
            }
        }
    }

    public static List<String> getItemLoreFromOpenContainer(String name) {
        Container openContainer = mc.thePlayer.openContainer;
        for (int i = 0; i < openContainer.inventorySlots.size(); i++) {
            Slot slot = openContainer.getSlot(i);
            if (slot == null || !slot.getHasStack()) {
                continue;
            }
            ItemStack stack = slot.getStack();
            if (!stack.hasDisplayName() || !StringUtils.stripControlCodes(stack.getDisplayName()).contains(name)) {
                continue;
            }
            return getItemLore(stack);
        }
        return new ArrayList<>();
    }

    public static List<String> getItemLore(ItemStack itemStack) {
        NBTTagList loreTag = itemStack.getTagCompound().getCompoundTag("display").getTagList("Lore", 8);
        ArrayList<String> loreList = new ArrayList<>();
        for (int i = 0; i < loreTag.tagCount(); i++) {
            loreList.add(StringUtils.stripControlCodes(loreTag.getStringTagAt(i)));
        }
        return loreList;
    }

    public static void useItem()
    {
        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
    }
}