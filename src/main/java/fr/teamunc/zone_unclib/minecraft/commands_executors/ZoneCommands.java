package fr.teamunc.zone_unclib.minecraft.commands_executors;

import fr.teamunc.base_unclib.utils.helpers.Message;
import fr.teamunc.zone_unclib.ZoneLib;
import fr.teamunc.zone_unclib.models.UNCZone;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZoneCommands implements CommandExecutor {

    public static List<String> getCommands() {
        return new ArrayList<>(Arrays.asList("new","addSubZone","list","remove","assignToUNCTeam","unassignFromUNCTeam","setInformation"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!ZoneLib.isInit()) {
            Message.Get().sendMessage("ZoneLib is not initialized!", sender, true);
            return false;
        }

        if (args.length != 0) {
            switch (args[0]) {
                case "new": {
                    if (args.length < 2) {
                        Message.Get().sendMessage("usage : /uncz new <zoneName>", sender, true);
                    } else {
                        // get the zone name
                        String zoneName = args[1];

                        // add the zone
                        ZoneLib.getZoneController().addZone(zoneName);

                        Message.Get().sendMessage("Zone " + zoneName + " created!", sender, false);
                    }
                    return true;
                }
                case "addSubZone": {
                    if (args.length < 8) {
                        Message.Get().sendMessage("usage : /uncz addSubZone <zoneName> <X1> <Y1> <Z1> <X2> <Y2> <Z2>", sender, true);
                    } else {

                        // get the zone name
                        String zoneName = args[1];

                        // get the positions
                        Location corner1 = new Location(sender.getServer().getWorlds().get(0), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                        Location corner2 = new Location(sender.getServer().getWorlds().get(0), Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]));

                        // add the positions to the zone
                        ZoneLib.getZoneController().addInterZone(zoneName, corner1, corner2);

                        Message.Get().sendMessage("SubZone added to " + zoneName + " !", sender, false);
                    }
                    return true;
                }
                case "list": {
                    String informations;
                    if (args.length == 1) informations = ZoneLib.getZoneController().getZonesInformations();
                    else {
                        if (!ZoneLib.getZoneController().zoneExist(args[1])) {
                            Message.Get().sendMessage("Zone " + args[1] + " does not exist!", sender, true);
                            return true;
                        }
                        UNCZone zone = ZoneLib.getZoneController().getZone(args[1]);
                        informations = zone.getZoneInfo();
                    }

                    Message.Get().sendMessage(informations, sender, false);

                    return true;
                }
                case "remove": {
                    if (args.length < 2) {
                        Message.Get().sendMessage("usage : /uncz remove <zoneName>", sender, true);
                    } else {
                        // get the zone name
                        String zoneName = args[1];

                        // remove the zone
                        ZoneLib.getZoneController().removeZone(zoneName);

                        Message.Get().sendMessage("Zone " + zoneName + " removed!", sender, false);
                    }
                    return true;
                }
                case "assignToUNCTeam": {
                    if (args.length < 3) {
                        Message.Get().sendMessage("usage : /uncz assignToUNCTeam <zoneName> <teamName>", sender, true);
                    } else {
                        // get the zone name
                        String zoneName = args[1];

                        // get the team name
                        String teamName = args[2];

                        // assign the zone to the team
                        ZoneLib.getZoneController().assignZoneToTeam(zoneName, teamName);

                        Message.Get().sendMessage("Zone " + zoneName + " assigned to team " + teamName + "!", sender, false);
                    }
                    return true;
                }
                case "unassignFromUNCTeam": {
                    if (args.length < 2) {
                        Message.Get().sendMessage("usage : /uncz unassignFromUNCTeam <zoneName>", sender, true);
                    } else {
                        // get the zone name
                        String zoneName = args[1];

                        // unassign the zone from the team
                        ZoneLib.getZoneController().assignZoneToTeam(zoneName, null);

                        Message.Get().sendMessage("Zone " + zoneName + " unassigned from team!", sender, false);
                    }
                    return true;
                }
                case "setInformation": {
                    if (args.length < 4) {
                        Message.Get().sendMessage("usage : /uncz setInformation <zoneName> <information> <value>", sender, true);
                    } else {
                        // get the zone name
                        String zoneName = args[1];

                        // get the information
                        String information = args[2];

                        // get the value (can be bool, int, double, string)
                        Object value;
                        if (args[3].equals("true") || args[3].equals("false")) {
                            value = Boolean.parseBoolean(args[3]);
                        } else if (args[3].matches("-?\\d+(\\.\\d+)?")) {
                            if (args[3].contains(".")) {
                                value = Double.parseDouble(args[3]);
                            } else {
                                value = Integer.parseInt(args[3]);
                            }
                        } else {
                            value = args[3];
                        }
                        // set the information
                        ZoneLib.getZoneController().setAdditionalInformation(zoneName, information, value);

                        Message.Get().sendMessage("Information " + information + " set to " + value + " for zone " + zoneName + "!", sender, false);
                    }
                    return true;
                }
                default:
                    Message.Get().sendMessage("usage : /uncz <new|addSubZone|list|remove|assignToUNCTeam|unassignFromUNCTeam|setInformation>", sender, true);
                    return true;
            }
        } else {
            Message.Get().sendMessage("usage : /uncz <new|addSubZone|list|remove|assignToUNCTeam|unassignFromUNCTeam|setInformation>", sender, true);
            return true;
        }
    }
}
