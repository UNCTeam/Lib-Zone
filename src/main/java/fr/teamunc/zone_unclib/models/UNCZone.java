package fr.teamunc.zone_unclib.models;

import com.google.errorprone.annotations.RestrictedApi;
import fr.teamunc.zone_unclib.ZoneLib;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder(toBuilder = true)
public class UNCZone {
    @Getter
    private Map<String, Object> additionalInformation;
    @Getter
    @Setter
    private String teamName;
    @Getter @NonNull
    private String name;
    @Getter
    private List<Location[]> coordinates;
    @Getter
    private UUID teamId;

    public static UNCZoneBuilder builder(String name) {
        return new UNCZoneBuilder().name(name).coordinates(new ArrayList<>()).additionalInformation(ZoneLib.getZoneParameters().getDefaultZoneAdditionalInformations());
    }

    public <T> T getAdditionalInformation(String key, Class<T> clazz) throws ClassCastException {
        return clazz.cast(this.additionalInformation.get(key));
    }

    public void setAdditionalInformation(String key, Object value) {
        this.additionalInformation.put(key, value);
    }

    @RestrictedApi(explanation = "This method is only used by the plugin itself", link = "ZoneController")
    public void addAnInterZone(Location corner1, Location corner2) {

        Location[] interZone = new Location[2];
        interZone[0] = corner1;
        interZone[1] = corner2;

        this.coordinates.add(interZone);
    }

    @RestrictedApi(explanation = "This method is only used by the plugin itself", link = "ZoneController")
    public void removeAnInterZone(int interZoneIndex) {
        this.coordinates.remove(interZoneIndex);
    }

    public String getZoneInfo() {
        StringBuilder sb = new StringBuilder();
        sb
            .append("\n")
            .append("Zone ")
            .append(this.name)
            .append(" (")
            .append(this.teamName == null ? "auncune team" : this.teamName)
            .append(")\n")
            .append("   InterZones : \n");

        for (int i = 0; i < this.coordinates.size(); i++) {
            Location[] interZone = this.coordinates.get(i);
            sb
                .append("       - ")
                .append(i)
                .append(" : ")
                .append(interZone[0].getBlockX())
                .append(", ")
                .append(interZone[0].getBlockY())
                .append(", ")
                .append(interZone[0].getBlockZ())
                .append(" -> ")
                .append(interZone[1].getBlockX())
                .append(", ")
                .append(interZone[1].getBlockY())
                .append(", ")
                .append(interZone[1].getBlockZ());

            sb.append("\n");
        }

        sb.append("   Additional information : \n");
        for (Map.Entry<String,Object> key : this.additionalInformation.entrySet()) {
            sb
                .append("       - ")
                .append(key.getKey())
                .append(" : ")
                .append(this.additionalInformation.get(key.getKey()));
        }
        sb.append("\n");

        return sb.toString();
    }

    public boolean isLocationInZone(Location location) {
        for (Location[] interZone : this.coordinates) {
            int xMin = Math.min(interZone[0].getBlockX(), interZone[1].getBlockX());
            int xMax = Math.max(interZone[0].getBlockX(), interZone[1].getBlockX());
            int yMin = Math.min(interZone[0].getBlockY(), interZone[1].getBlockY());
            int yMax = Math.max(interZone[0].getBlockY(), interZone[1].getBlockY());
            int zMin = Math.min(interZone[0].getBlockZ(), interZone[1].getBlockZ());
            int zMax = Math.max(interZone[0].getBlockZ(), interZone[1].getBlockZ());

            if (location.getBlockX() >= xMin && location.getBlockX() <= xMax
                && location.getBlockY() >= yMin && location.getBlockY() <= yMax
                && location.getBlockZ() >= zMin && location.getBlockZ() <= zMax) {
                return true;
            }
        }
        return false;
    }
}
