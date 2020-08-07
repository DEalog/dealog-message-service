package de.dealog.msg.service;

import lombok.*;

import java.util.List;

@Builder
@Getter
public class PagedList<T> {

    /**
     * The requested page number
     */
    @lombok.NonNull
    private final int page;

    /**
     * The number of entries that should be contained
     */
    @lombok.NonNull
    private final int pageSize;

    /**
     * The results as a {@link List}.
     */
     private final List<? extends T> content;

    /**
     * The total number of entries
     */
    private final long count;

    /**
     * Returns the total number of pages to be read using the current page size.
     */
    private final int pageCount;
}
