package de.dealog.msg.rest.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@RegisterForReflection
public enum RegionTypeRest {

    COUNTRY("Bundesrepublik"),
    STATE("Land", "Freie Hansestadt", "Freie und Hansestadt", "Freistaat"),
    COUNTY("Regierungsbezirk"),
    DISTRICT("Kreis", "Kreisfreie Stadt", "Landkreis", "Stadtkreis"),
    MUNICIPALITY("Gemeinde", "Gemeindefreies Gebiet", "Stadt"),
    UNKNOWN;

    private final List<String> identifiers;

    RegionTypeRest(final String... identifiers) {
        this.identifiers = Arrays.asList(identifiers);
    }

    /**
     * Find a region type by an identifer.
     *
     * @param identifier the identifier as string
     * @return an {@link Optional} containing the found {@link RegionTypeRest}
     */
    public static Optional<RegionTypeRest> findByIdentifier(final String identifier) {
        return Arrays.stream(RegionTypeRest.values())
        .filter(rt -> rt.getIdentifiers().contains(identifier))
        .findFirst();
    }
}
