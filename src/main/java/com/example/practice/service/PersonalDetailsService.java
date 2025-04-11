package com.example.practice.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import com.example.practice.dto.PersonalDetailsDto;
import com.example.practice.entity.PersonalDetails;
import com.example.practice.listing.ProposerListing;

public interface PersonalDetailsService {

	PersonalDetails savePersonalDetails(PersonalDetailsDto personalDetailsDto);

	List<PersonalDetails> getAllPersonalDetails();

	Optional<PersonalDetails> getPersonalDetailsById(Integer personalId);

	PersonalDetails updatePersonalDetails(Integer personalId, PersonalDetailsDto personalDetailsDto);

	void deletePersonalDetails(Integer personalId);

	List<PersonalDetails> getPersonalDetails(ProposerListing proposerListing);

	Integer totalRecords();

	String exportPersonalDetailsToExcel() throws IOException;

	public List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile file) throws IOException;
}
