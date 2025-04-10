package com.example.practice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.practice.entity.PersonalDetails;

@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Integer> {

	Optional<PersonalDetails> findByEmailId(String emailId);

	Optional<PersonalDetails> findByMobileNo(String mobileNo);

	Optional<PersonalDetails> findByPanNumber(String panNumber);

//	@Query("select a from PersonalDetails a where a.mobileNo = :n")
//	Optional<PersonalDetails> m(@Param("n") String m);

}
