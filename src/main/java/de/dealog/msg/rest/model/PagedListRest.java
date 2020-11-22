package de.dealog.msg.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@RegisterForReflection
public class PagedListRest<T> {
    /**
     * The results as a {@link List}.
     */
    private List<? extends T> content;

    /**
     * The requested page number
     */
    @JsonIgnore
    private int number;

    /**
     * The number of entries that should be contained
     */
    @JsonIgnore
    private int size;

    /**
     * The total amount of elements.
     */
    @JsonIgnore
    private long totalElements;

    /**
     * Returns the total number of pages to be read using the current page size.
     */
    @JsonIgnore
    private int totalPages;

    @JsonProperty("meta")
    private Map<String,Object> packNestedMeta() {
        return ImmutableMap.of(
                "size", this.size,
                "number", this.number,
                "totalElements", this.totalElements,
                "totalPages", this.totalPages);
    }
}
