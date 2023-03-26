package fr.teamunc.zone_unclib;

import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import fr.teamunc.zone_unclib.controllers.ZoneController;
import fr.teamunc.zone_unclib.minecraft.commands_executors.ZoneCommands;
import fr.teamunc.zone_unclib.minecraft.commands_executors.ZoneTab;
import fr.teamunc.zone_unclib.minecraft.event_listeners.BlockListener;
import fr.teamunc.zone_unclib.models.UNCZoneContainer;
import lombok.Getter;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Behaviors of the Base_UNCLib :
 * - init with the plugin
 *
 */
public class ZoneLib {
    @Getter
    private static JavaPlugin plugin;
    @Getter
    private static ZoneController zoneController;
    @Getter
    private static Map<String, Object> zoneInformationInitialiser;

    private ZoneLib() {}

    public static void init(JavaPlugin plugin, Map<String, Object> zoneInformationInitialiser) {
        ZoneLib.plugin = plugin;

        //init zone informations
        if (zoneInformationInitialiser == null) {
            zoneInformationInitialiser = new HashMap<>();
        }
        if (!zoneInformationInitialiser.containsKey("OnlyTeamCanInteract"))
            zoneInformationInitialiser.put("OnlyTeamCanInteract", true);

        ZoneLib.zoneInformationInitialiser = zoneInformationInitialiser;

        // init json entities
        UNCEntitiesContainer.init(plugin.getDataFolder());

        // init controller
        zoneController = new ZoneController(initZoneContainer());

        // register commands
        initCommands();
    }

    public static boolean isInit() {
        return plugin != null;
    }

    private static UNCZoneContainer initZoneContainer() {
        UNCEntitiesContainer.init(plugin.getDataFolder());
        UNCZoneContainer res;

        try {
            res = UNCEntitiesContainer.loadContainer("zones", UNCZoneContainer.class);
        } catch (Exception e) {
            plugin.getLogger().info("Creating new zone container file");
            res = new UNCZoneContainer();
        }
        return res;
    }

    private static void initCommands() {
        PluginCommand blockCommand = plugin.getCommand("unczone");
        if (blockCommand != null) {
            blockCommand.setExecutor(new ZoneCommands());
            blockCommand.setTabCompleter(new ZoneTab());
        }
    }
    public static void initGameListeners(Zone_UNCLib customBlockUncLib) {
        customBlockUncLib.getServer().getPluginManager().registerEvents(new BlockListener(), customBlockUncLib);
    }
    public static void save() {
        zoneController.save("zones");
    }
}
