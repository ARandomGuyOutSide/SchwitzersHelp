package com.schwitzer.schwitzersHelp.commands;

import com.schwitzer.schwitzersHelp.util.ChatUtil;
import com.schwitzer.schwitzersHelp.util.InventoryUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import com.schwitzer.schwitzersHelp.config.SchwitzerHelpConfig;

public class TestingCommand extends CommandBase {

    private final SchwitzerHelpConfig config = SchwitzerHelpConfig.getInstance();

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "test";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) return;

        String message = "The output of the test : ";

        message += InventoryUtil.isItemInInventory(args[0]);

        ChatUtil.formatedChatMessage(message);
    }
}
