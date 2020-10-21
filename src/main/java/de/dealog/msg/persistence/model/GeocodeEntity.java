package de.dealog.msg.persistence.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeocodeEntity extends PanacheEntity implements Geocode {

    /**
     * MD5 hash of the WKT String representation of the {@link GeocodeEntity#polygons}
     */
    private String hash;

    /**
     * The geographic code delineating the affected area of the alert message
     */
    @ToString.Exclude
    private MultiPolygon<G2D> polygons;

    @OneToMany
    private List<MessageEntity> messages;
}
