package com.schwitzer.schwitzersHelp.commands;

import com.schwitzer.schwitzersHelp.discord.DiscordNotifications;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import com.schwitzer.schwitzersHelp.config.SchwitzerHelpConfig;

import java.io.IOException;

public class DiscordCommand extends CommandBase {

    private final SchwitzerHelpConfig config = SchwitzerHelpConfig.getInstance();

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "discord";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) return;

        String message = "";

        for (String arg : args) {
            message += arg + " ";
        }

        try {
            DiscordNotifications.sendMessageToWebhook(message, config.getDiscordWebhook());
            DiscordNotifications.sendEmbedToWebhook("Test", message, 65280, config.getDiscordWebhook());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
