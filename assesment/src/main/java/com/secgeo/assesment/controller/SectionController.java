package com.secgeo.assesment.controller;

import com.secgeo.assesment.entities.Section;
import com.secgeo.assesment.repo.SectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/sections")
public class SectionController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final SectionRepository  sectionRepository;

    public SectionController(SectionRepository sectionRepository){
        this.sectionRepository = sectionRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Section> getAllSections() {
        LOG.info("Getting all Sections.");
        return sectionRepository.findAll();
    }

    @RequestMapping(value = "/get/{sectionName}", method = RequestMethod.GET)
    public Section getSection(@PathVariable String sectionName){
        Optional<Section> section = sectionRepository.findById(sectionName);
        if(section != null && section.isPresent())
            return section.get();
        else
            return null;

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Section addNewSection(@RequestBody Section section) {
        LOG.info("Saving section.");
        return sectionRepository.save(section);
    }

    @RequestMapping(value = "/delete/{sectionName}", method = RequestMethod.DELETE)
    public void deleteSection(@PathVariable String sectionName) {
        LOG.info("Deleting section : {}", sectionName);
        sectionRepository.deleteById(sectionName);
    }

    @RequestMapping(value = "/by-code", method = RequestMethod.GET)
    public List<Section> getSectionsByCode(@RequestParam("code") String code){
        List<Section> data = sectionRepository.querySectionByGeologicalClassCode(code);
        return data;

    }
}
