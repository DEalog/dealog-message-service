package de.dealog.msg.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "geocode_id")
    private GeocodeEntity geocode;

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
