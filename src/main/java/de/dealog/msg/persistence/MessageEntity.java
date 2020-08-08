package de.dealog.msg.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class MessageEntity extends PanacheEntity implements Message {

    /**
     * The identifier of the message
     */
    @NotEmpty
    @Column(unique=true)
    private String identifier;

    /**
     * The headline of the message
     */
    @NotEmpty
    @Column(columnDefinition="TEXT")
    private String headline;

    /**
     * The description of the message
     */
    @NotEmpty
    @Column(columnDefinition="TEXT")
    private String description;

    @CreationTimestamp
    protected Date createdDate;

    @UpdateTimestamp
    protected Date lastModifiedDate;
}
