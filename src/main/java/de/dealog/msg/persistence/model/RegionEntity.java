package de.dealog.msg.persistence.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Immutable
@Table(name = "vg250_compound")
public class RegionEntity extends PanacheEntity implements Region {

    /**
     * Amtlicher Regionalschlüssel
     * 1.–2. Stelle   = Kennzahl des Bundeslandes
     * 3. Stelle      = Kennzahl des Regierungsbezirks; wenn nicht vorhanden: 0
     * 4.–5. Stelle   = Kennzahl des Landkreises oder der kreisfreien Stadt
     * 6.–9. Stelle   = Verbandsschlüssel
     * 10.–12. Stelle = Gemeindekennzahl
     */
    @Column(name = "ars")
    private String code;

    /**
     * The name of the region
     */
    @Column(name = "gen")
    private String name;

    /**
     * Description of the region
     */
    @Column(name = "bez")
    private String description;

    /**
     * The geographic code delineating the affected area of the alert message
     */
    @ToString.Exclude
    @Column(name = "geom")
    private MultiPolygon<G2D> geometries;
}
