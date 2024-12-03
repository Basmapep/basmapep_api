package net.javaguides.peptides_backend.service;

import net.javaguides.peptides_backend.dto.peptideDto;
import net.javaguides.peptides_backend.entity.peptide_1121_r1;
import net.javaguides.peptides_backend.mapper.peptideMapper;
import net.javaguides.peptides_backend.repository.PeptideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PeptideServiceImpl implements PeptideService {

    @Autowired
    PeptideRepository peptideRep;

    @Override
    public List<peptideDto> getPeptide(String category, String param) {
        List<peptideDto> beanList = new ArrayList<>();

        if (category.equalsIgnoreCase("Accession")) {
            List<peptide_1121_r1> list = peptideRep.findByAccession(param);
            if (list != null && !list.isEmpty()) {
                for (peptide_1121_r1 entity : list) {
                    peptideDto dto = peptideMapper.mapTopeptideDto(entity);
                    beanList.add(dto);
                }
            }
        } else if (category.equalsIgnoreCase("Score")) {
            // Handle Score category
            List<peptide_1121_r1> list = peptideRep.findByScore(param); // Ensure this method is implemented in repository

            if (list != null && !list.isEmpty()) {
                for (peptide_1121_r1 entity : list) {
                    peptideDto dto = peptideMapper.mapTopeptideDto(entity);
                    beanList.add(dto);
                }
            }
        } else if (category.equalsIgnoreCase("Peptide sequence")) {
            // Handle Peptide sequence category
            List<peptide_1121_r1> list = peptideRep.findByPeptideSequence(param); // Ensure this method is implemented in repository
            if (list != null && !list.isEmpty()) {
                for (peptide_1121_r1 entity : list) {
                    peptideDto dto = peptideMapper.mapTopeptideDto(entity);
                    beanList.add(dto);
                }
            }
        } else if (category.equalsIgnoreCase("Peptide modification")) {
            // Handle Peptide modification category
            List<peptide_1121_r1> list = peptideRep.findByPeptideModification(param); // Ensure this method is implemented in repository
            if (list != null && !list.isEmpty()) {
                for (peptide_1121_r1 entity : list) {
                    peptideDto dto = peptideMapper.mapTopeptideDto(entity);
                    beanList.add(dto);
                }
            }
        } else if (category.equalsIgnoreCase("Peptide Length")) {
            // Handle Peptide Length category
            List<peptide_1121_r1> list = peptideRep.findByPeptideLength(param); // Ensure this method is implemented in repository
            if (list != null && !list.isEmpty()) {
                for (peptide_1121_r1 entity : list) {
                    peptideDto dto = peptideMapper.mapTopeptideDto(entity);
                    beanList.add(dto);
                }
            }
        } else if (category.equalsIgnoreCase("Peptide Mass")) {
            // Handle Peptide Mass category
            List<peptide_1121_r1> list = peptideRep.findByPeptideMass(param); // Ensure this method is implemented in repository
            if (list != null && !list.isEmpty()) {
                for (peptide_1121_r1 entity : list) {
                    peptideDto dto = peptideMapper.mapTopeptideDto(entity);
                    beanList.add(dto);
                }
            }
        }
        return beanList;
    }

    // Method to process peptides and run BLAST
    public String blast(String blastSequence) {
        // Step 1: Get the list of peptides based on the blastSequence
        List<peptide_1121_r1> list = peptideRep.blast(blastSequence);  // Assuming peptideRep is your repository instance

        // Step 2: Write peptides to a .fasta file
        String fastaFilePath = "C:\\karthick\\personal\\SathishProjects\\datafiles\\peptides.fasta";  // Path to save FASTA file
        writePeptidesToFasta(list, fastaFilePath);

        // Step 3: Perform BLAST with the generated .fasta file
        String queryFile = "C:\\karthick\\personal\\SathishProjects\\datafiles\\peptides.fasta"; // Path to the peptide.fasta file

        // Paths and settings
        String databasePath = "C:\\karthick\\personal\\SathishProjects\\datafiles\\peptide_db"; // Path to the BLAST database

        // Full path to the blastp executable
        String blastExecutable = "C:\\Program Files\\NCBI\\blast-2.16.0+\\bin\\blastp.exe"; // Update with the full path to blastp.exe

        // Process the BLAST command and capture the results
        StringBuilder output = new StringBuilder();
        try {
            // Build the BLAST command
            String command = blastExecutable + " -query " + queryFile + " -db " + databasePath + " -outfmt 7";
            System.out.println("Running BLAST command: " + command); // Debug print

            // Use ProcessBuilder to run the BLAST command
            ProcessBuilder pb = new ProcessBuilder(
                    blastExecutable,
                    "-query", queryFile,  // Use the query file
                    "-db", databasePath,  // Specify the database path
                    "-outfmt", "7"         // Tabular format (output as tabular)
            );

            // Start the process
            Process process = pb.start();

            // Capture standard output from BLAST
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");  // Append each line of BLAST result
                }
            }

            // Capture any error output
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    output.append("Error: ").append(errorLine).append("\n");  // Append error output if exists
                }
            }

            // Wait for the process to complete
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            // Catch IO or InterruptedException and print stack trace
            e.printStackTrace();
            output.append("Error: ").append(e.getMessage()).append("\n");
        }

        // Return the captured BLAST output

        return  output.toString() ;

    }
    // Method to write peptides to a FASTA file
    private void writePeptidesToFasta(List<peptide_1121_r1> peptides, String fastaFilePath) {
        // Use a set to keep track of already seen accession numbers
        Set<String> seenAccessions = new HashSet<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fastaFilePath))) {
            for (peptide_1121_r1 peptide : peptides) {
                String accession = peptide.getAccession();  // Access the peptide accession
                String peptideSeq = peptide.getPeptideSeq();  // Access the peptide sequence

                // Only write if the accession has not been written before
                if (peptideSeq != null && !peptideSeq.isEmpty() && !seenAccessions.contains(accession)) {
                    // Add the accession to the set to prevent duplicates
                    seenAccessions.add(accession);

                    // Write the accession and sequence to the FASTA file
                    writer.write(">" + accession + "\n");
                    writer.write(peptideSeq + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
