package dev.siea.inventorycap.commands;

import dev.siea.inventorycap.listeners.ItemPickupListeners;
import dev.siea.inventorycap.utils.ItemsConfig;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

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
        ItemStack item = player.getInventory().getItemInMainHand();
        Material material = item.getType();
        switch (subCommand){
            case "list":
                HashMap<Material, Integer> itemCaps = ItemsConfig.getItemCaps();
                for (Material mat : itemCaps.keySet()){
                    player.sendMessage("§e" + mat.name() + " - " + itemCaps.get(mat));
                }
                return true;
            case "add":
                if (material == Material.AIR){
                    player.sendMessage("§cYou need to be holding an Item");
                    return true;
                }
                if (args.length < 2){
                    return false;
                }
                ItemsConfig.addItemCap(material, Integer.parseInt(args[1]));
                ItemPickupListeners.reloadItemCaps();
                player.sendMessage("§eAdded Item-Cap for " + material.name() + " (" + args[1] + ")");
                return true;
            case "remove":
                if (material == Material.AIR){
                    player.sendMessage("§cYou need to be holding an Item");
                    return true;
                }
                ItemsConfig.removeItemCap(material);
                player.sendMessage("§eRemoved Item-Cap for " + material.name());
                ItemPickupListeners.reloadItemCaps();
                return true;
            case "help":
                String message =
                        "§eInvc list - displays a list of Item Caps\"" +
                        "§eInvc add <amount> - adds an Item-Cap for the Item you are holding\"" +
                        "§eInvc remove - removes the Item-Cap for the Item you are holding\"" +
                        "§eInvc help - Shows this list\"";
                player.sendMessage(message);
                return true;
        }
        return false;
    }
}
