package com.secgeo.assesment.repo;

import com.secgeo.assesment.entities.Section;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends MongoRepository<Section, String> {
    @Query(value = "{'geologicalClasses.code': ?0}")
    List<Section> querySectionByGeologicalClassCode(@Param("code") String code);

}
