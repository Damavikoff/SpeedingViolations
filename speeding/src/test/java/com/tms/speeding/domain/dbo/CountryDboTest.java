package com.tms.speeding.domain.dbo;


import com.tms.speeding.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CountryDboTest {

    @Autowired
    private CountryRepository repo;

    @Test
    public void createCountryTest() {
        CountryDbo country = new CountryDbo("Test", "tst");
        CountryDbo savedCountry = repo.save(country);

        assertNotNull(savedCountry);
    }

    @Test
    public void addRegionsTest() {
        CountryDbo country = new CountryDbo("Test", "tst");
        RegionDbo region = new RegionDbo("Test");
        region.setCountry(country);
        List<RegionDbo> regionList = new ArrayList<>();
        regionList.add(region);
        country.setRegions(regionList);

        repo.save(country);

        RegionDbo findedRegion = repo.findByTitle(country.getTitle()).getRegions().get(0);

        assertThat(findedRegion.getTitle()).isEqualTo("Test");
        assertThat(findedRegion.getCountry()).isEqualTo(country);
    }

    @Test
    public void findNonExistingCountryTest() {
        String title = "BigBrotherIsWatchingYou";
        CountryDbo nonExistentCountry = repo.findByTitle(title);

        assertNull(nonExistentCountry);
    }

    @Test
    public void findExistingCountryTest() {
        String title = "Osterreich";
        CountryDbo country = new CountryDbo(title, null);
        country.setIso("OST");
        repo.save(country);

        CountryDbo existentCountry = repo.findByTitle(title);

        assertThat(existentCountry.getTitle()).isEqualTo(title);
    }

    @Test
    public void countryListIsGreaterThanNullTest() {
        CountryDbo country = new CountryDbo();
        country.setTitle("Osterriech");
        country.setIso("OST");
        repo.save(country);
        List<CountryDbo> countries = (List<CountryDbo>) repo.findAll();

        assertThat(countries).size().isGreaterThan(0);
    }

    @Test
    public void deleteCountryTest() {
        List<CountryDbo> countries = (List<CountryDbo>) repo.findAll();
        Integer id;
        if (countries.isEmpty()) {
            repo.save(new CountryDbo("Test", "tst"));
            id = repo.findByTitle("Test").getId();
        } else {
            id = countries.get(countries.size() - 1).getId();
        }
        boolean isExistBeforeDeleting = repo.findById(id).isPresent();

        repo.deleteById(id);

        boolean isExistAfterDeleting = repo.findById(id).isPresent();

        assertTrue(isExistBeforeDeleting);
        assertFalse(isExistAfterDeleting);
    }
}
