package com.example.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.practice.entity.QueueTable;

public interface QueueTableRepository extends JpaRepository<QueueTable, Integer>{

}
