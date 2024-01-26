package dev.siea.inventorycap.listeners;

import dev.siea.inventorycap.InventoryCap;
import dev.siea.inventorycap.utils.ItemsConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.*;

public class ItemPickupListeners implements Listener{
    private static HashMap<Material, Integer> itemCaps = ItemsConfig.getItemCaps();
    private static final boolean includeShulkers = InventoryCap.getPlugin().getConfig().getBoolean("includeShulkers");
    @EventHandler
    public static void onPlayerPickUpItem(EntityPickupItemEvent e){
        if (!(e.getEntity()instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (!canPickUp(player, e.getItem().getItemStack(), player.getItemOnCursor())){
            e.setCancelled(true);
            String message = "§cYou reached the limit for §e" + e.getItem().getItemStack().getType().name().replace("_", " ") + "§c !";
            if (e.getItem().getItemStack().getType().name().toLowerCase().contains("shulker")){
                message = "§cYou reached the limit for at least one of this Shulker-Boxes items!";
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e){
        if (!(e.getWhoClicked() instanceof Player)) return;
        String action = e.getAction().toString();


        ItemStack item = e.getCurrentItem();
        if (action.toLowerCase().contains("pick") && e.getClickedInventory() == e.getWhoClicked().getInventory()) {
            return;
        }

        if (( (action.toLowerCase().contains("place")) && e.getClickedInventory() != e.getWhoClicked().getInventory())) {
            return;
        }

        if (action.toLowerCase().contains("move_to_other") && e.getClickedInventory() == e.getWhoClicked().getInventory()) {
            return;
        }

        if (action.toLowerCase().contains("drop")) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        if (item == null) return;
        if (!canPickUp(player, item, player.getItemOnCursor())){
            e.setCancelled(true);
            String message = "§cYou reached the limit for §e" + item.getType().name().replace("_", " ") + "§c !";
            if (item.getType().name().toLowerCase().contains("shulker")){
                message = "§cYou reached the limit for at least one of this Shulker-Boxes items!";
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }
    }

    public static void reloadItemCaps(){
        itemCaps = ItemsConfig.getItemCaps();
    }
    private static boolean canPickUp(Player player, ItemStack itemStack, ItemStack additionalItem){
        int currentAmount = getItemAmount(player,itemStack.getType());
        Material material = itemStack.getType();
        int newlyAdded = itemStack.getAmount();

        if (includeShulkers){
            if ((itemStack.getType().toString().contains(Material.SHULKER_BOX.toString())) && (itemStack.getAmount() > 0)) {
                ItemStack[] shulkerContents = Objects.requireNonNull(getShulkerBoxContents(itemStack)).getContents();
                newlyAdded = 0;
                for (ItemStack shulkerItem : shulkerContents) {
                    if (shulkerItem == null) continue;
                    if (!canPickUp(player, shulkerItem, player.getItemOnCursor())) return false;
                }
            }
        }

        if (additionalItem.getType() == itemStack.getType()) currentAmount = currentAmount+additionalItem.getAmount();

        int cap = itemCaps.getOrDefault(material, -1);
        if (cap == -1) return true;
        return cap >= currentAmount + newlyAdded;
    }

    private static int getItemAmount(Player player, Material material){
        List<ItemStack> items = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));
        items.add(player.getItemOnCursor());
        int has = 0;
        for (ItemStack item : items)
        {
            if ((item != null) && (item.getType() == material) && (item.getAmount() > 0))
            {
                has += item.getAmount();
            }
            if (includeShulkers){
                if ((item != null) && (item.getType().toString().contains(Material.SHULKER_BOX.toString())) && (item.getAmount() > 0)) {
                    ItemStack[] shulkerContents = Objects.requireNonNull(getShulkerBoxContents(item)).getContents();
                    for (ItemStack shulkerItem : shulkerContents) {
                        if ((shulkerItem != null) && (shulkerItem.getType() == material) && (shulkerItem.getAmount() > 0))
                        {
                            has += shulkerItem.getAmount();
                        }
                    }
                }
            }
        }
        return has;
    }

    private static Inventory getShulkerBoxContents(ItemStack shulkerBoxItem) {
        BlockStateMeta blockStateMeta = (BlockStateMeta) shulkerBoxItem.getItemMeta();
        if (blockStateMeta == null || !(blockStateMeta.getBlockState() instanceof ShulkerBox)) {
            return null;
        }
        ShulkerBox shulkerBox = (ShulkerBox) blockStateMeta.getBlockState();
        return shulkerBox.getInventory();
    }
}
