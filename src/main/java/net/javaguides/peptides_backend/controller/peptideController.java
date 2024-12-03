package net.javaguides.peptides_backend.controller;

import net.javaguides.peptides_backend.dto.peptideDto;
import net.javaguides.peptides_backend.service.PeptideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin

@Transactional
@RestController
//Karthicksudhan

@RequestMapping(value = "/api/peptide")
public class peptideController {

    @Autowired(required=true)
    PeptideService peptideService;

    @RequestMapping(value = "/searchPeptide",method = RequestMethod.GET)
    public List<peptideDto> searchPeptide(@RequestParam (value = "category",required = true) String category,
                                          @RequestParam (value = "searchValue",required = true) String searchValue){
        searchValue = searchValue.trim(); // Remove leading/trailing whitespace
        searchValue = searchValue.replaceAll("\\s+", " "); // Replace multiple spaces/newlines/tabs with a single space
        searchValue = searchValue.replaceAll("[\n\r]", "");
        return peptideService.getPeptide(category,searchValue);
    }
    @RequestMapping(value = "/blast",method = RequestMethod.GET)
    public String blast(@RequestParam (value = "blastSequence",required = true) String blastSequence){
        blastSequence = blastSequence.trim(); // Remove leading/trailing whitespace

        return peptideService.blast(blastSequence);
    }
}

