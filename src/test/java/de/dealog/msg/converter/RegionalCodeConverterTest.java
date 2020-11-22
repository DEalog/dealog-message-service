package de.dealog.msg.converter;

import de.dealog.msg.converter.RegionalCodeConverter;
import de.dealog.msg.service.model.RegionalCode;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
class RegionalCodeConverterTest {

    @Inject
    RegionalCodeConverter regionalCodeConverter;

    @Test
    void convertMuncipality() {
        final RegionalCode regionalCode = regionalCodeConverter.convert("091790134134");
        assert regionalCode != null;
        assertThat(regionalCode.getCountry(), is("09"));
        assertThat(regionalCode.getState(), is("1"));
        assertThat(regionalCode.getCounty(), is("79"));
        assertThat(regionalCode.getDistrict(), is("0134"));
        assertThat(regionalCode.getMuncipality(), is("134"));
        assertThat(regionalCode.getRegionalMuncipality().get(), is("091790134134"));
    }

    @Test
    void convertDistrict() {
        final RegionalCode regionalCode = regionalCodeConverter.convert("091790134");
        assert regionalCode != null;
        assertThat(regionalCode.getCountry(), is("09"));
        assertThat(regionalCode.getState(), is("1"));
        assertThat(regionalCode.getCounty(), is("79"));
        assertThat(regionalCode.getDistrict(), is("0134"));
        assertThat(regionalCode.getRegionalMuncipality().isPresent(), is(false));
    }

    @Test
    void doBackward() {
        final RegionalCode regionalCode = RegionalCode.builder()
                .country("12")
                .state("0")
                .county("64")
                .district("5410")
                .muncipality("340")
                .build();
        assertThat(regionalCodeConverter.reverse().convert(regionalCode), is("120645410340"));
    }
}