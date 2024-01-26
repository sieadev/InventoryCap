package dev.siea.inventorycap;

import dev.siea.inventorycap.commands.InventoryCapCommand;
import dev.siea.inventorycap.listeners.ItemPickupListeners;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class InventoryCap extends JavaPlugin {

    private static Plugin plugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        Objects.requireNonNull(getCommand("inventorycap")).setExecutor(new InventoryCapCommand());
        getServer().getPluginManager().registerEvents(new ItemPickupListeners(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getPlugin(){
        return plugin;
    }
}
