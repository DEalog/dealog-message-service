package de.dealog.msg.service.model;

import com.google.common.base.Strings;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

@Builder
@Getter
@ToString
public class RegionalCode {
    /**
     * 1.–2. Stelle   = Kennzahl des Bundeslandes
     */
    private final String country;

    /**
     * 3. Stelle      = Kennzahl des Regierungsbezirks; wenn nicht vorhanden: 0
     */
    private final String state;

    /**
     * 4.–5. Stelle   = Kennzahl des Landkreises oder der kreisfreien Stadt
     */
    private final String county;

    /**
     * 6.–9. Stelle   = Verbandsschlüssel
     */
    private final String district;

    /**
     * 10.–12. Stelle = Gemeindekennzahl
     */
    private final String muncipality;

    public Optional<String>  getRegionalCountry(){
        return buildRegionalArs(Collections.singletonList(country));
    }
    public Optional<String>  getRegionalState(){
        return buildRegionalArs(Arrays.asList(country, state));
    }

    public Optional<String>  getRegionalCounty(){
        return buildRegionalArs(Arrays.asList(country, state, county));
    }

    public Optional<String> getRegionalDistrict(){
        return buildRegionalArs(Arrays.asList(country, state, county, district));
    }

    public Optional<String> getRegionalMuncipality(){
        return buildRegionalArs(Arrays.asList(country, state, county, district, muncipality));
    }

    public List<String> asList(){
        final List<String> codes = new ArrayList<>();
        getRegionalCountry().ifPresent(codes::add);
        getRegionalState().ifPresent(codes::add);
        getRegionalCounty().ifPresent(codes::add);
        getRegionalDistrict().ifPresent(codes::add);
        getRegionalMuncipality().ifPresent(codes::add);
        return codes;
    }

    private Optional<String> buildRegionalArs(final List<String> regionalCodes){
        String result = null;
        if (regionalCodes.size() > 0 && !Strings.isNullOrEmpty(regionalCodes.get(regionalCodes.size() -1))) {
            final StringBuilder pattern = new StringBuilder();
            regionalCodes.forEach(s -> pattern.append("%s"));
            result = String.format(pattern.toString(), regionalCodes.toArray());
        }
        return Optional.ofNullable(result);
    }
}
