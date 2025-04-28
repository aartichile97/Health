package com.example.practice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.practice.entity.QueueTable;

public interface QueueTableRepository extends JpaRepository<QueueTable, Integer>{

//	List<QueueTable> findByIsProcessed(char c);
	Optional<QueueTable> findByIsProcessed(Character c);

	
	

}
