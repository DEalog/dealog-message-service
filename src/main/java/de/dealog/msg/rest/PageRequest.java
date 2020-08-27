package de.dealog.msg.rest;

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
    static final String PAGE = "page";

    /**
     *  The name of the HTTP size query parameter
     */
    static final String SIZE = "size";

    @QueryParam(PAGE)
    @DefaultValue("0")
    private int page;

    @QueryParam(SIZE)
    @DefaultValue("10")
    private int size;
}
