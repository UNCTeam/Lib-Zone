package fr.teamunc.zone_unclib.controllers;

import fr.teamunc.base_unclib.utils.helpers.Message;
import fr.teamunc.ekip_unclib.EkipLib;
import fr.teamunc.ekip_unclib.controllers.UNCTeamController;
import fr.teamunc.ekip_unclib.models.UNCTeam;
import fr.teamunc.zone_unclib.models.UNCZone;
import fr.teamunc.zone_unclib.models.UNCZoneContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 *
 */
public class ZoneController {

    private UNCZoneContainer zoneContainer;

    public ZoneController(UNCZoneContainer zoneContainer) {
        this.zoneContainer = zoneContainer;
        Message.Get().broadcastMessageToConsole("[ZoneController] : Loading " + getZones().size() + " zones data");
    }

    public List<UNCZone> getZones() {
        return zoneContainer.getZones();
    }

    public UNCZone isLocationInAZone(Location location) {
        for (UNCZone zone : zoneContainer.getZones()) {
            if (zone.isLocationInZone(location)) {
                return zone;
            }
        }
        return null;
    }

    public boolean canInteract(Player player, UNCZone zone) {
        UNCTeamController teamController = EkipLib.getTeamController();

        if (zone == null)
            return true;

        if (zone.getAdditionalInformation("OnlyTeamCanInteract", Boolean.class).equals(false))
            return true;

        if (zone.getTeamName() == null)
            return false;

        UNCTeam team = teamController.getTeam(zone.getTeamName());

        if (team == null)
            throw new IllegalArgumentException("La team n'existe pas.");


        return team.getPlayers().contains(player.getUniqueId());
    }

    public void addZone(String name) {
        UNCZone zone = UNCZone.builder(name).build();
        zoneContainer.getZones().add(zone);
    }

    public void removeZone(String name) {
        zoneContainer.getZones().removeIf(zone -> zone.getName().equals(name));
    }

    public UNCZone getZone(String name) {
        return zoneContainer.getZones().stream().filter(zone -> zone.getName().equals(name)).findFirst().orElse(null);
    }
    public void addInterZone(String zoneName, Location corner1, Location corner2) {
        UNCZone zone = zoneContainer.getZones().stream().filter(z -> z.getName().equals(zoneName)).findFirst().orElse(null);
        if (zone == null) {
            throw new IllegalArgumentException("La zone n'existe pas.");
        }

        zone.addAnInterZone(corner1, corner2);
    }

    public void removeInterZone(String zoneName, int interZoneIndex) {
        UNCZone zone = zoneContainer.getZones().stream().filter(z -> z.getName().equals(zoneName)).findFirst().orElse(null);
        if (zone == null) {
            throw new IllegalArgumentException("La zone n'existe pas.");
        }

        zone.removeAnInterZone(interZoneIndex);
    }

    public String getZonesInformations() {
        StringBuilder sb = new StringBuilder();
        for (UNCZone zone : zoneContainer.getZones()) {
            sb
                .append(zone.getZoneInfo())
                .append("\n");
        }
        return sb.toString();
    }

    public void save(String fileName) {
        zoneContainer.save(fileName);
    }

    public boolean zoneExist(String arg) {
        return zoneContainer.getZones().stream().anyMatch(zone -> zone.getName().equals(arg));
    }

    public void assignZoneToTeam(String zoneName, String teamName) {
        UNCZone zone = zoneContainer.getZones().stream().filter(z -> z.getName().equals(zoneName)).findFirst().orElse(null);
        if (zone == null) {
            throw new IllegalArgumentException("La zone n'existe pas.");
        }

        zone.setTeamName(teamName);
    }

    public void setAdditionalInformation(String zoneName, String information, Object value) {
        UNCZone zone = zoneContainer.getZones().stream().filter(z -> z.getName().equals(zoneName)).findFirst().orElse(null);
        if (zone == null) {
            throw new IllegalArgumentException("La zone n'existe pas.");
        }

        zone.setAdditionalInformation(information, value);
    }

    public Object getAdditionalInformation(String zoneName, String information) {
        UNCZone zone = zoneContainer.getZones().stream().filter(z -> z.getName().equals(zoneName)).findFirst().orElse(null);
        if (zone == null) {
            throw new IllegalArgumentException("La zone n'existe pas.");
        }

        return zone.getAdditionalInformation(information, Object.class);
    }
}
