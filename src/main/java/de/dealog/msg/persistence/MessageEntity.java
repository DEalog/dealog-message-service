package de.dealog.msg.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
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

    /**
     * The geographic code delineating the affected area of the alert message
     */
    @ToString.Exclude
    private MultiPolygon<G2D> geocode;

    /**
     * The status of the message
     */
    protected MessageStatus status;

    /***
     * The date when the message is published
     */
    protected Date publishedAt;

    /**
     * Internal creation date
     */
    @CreationTimestamp
    protected Date createdAt;

    /**
     * Internal modification date
     */
    @UpdateTimestamp
    protected Date modifiedAt;
}
