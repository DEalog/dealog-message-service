package de.dealog.msg.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PageRequest {

    /**
     *  The name of the HTTP page query parameter
     */
    public static final String PAGE = "page";

    /**
     *  The name of the HTTP size query parameter
     */
    public static final String SIZE = "size";

    @QueryParam(PAGE)
    @DefaultValue("0")
    private int page;

    @QueryParam(SIZE)
    @DefaultValue("10")
    private int size;
}
