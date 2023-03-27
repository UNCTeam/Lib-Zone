package fr.teamunc.zone_unclib.controllers;

import fr.teamunc.base_unclib.utils.helpers.Message;
import fr.teamunc.ekip_unclib.EkipLib;
import fr.teamunc.ekip_unclib.controllers.UNCTeamController;
import fr.teamunc.ekip_unclib.models.UNCTeam;
import fr.teamunc.zone_unclib.models.UNCZone;
import fr.teamunc.zone_unclib.models.UNCZoneContainer;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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

    public ArrayList<UNCZone> isLocationInAZone(Location location) {
        val res = new ArrayList<UNCZone>();
        for (UNCZone zone : zoneContainer.getZones()) {
            if (zone.isLocationInZone(location)) {
                res.add(zone);
            }
        }
        return res;
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
        UNCZone zone = zoneContainer.getZones().stream().filter(z -> z.getName().equals(name)).findFirst().orElse(null);
        if (zone == null) {
            throw new IllegalArgumentException("La zone n'existe pas.");
        }
        return zone;
    }
    public void addInterZone(String zoneName, Location corner1, Location corner2) {
        getZone(zoneName).addAnInterZone(corner1, corner2);
    }

    public void removeInterZone(String zoneName, int interZoneIndex) {
        getZone(zoneName).removeAnInterZone(interZoneIndex);
    }

    public boolean removeInterZone(String zoneName, Location locationInZone) {
        UNCZone zone = getZone(zoneName);

        for (int i = 0; i < zone.getCoordinates().size(); i++) {
            Location[] corners = zone.getCoordinates().get(i);

            /// if the location is between the two corners
            // min max
            int minX = Math.min(corners[0].getBlockX(), corners[1].getBlockX());
            int maxX = Math.max(corners[0].getBlockX(), corners[1].getBlockX());
            int minY = Math.min(corners[0].getBlockY(), corners[1].getBlockY());
            int maxY = Math.max(corners[0].getBlockY(), corners[1].getBlockY());
            int minZ = Math.min(corners[0].getBlockZ(), corners[1].getBlockZ());
            int maxZ = Math.max(corners[0].getBlockZ(), corners[1].getBlockZ());

            if (locationInZone.getBlockX() >= minX && locationInZone.getBlockX() <= maxX
                    && locationInZone.getBlockY() >= minY && locationInZone.getBlockY() <= maxY
                    && locationInZone.getBlockZ() >= minZ && locationInZone.getBlockZ() <= maxZ) {
                zone.removeAnInterZone(i);
                return true;
            }
        }
        return false;
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
        getZone(zoneName).setTeamName(teamName);
    }

    public void setAdditionalInformation(String zoneName, String information, Object value) {
        getZone(zoneName).setAdditionalInformation(information, value);
    }

    public Object getAdditionalInformation(String zoneName, String information) {
        return getZone(zoneName).getAdditionalInformation(information, Object.class);
    }

    public String getZoneInformations(Location location) {
        ArrayList<UNCZone> zones = isLocationInAZone(location);
        if (zones.isEmpty())
            return "Vous n'êtes pas dans une zone.";

        StringBuilder sb = new StringBuilder();
        for (UNCZone zone : zones) {
            sb
                .append(zone.getZoneInfo())
                .append("\n");
        }
        return sb.toString();
    }
}
