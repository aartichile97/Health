package com.example.practice.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
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

//	List<PersonalDetails> getPersonalDetails(ProposerListing proposerListing);
	List<Map<String, Object>> getPersonalDetails(ProposerListing proposerListing);

	Integer totalRecords();

//	Integer failedRecords();
	void exportPersonalDetailsToExcel() throws IOException;

	String exportSamplePersonalDetailsToExcel() throws IOException;

	List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile file, Map<String, Integer> recordCount)
			throws IOException;
//	public List<ResponseExcel> importPersonalDetailsFromExcel(MultipartFile file) throws IOException;

}
