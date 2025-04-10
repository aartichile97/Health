package com.example.practice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.practice.entity.GenderTable;

@Repository
public interface GenderTableRepository extends JpaRepository<GenderTable, Integer> {
	
	Optional<GenderTable> findByGenderType(String genderType);
}
