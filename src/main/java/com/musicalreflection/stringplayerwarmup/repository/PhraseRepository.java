package com.musicalreflection.stringplayerwarmup.repository;

import com.musicalreflection.stringplayerwarmup.model.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {
    @Query(value = "SELECT * FROM phrase ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Phrase> findRandomPhrase();
}