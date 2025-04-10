package com.example.practice.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.practice.dto.PersonalDetailsDto;
import com.example.practice.entity.PersonalDetails;
import com.example.practice.listing.ProposerListing;

import jakarta.servlet.http.HttpServletResponse;

public interface PersonalDetailsService {

	PersonalDetails savePersonalDetails(PersonalDetailsDto personalDetailsDto);

	List<PersonalDetails> getAllPersonalDetails();

	Optional<PersonalDetails> getPersonalDetailsById(Integer personalId);

	PersonalDetails updatePersonalDetails(Integer personalId, PersonalDetailsDto personalDetailsDto);

	void deletePersonalDetails(Integer personalId);

	List<PersonalDetails> getPersonalDetails(ProposerListing proposerListing);

    void exportPersonalDetailsToExcel(HttpServletResponse response) throws IOException;
//    public void createExcelTemplate(String filePath) throws IOException;
	Integer totalRecords();

}
