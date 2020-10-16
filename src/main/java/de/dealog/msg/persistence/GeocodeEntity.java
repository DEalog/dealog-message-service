package de.dealog.msg.persistence;

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
@AllArgsConstructor
@NoArgsConstructor
public class GeocodeEntity extends PanacheEntity implements Geocode {

    private String hash;

    /**
     * The geographic code delineating the affected area of the alert message
     */
    @ToString.Exclude
    private MultiPolygon<G2D> polygons;

    @OneToMany
    private List<MessageEntity> messages;
}
