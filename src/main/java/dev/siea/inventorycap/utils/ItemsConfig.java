package dev.siea.inventorycap.utils;

import dev.siea.inventorycap.InventoryCap;
import org.bukkit.Material;
import java.util.HashMap;
import java.util.Map;

public class ItemsConfig {
    private static final ConfigUtil config = new ConfigUtil(InventoryCap.getPlugin(), "Items.yml");

    public static HashMap<Material, Integer> getItemCaps() {
        Map<String, Object> itemCapsMap = config.getConfig().getValues(false);
        HashMap<Material, Integer> itemCaps = new HashMap<>();
        for (Map.Entry<String, Object> entry : itemCapsMap.entrySet()) {
            try {
                Material material = Material.valueOf(entry.getKey());
                int cap = (int) entry.getValue();
                itemCaps.put(material, cap);
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        return itemCaps;
    }

    public static void addItemCap(Material material, int amount){
        config.getConfig().set(String.valueOf(material), amount);
        config.save();
    }

    public static void removeItemCap(Material material){
        config.getConfig().set(String.valueOf(material), null);
        config.save();
    }
}
