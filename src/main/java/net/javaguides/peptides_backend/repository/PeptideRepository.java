package net.javaguides.peptides_backend.repository;

import net.javaguides.peptides_backend.entity.peptide_1121_r1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeptideRepository  extends JpaRepository <peptide_1121_r1  , String> {

    @Query(value = "select distinct a from peptide_1121_r1 a where a.accession like ?1")
    List<peptide_1121_r1> findByAccession(String param);

    @Query(value = "select distinct a from peptide_1121_r1 a")
    List<peptide_1121_r1> findByScore(String param);

    @Query(value = "select distinct a from peptide_1121_r1 a where cast(a.avgMass as string) like concat('%', ?1, '%')")
    List<peptide_1121_r1> findByPeptideMass(String param);

    @Query(value = "select distinct a from peptide_1121_r1 a where cast(a.peptideSeqLength as string) like concat('%', ?1, '%')")
    List<peptide_1121_r1> findByPeptideLength(String param);

    @Query(value = "select distinct a from peptide_1121_r1 a where a.peptideModification like ?1")
    List<peptide_1121_r1> findByPeptideModification(String param);

    @Query(value = "select distinct a from peptide_1121_r1 a where a.peptideSeq like ?1")
    List<peptide_1121_r1> findByPeptideSequence(String param);

    @Query(value = "select distinct a from peptide_1121_r1 a where a.peptideSeq like ?1")
    List<peptide_1121_r1> blast(String param);
}
