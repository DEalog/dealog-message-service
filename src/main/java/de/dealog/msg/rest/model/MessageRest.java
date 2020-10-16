package de.dealog.msg.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.dealog.msg.persistence.model.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * RESTful representation of a {@link Message}
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageRest {

    /**
     * Unique identifier
     */
    private String identifier;

    /**
     * The headline
     */
    private String headline;

    /**
     * The description
     */
    private String description;

    /**
     * The createdDate
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date publishedAt;
}
