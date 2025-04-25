package com.example.practice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.practice.entity.QueueTable;

public interface QueueTableRepository extends JpaRepository<QueueTable, Integer>{

	List<QueueTable> findByIsProcessed(char c);
	
	

}
