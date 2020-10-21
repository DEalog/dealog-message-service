package de.dealog.msg.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionRest {

    /**
     * The regional code "Amtlicher Regionalschlüssel"
     */
    private String ars;

    /*
     * The name of the region
     */
    private String name;

    /**
     * The {@link RegionTypeRest} of the region
     */
    private RegionTypeRest type;
}
