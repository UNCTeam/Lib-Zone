package fr.teamunc.zone_unclib.models;

import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import lombok.Getter;

import java.util.ArrayList;

public class UNCZoneContainer extends UNCEntitiesContainer {
    @Getter
    private ArrayList<UNCZone> zones = new ArrayList<>();
}
