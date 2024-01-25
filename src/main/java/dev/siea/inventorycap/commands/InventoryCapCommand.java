package dev.siea.inventorycap.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryCapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly Players can use this command!");
            return true;
        }
        if (args.length < 1){
            return false;
        }
        String subCommand = args[0];
        Player player = (Player) sender;
        switch (subCommand){
            case "list":
                break;
            case "add":
                break;
            case "remove":
                break;
        }
        return false;
    }
}
