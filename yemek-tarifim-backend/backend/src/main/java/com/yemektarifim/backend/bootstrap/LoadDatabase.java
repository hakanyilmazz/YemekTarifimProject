package com.yemektarifim.backend.bootstrap;

import com.yemektarifim.backend.model.Country;
import com.yemektarifim.backend.model.Role;
import com.yemektarifim.backend.model.Setting;
import com.yemektarifim.backend.repository.CountryRepository;
import com.yemektarifim.backend.repository.RoleRepository;
import com.yemektarifim.backend.repository.SettingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class LoadDatabase {

    // Konsola veri yazdırmada kullanılır.
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    /**
     * Veritabanı uygulama ilk başlatıldığında bazı default değerler alması gerekli.
     * Bu yüzden ilk başlatılma veya sonrada başlatılma durumlarına göre veritabanına bazı bilgiler kaydedilecek.
     */
    @Bean
    public CommandLineRunner initDatabase(SettingRepository settingRepository, CountryRepository countryRepository, RoleRepository roleRepository) {
        final List<Setting> settingList = settingRepository.findAll();

        if (settingList.size() != 0 && !settingList.get(0).getIsFirstStarting()) {
            return args -> log.info("Database is ready.");
        }

        final List<Country> countryList = createCountryList();
        saveCountryList(countryRepository, countryList);

        final List<Role> roleList = createRoleList();
        saveRoleList(roleRepository, roleList);

        settingRepository.insert(new Setting(false));

        return args -> {
            log.info("Country list loading to database...");
            log.info("Role list loading to database...");
        };
    }

    /**
     * Önceden belirlenmiş kullanıcı rollerini veritabanına kaydeder.
     */
    private void saveRoleList(RoleRepository roleRepository, List<Role> roleList) {
        for (Role role : roleList) {
            roleRepository.insert(role);
        }
    }

    /**
     * @return Default rol değerlerini döndürür.
     */
    private List<Role> createRoleList() {
        List<Role> roleList = new ArrayList<>();

        roleList.add(new Role("ADMIN"));
        roleList.add(new Role("MODERATOR"));
        roleList.add(new Role("USER"));
        roleList.add(new Role("BANNED"));

        return roleList;
    }

    /**
     * Önceden belirlenmiş ülke isimlerini veritabanına kaydeder.
     */
    private void saveCountryList(CountryRepository countryRepository, List<Country> countryList) {
        for (Country country : countryList) {
            countryRepository.insert(country);
        }
    }

    /**
     * @return Önceden belirlenmiş ülke isimlerini bir listeye ekler ve listeyi döndürür.
     */
    private List<Country> createCountryList() {
        List<Country> countryList = new ArrayList<>();

        countryList.add(new Country("Turkey"));
        countryList.add(new Country("Australia"));
        countryList.add(new Country("Belgium"));
        countryList.add(new Country("Brazil"));
        countryList.add(new Country("Canada"));
        countryList.add(new Country("Azerbaijan"));
        countryList.add(new Country("Argentina"));
        countryList.add(new Country("Austria"));
        countryList.add(new Country("France"));
        countryList.add(new Country("Greece"));
        countryList.add(new Country("Iraq"));
        countryList.add(new Country("Italy"));
        countryList.add(new Country("Japan"));
        countryList.add(new Country("Mexico"));
        countryList.add(new Country("Saudi Arabia"));
        countryList.add(new Country("Thailand"));
        countryList.add(new Country("Ukraine"));
        countryList.add(new Country("Zambia"));
        countryList.add(new Country("South Africa"));
        countryList.add(new Country("United States of America"));
        countryList.add(new Country("Norway"));

        return countryList;
    }
}