package com.schwitzer.schwitzersHelp.commands;

import com.schwitzer.schwitzersHelp.features.PlaceBlocksOnCommand;
import com.schwitzer.schwitzersHelp.util.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class PlaceBlockCommand extends CommandBase {

    // Backup für das ursprüngliche Item in Slot 9
    private static ItemStack slot9Backup = null;

    @Override
    public String getCommandName() {
        return "schwitza";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/schwitza place [<block>]";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // Jeder Spieler darf den Command nutzen
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1 || !"place".equalsIgnoreCase(args[0])) {
            sender.addChatMessage(new ChatComponentText("§cFalsche Nutzung! Nutze: /schwitza place [<block>]"));
            return;
        }

        EntityPlayer player = (EntityPlayer) sender;

        // Wenn nur "/schwitza place" eingegeben wird -> Toggle OFF
        if (args.length == 1) {
            PlaceBlocksOnCommand.setEnabled(false);

            // Ursprüngliches Item zurück in Slot 9 setzen (falls vorhanden)
            if (slot9Backup != null) {
                player.inventory.setInventorySlotContents(8, slot9Backup.copy());
            } else {
                // Slot 9 leeren falls kein Backup vorhanden
                player.inventory.setInventorySlotContents(8, null);
            }

            // Backup zurücksetzen
            slot9Backup = null;

            ChatUtil.formatedChatMessage("§cBlock-Platzierung deaktiviert!");
            return;
        }

        // Wenn "/schwitza place <block>" eingegeben wird -> Toggle ON + Item geben
        if (args.length == 2) {
            try {
                String blockName = args[1];
                Block block = Block.getBlockFromName(blockName);

                if (block == null) {
                    return;
                }

                // Item vom Block bekommen
                Item item = Item.getItemFromBlock(block);
                if (item == null) {
                    sender.addChatMessage(new ChatComponentText("§cKein Item für Block " + blockName + " gefunden!"));
                    return;
                }

                // Aktuelles Item in Slot 9 sichern (falls vorhanden)
                ItemStack currentSlot9Item = player.inventory.getStackInSlot(8);
                if (currentSlot9Item != null) {
                    slot9Backup = currentSlot9Item.copy();
                } else {
                    slot9Backup = null;
                }

                // ItemStack erstellen und in Slot 9 (Index 8) platzieren
                ItemStack itemStack = new ItemStack(item, 1);
                player.inventory.setInventorySlotContents(8, itemStack);

                ChatUtil.formatedChatMessage("§7Rechtsklick-Platzierung aktiviert!");

                PlaceBlocksOnCommand.setEnabled(true);
            } catch (Exception e) {
                sender.addChatMessage(new ChatComponentText("§cFehler beim Geben des Items!"));
                e.printStackTrace();
            }
            return;
        }

        // Falsche Anzahl von Argumenten
        sender.addChatMessage(new ChatComponentText("§cFalsche Nutzung! Nutze: /schwitza place [<block>]"));
    }
}