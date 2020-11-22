package de.dealog.msg.rest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@Getter
@Setter
@NoArgsConstructor
public class PageRequest {

    /**
     *  The name of the HTTP page query parameter
     */
    public static final String PAGE = "page";

    /**
     *  The name of the HTTP size query parameter
     */
    public static final String SIZE = "size";

    /**
     * The maximal page size
     */
    private static final int MAX_SIZE = 50;

    @QueryParam(PAGE)
    @DefaultValue("0")
    private int page;

    @QueryParam(SIZE)
    @DefaultValue("10")
    private int size;

    /**
     * Return the requested page size.
     *
     * @return the page size or {@link #MAX_SIZE}
     */
    public int getSize() {
        return (size > MAX_SIZE) ? MAX_SIZE : size;
    }
}
