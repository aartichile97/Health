package com.example.practice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.practice.dto.PersonalDetailsDto;
import com.example.practice.entity.PersonalDetails;
import com.example.practice.listing.ProposerListing;
import com.example.practice.response.ResponseHandler;
import com.example.practice.service.PersonalDetailsService;

@RestController
@RequestMapping("/personal_details/")
public class PersonalDetailsController {

	@Autowired
	private PersonalDetailsService personalDetailsService;

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

	@GetMapping("list")
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

	@DeleteMapping("delete/{personalId}")
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

	@PutMapping("update/{personalId}")
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

	@PostMapping("listing")
	public ResponseHandler getPersonalDetails(@RequestBody ProposerListing proposerListing) {
//		ResponseHandler response = new ResponseHandler();
		try {
//			List<PersonalDetails> personalDetailsList = personalDetailsService.getPersonalDetails(proposerListing);
			List<Map<String, Object>> personalDetailsList = personalDetailsService.getPersonalDetails(proposerListing);

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

//	@GetMapping("export_personal_details")
//	public ResponseHandler exportPersonalDetailsToExcel(HttpServletResponse servletResponse) {
////		ResponseHandler response = new ResponseHandler();
//		try {
//			personalDetailsService.exportPersonalDetailsToExcel(servletResponse);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
	@GetMapping("/excel_export")
	public void exportPersonalDetailsToExcel() {
		try {
			// Manually invoke the scheduled task method
			personalDetailsService.exportPersonalDetailsToExcel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("excel_sample_export")
	public ResponseHandler exportSamplePersonalDetailsToExcel() {
//		ResponseHandler response = new ResponseHandler();
		try {
			String downloadLink = personalDetailsService.exportSamplePersonalDetailsToExcel();
			response.setData(downloadLink);
			response.setMessage("Downloaded");
			response.setStatus(true);

		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed to export");
			response.setStatus(false);
		}
		return response;
	}

	@PostMapping(value = "excel_import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseHandler importPersonalDetails(@RequestParam("file") MultipartFile file) {

		ResponseHandler response = new ResponseHandler();

		try {

			Map<String, Integer> recordCount = new HashMap<>();
			List<PersonalDetails> savedExcelList = personalDetailsService.importPersonalDetailsFromExcel(file,
					recordCount);
			Integer totalExcelCount = recordCount.getOrDefault("totalExcelCount", 0);
			Integer errorExcelCount = recordCount.getOrDefault("errorExcelCount", 0);

			response.setData(savedExcelList);
//	        response.setMessage("Excel imported successfully. Rows saved: " +savedExcelList.size());
//	        response.setMessage("Successfully saved " +savedExcelList.size()  +" & failed records "+errorExcelCount+ " rows out of "+totalExcelCount);
			response.setMessage("Out of " + totalExcelCount + " , " + savedExcelList.size() + " Saved "
					+ errorExcelCount + " Failed ");

			response.setStatus(true);
			response.setTotalRecords(totalExcelCount);
//			response.setTotalRecords(personalDetailsService.totalRecords());
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed to import excel file");
			response.setStatus(false);
			response.setData(new ArrayList<>());
		}
		return response;
	}

//	@GetMapping("/products")
//    public List<Map<String, Object>> getProducts() {
//        return personalDetailsService.getAllProducts();
//    }

	@GetMapping("/products")
	public ResponseHandler getProducts() {
		ResponseHandler response = new ResponseHandler();
		try {
			List<Map<String, Object>> products = personalDetailsService.getAllProducts();

			response.setData(products);
			response.setMessage("Products retrieved successfully.");
			response.setStatus(true);
			response.setTotalRecords(products.size());

		} catch (Exception e) {
			response.setData(new ArrayList<>());
			response.setMessage("Failed to fetch products: " + e.getMessage());
			response.setStatus(false);
		}
		return response;
	}
	
	
	
	@PostMapping(value = "excel_import_schedule", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseHandler importSchedulePersonalDetails(@RequestParam("file") MultipartFile file) {

		ResponseHandler response = new ResponseHandler();

		try {

			Map<String, Integer> recordCount = new HashMap<>();
			List<PersonalDetails> savedExcelList = personalDetailsService.importScheduleDetailsFromExcel(file,
					recordCount);
			Integer totalExcelCount = recordCount.getOrDefault("totalExcelCount", 0);
			Integer errorExcelCount = recordCount.getOrDefault("errorExcelCount", 0);

			response.setData(savedExcelList);
			response.setMessage("Out of " + totalExcelCount + " , " + savedExcelList.size() + " Saved "
					+ errorExcelCount + " Failed ");

			response.setStatus(true);
			response.setTotalRecords(totalExcelCount);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed to import excel file");
			response.setStatus(false);
			response.setData(new ArrayList<>());
		}
		return response;
	}

}
