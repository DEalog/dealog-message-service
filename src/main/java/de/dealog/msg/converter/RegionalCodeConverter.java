package de.dealog.msg.converter;

import com.google.common.base.Converter;
import de.dealog.msg.service.model.RegionalCode;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
@Slf4j
public class RegionalCodeConverter extends Converter<String, RegionalCode> {

    private static final String COUNTRY = "country";
    private static final String STATE = "state";
    private static final String COUNTY = "county";
    private static final String DISTRICT = "district";
    private static final String MUNCIPALITY = "muncipality";

    /**
     * The regex to find the different codes as specfied for the ARS.
     * {@see https://de.wikipedia.org/wiki/Amtlicher_Gemeindeschl%C3%BCssel#Regionalschl%C3%BCssel}
     */
    private static final String CODES_REGEX = "\\b(?<%s>[0-9]{2})(?<%s>[0-9]{1})?(?<%s>[0-9]{2})?(?<%s>[0-9]{4})?(?<%s>[0-9]{3})?\\b";

    Map<String, String> codes = new HashMap<>(Map.of(COUNTRY, "", STATE, "", COUNTY, "", DISTRICT, "", MUNCIPALITY, ""));

    @Override
    protected RegionalCode doForward(final String ars) {

        final Map<String, String> codes = findCodes(ars);

        return RegionalCode.builder()
                .country(codes.get(COUNTRY))
                .state(codes.get(STATE))
                .county(codes.get(COUNTY))
                .district(codes.get(DISTRICT))
                .muncipality(codes.get(MUNCIPALITY))
                .build();
    }

    @Override
    protected String doBackward(final RegionalCode regionalCode) {
        return String.format("%s%s%s%s%s",
                regionalCode.getCountry(), regionalCode.getState(), regionalCode.getCounty(), regionalCode.getDistrict(), regionalCode.getMuncipality());
    }

    /**
     * Disassembles the ars to regional codes.
     *
     * @param ars the ars as string
     * @return a map containing the regional codes
     */
    private Map<String, String> findCodes(final String ars) {
        log.debug("find codes for ars {}", ars);
        final String regex = String.format(CODES_REGEX, COUNTRY, STATE, COUNTY, DISTRICT, MUNCIPALITY);
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(ars);

        while (matcher.find()) {
            if (matcher.groupCount() == 5) {
                codes.forEach((groupName, code) -> codes.put(groupName, matcher.group(groupName)));
            }
        }
        return codes;
    }
}
