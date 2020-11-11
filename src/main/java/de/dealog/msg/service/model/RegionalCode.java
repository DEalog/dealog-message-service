package de.dealog.msg.service.model;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class RegionalCode {
    /**
     * 1.–2. Stelle   = Kennzahl des Bundeslandes
     */
    private String country;

    /**
     * 3. Stelle      = Kennzahl des Regierungsbezirks; wenn nicht vorhanden: 0
     */
    private String state;

    /**
     * 4.–5. Stelle   = Kennzahl des Landkreises oder der kreisfreien Stadt
     */
    private String county;

    /**
     * 6.–9. Stelle   = Verbandsschlüssel
     */
    private String district;

    /**
     * 10.–12. Stelle = Gemeindekennzahl
     */
    private String muncipality;
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
