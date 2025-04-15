package com.example.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.practice.response.ResponseExcel;

@Repository
public interface ResponseExcelRepository extends JpaRepository<ResponseExcel, Integer>{

}
