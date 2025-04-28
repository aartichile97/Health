package com.example.practice.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import com.example.practice.dto.PersonalDetailsDto;
import com.example.practice.entity.PersonalDetails;
import com.example.practice.entity.Product;
import com.example.practice.listing.ProposerListing;
import jakarta.servlet.http.HttpServletResponse;

public interface PersonalDetailsService {

	// Add
	PersonalDetails savePersonalDetails(PersonalDetailsDto personalDetailsDto);

	// List all details
	List<PersonalDetails> getAllPersonalDetails();

	// Get details by Id
	Optional<PersonalDetails> getPersonalDetailsById(Integer personalId);

	// Update details by Id
	PersonalDetails updatePersonalDetails(Integer personalId, PersonalDetailsDto personalDetailsDto);

	// Delete details by Id
	void deletePersonalDetails(Integer personalId);

	// List<PersonalDetails> getPersonalDetails(ProposerListing proposerListing);
	// get sorted/filtered records by Id
	List<Map<String, Object>> getPersonalDetails(ProposerListing proposerListing);

	Integer totalRecords();

	// Integer failedRecords();
	// Export DB details
	void exportPersonalDetailsToExcel() throws IOException;

	// Export Sample Excel with headers
	String exportSamplePersonalDetailsToExcel() throws IOException;

	//
	List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile file, Map<String, Integer> recordCount)
			throws IOException;

	// public List<ResponseExcel> importPersonalDetailsFromExcel(MultipartFile file)
	// throws IOException;

	// fetching third party API details
	public List<Map<String, Object>> getAllProducts();

	// Queue
	// importing details from excel to DB using scheduling
	public List<PersonalDetails> importScheduleDetailsFromExcel(MultipartFile file, Map<String, Integer> recordCount)
			throws IOException;
	
	void scheduleQueueProcessing();

}
