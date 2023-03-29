package fr.teamunc.zone_unclib.models;

import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class UNCZoneParameters extends UNCEntitiesContainer {
    @Getter @Setter
    private Map<String, Object> defaultZoneAdditionalInformations;

    public UNCZoneParameters() {
        super();

        this.defaultZoneAdditionalInformations = new HashMap<>();

        this.defaultZoneAdditionalInformations.put("OnlyTeamCanInteract", true);
        this.defaultZoneAdditionalInformations.put("Priority", 0);
        this.defaultZoneAdditionalInformations.put("OnlyTeamCanPvP", true);
    }
}
