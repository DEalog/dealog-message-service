package de.dealog.msg.rest.validations;

import de.dealog.msg.rest.model.GeoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidGeoRequestValidator implements ConstraintValidator<ValidGeoRequest, GeoRequest> {


    public boolean isValid(final GeoRequest geoRequest, final ConstraintValidatorContext context) {
        if (geoRequest == null) {
            return true;
        }
        final Double longitude = geoRequest.getLongitude();
        final Double latitude = geoRequest.getLatitude();
        if (longitude == null && latitude == null) {
            return true;
        }

        return !(longitude == null || latitude == null);
    }
}
