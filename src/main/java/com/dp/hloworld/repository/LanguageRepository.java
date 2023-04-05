package com.dp.hloworld.repository;

import com.dp.hloworld.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Long>{
}
