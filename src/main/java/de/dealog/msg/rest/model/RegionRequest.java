package de.dealog.msg.rest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.ws.rs.QueryParam;

@Getter
@Setter
@NoArgsConstructor
public class RegionRequest {

    /**
     *  The name of the HTTP ars query parameter
     */
    public static final String QUERY_ARS = "ars";

    @QueryParam(QUERY_ARS)
    private String ars;
}


