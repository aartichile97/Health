package com.example.practice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.practice.dto.PersonalDetailsDto;
import com.example.practice.entity.PersonalDetails;
import com.example.practice.listing.ProposerListing;
import com.example.practice.repository.PersonalDetailsRepository;
import com.example.practice.response.ResponseHandler;
import com.example.practice.service.PersonalDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/personal_details/")
public class PersonalDetailsController {

	@Autowired
	private PersonalDetailsService personalDetailsService;

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	ResponseHandler response = new ResponseHandler();

	@PostMapping("add")
	public ResponseHandler addPersonalDetails(@RequestBody PersonalDetailsDto personalDetailsDto) {
//		ResponseHandler response = new ResponseHandler();
		try {
			PersonalDetails savedDetails = personalDetailsService.savePersonalDetails(personalDetailsDto);

			response.setData(savedDetails);
			response.setMessage("success");
			response.setStatus(true);
			response.setTotalRecords(personalDetailsService.totalRecords());

		} catch (IllegalArgumentException ex) {
			response.setData(new ArrayList<>());
			response.setMessage(ex.getMessage());
			response.setStatus(false);

		} catch (Exception e) {
			response.setData(new ArrayList<>());
			response.setMessage(e.getMessage());
			response.setStatus(false);
		}
		return response;
	}

	@GetMapping("listing")
	public ResponseHandler getAllPersonalDetails() {
//		ResponseHandler response = new ResponseHandler();
		try {
			List<PersonalDetails> details = personalDetailsService.getAllPersonalDetails();

			response.setData(details);
			response.setMessage("Personal details retrieved successfully.");
			response.setStatus(true);
			response.setTotalRecords(personalDetailsService.totalRecords());
		} catch (IllegalArgumentException ex) {
			response.setData(new ArrayList<>());
			response.setMessage(ex.getMessage());
			response.setStatus(false);
		} catch (Exception ex) {
			response.setData(new ArrayList<>());
			response.setMessage(ex.getMessage());
			response.setStatus(false);
		}
		return response;
	}

	@GetMapping("get_by_id/{personalId}")
	public ResponseHandler getPersonalDetailsById(@PathVariable Integer personalId) {
//		ResponseHandler response = new ResponseHandler();
		try {
			Optional<PersonalDetails> personalDetails = personalDetailsService.getPersonalDetailsById(personalId);

			response.setData(personalDetails.get());
			response.setMessage("Personal details retrieved successfully.");
			response.setStatus(true);
			response.setTotalRecords(personalDetailsService.totalRecords());
		} catch (IllegalArgumentException ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(false);
			response.setData(new ArrayList<>());
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(false);
			response.setData(new ArrayList<>());
		}
		return response;
	}

	@DeleteMapping("delete_by_id/{personalId}")
	public ResponseHandler deletePersonalDetails(@PathVariable Integer personalId) {
//		ResponseHandler response = new ResponseHandler();
		try {
			personalDetailsService.deletePersonalDetails(personalId);
			response.setMessage("Deleted record with given ID: " + personalId);
			response.setStatus(true);
			response.setData(new ArrayList<>());
			response.setTotalRecords(personalDetailsService.totalRecords());
		} catch (IllegalArgumentException ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(false);
			response.setData(new ArrayList<>());
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(false);
			response.setData(new ArrayList<>());
		}
		return response;
	}

	@PutMapping("update_by_id/{personalId}")
	public ResponseHandler updatePersonalDetails(@PathVariable Integer personalId,
			@RequestBody PersonalDetailsDto personalDetailsDto) {
//		ResponseHandler response = new ResponseHandler();
		try {
			PersonalDetails updatedDetails = personalDetailsService.updatePersonalDetails(personalId,
					personalDetailsDto);
			response.setData(updatedDetails);
			response.setMessage("Personal details updated successfully.");
			response.setStatus(true);
			response.setTotalRecords(personalDetailsService.totalRecords());

		} catch (IllegalArgumentException ex) {
			response.setData(new ArrayList<>());
			response.setMessage(ex.getMessage());
			response.setStatus(null);
		}
		return response;
	}

	@PostMapping("get_all")
	public ResponseHandler getPersonalDetails(@RequestBody ProposerListing proposerListing) {
//		ResponseHandler response = new ResponseHandler();
		try {
			List<PersonalDetails> personalDetailsList = personalDetailsService.getPersonalDetails(proposerListing);
			response.setStatus(true);
			response.setTotalRecords(personalDetailsService.totalRecords());
			response.setData(personalDetailsList);
			response.setMessage("Personal details fetched successfully");
		} catch (IllegalArgumentException ex) {
			response.setStatus(false);
			response.setData(new ArrayList<>());
			response.setMessage(ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setData(new ArrayList<>());
			response.setMessage("An error occurred while fetching personal details.");
		}
		return response;
	}

	@GetMapping("export_to_excel")
	public void exportPersonalDetailsToExcel(HttpServletResponse response)

	{
//		ResponseHandler response = new ResponseHandler();

		try {
			personalDetailsService.exportPersonalDetailsToExcel(response);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

}
