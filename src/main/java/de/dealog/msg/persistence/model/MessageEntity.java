package de.dealog.msg.persistence.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class MessageEntity extends PanacheEntity implements Message {

    @NotEmpty
    @Column(unique=true)
    private String identifier;

    @NotEmpty
    @Column(columnDefinition="TEXT")
    private String headline;

    @NotEmpty
    @Column(columnDefinition="TEXT")
    private String description;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "geocode_id")
    private GeocodeEntity geocode;

    @Column(columnDefinition="TEXT")
    private String regionCode;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private Date publishedAt;

    /**
     * Internal creation date
     */
    @CreationTimestamp
    private Date createdAt;

    /**
     * Internal modification date
     */
    @UpdateTimestamp
    private Date modifiedAt;
}
