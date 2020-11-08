package de.dealog.msg.service.model;

import lombok.Builder;
import lombok.ToString;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import java.util.Optional;

@Builder
@ToString
public class QueryParams {

    private final String ars;

    private final RegionalCode regionalCode;

    private final Point<G2D> point;

    public Optional<RegionalCode> maybeRegionalCode() {
        return Optional.ofNullable(regionalCode);
    }

    public Optional<Point<G2D>> maybePoint() {
        return Optional.ofNullable(point);
    }

    public Optional<String> maybeArs() {
        return Optional.ofNullable(ars);
    }
}
