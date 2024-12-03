package net.javaguides.peptides_backend.service;

import net.javaguides.peptides_backend.dto.peptideDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PeptideService {
    List<peptideDto> getPeptide(String category, String param);
    String blast(String blastSequence );

}
