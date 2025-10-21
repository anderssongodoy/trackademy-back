package com.trackademy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trackademy.entity.Campus;
import com.trackademy.entity.Institution;
import com.trackademy.repository.CampusRepository;
import com.trackademy.repository.InstitutionRepository;

@SpringBootApplication
public class TrackademyApplication {

    private static final Logger logger = LoggerFactory.getLogger(TrackademyApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TrackademyApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeData(CampusRepository campusRepository,
                                           InstitutionRepository institutionRepository) {
        return args -> {
            logger.info("Starting data initialization");

            Institution institution = institutionRepository.findBySlug("utp")
                    .orElseGet(() -> {
                        logger.info("Creating UTP institution");
                        Institution newInstitution = Institution.builder()
                                .slug("utp")
                                .name("Universidad Tecnologica del Peru")
                                .country("PE")
                                .build();
                        Institution saved = institutionRepository.save(newInstitution);
                        logger.info("UTP institution created with ID: {}", saved.getId());
                        return saved;
                    });

            String[] campuses = {
                    "Lima Centro", "Lima Norte", "Lima Sur", "Ate", "San Juan de Lurigancho",
                    "Arequipa", "Chiclayo", "Chimbote", "Piura", "Huancayo",
                    "Ica", "Trujillo", "Iquitos", "Tacna", "Pucallpa"
            };

            int createdCount = 0;
            for (String campusName : campuses) {
                if (campusRepository.findByName(campusName).isEmpty()) {
                    Campus campus = Campus.builder()
                            .institution(institution)
                            .name(campusName)
                            .city(extractCity(campusName))
                            .region(extractRegion(campusName))
                            .active(true)
                            .build();
                    campusRepository.save(campus);
                    createdCount++;
                    logger.debug("Campus created: {}", campusName);
                }
            }
            logger.info("Data initialization completed. Campuses created: {}", createdCount);
        };
    }

    private static String extractCity(String campusName) {
        return campusName.replaceAll("\\s*(Centro|Norte|Sur)\\s*", "").trim();
    }

    private static String extractRegion(String campusName) {
        return campusName;
    }
}
