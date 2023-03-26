package fr.teamunc.zone_unclib;

import org.bukkit.plugin.java.JavaPlugin;

public final class Zone_UNCLib extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        ZoneLib.initGameListeners(this);
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (ZoneLib.isInit()) {
            ZoneLib.save();
        }
    }
}
