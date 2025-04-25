package com.example.practice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.example.practice.dto.PersonalDetailsDto;
import com.example.practice.entity.Gender;
import com.example.practice.entity.GenderTable;
import com.example.practice.entity.MaritalStatus;
import com.example.practice.entity.Nationality;
import com.example.practice.entity.Occupation;
import com.example.practice.entity.PersonalDetails;
import com.example.practice.entity.Product;
import com.example.practice.entity.QueueTable;
import com.example.practice.listing.ProposerListing;
import com.example.practice.listing.SearchFilter;
import com.example.practice.repository.GenderTableRepository;
import com.example.practice.repository.PersonalDetailsRepository;
import com.example.practice.repository.QueueTableRepository;
import com.example.practice.repository.ResponseExcelRepository;
import com.example.practice.repository.responseExcel2Repository;
import com.example.practice.response.ResponseExcel;
import com.example.practice.response.ResponseExcel2;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class PersonalDetailsServiceImpl implements PersonalDetailsService {

	private final QueueTableRepository queueTableRepository;

	Integer totalRecord = 0;

	PersonalDetailsServiceImpl(QueueTableRepository queueTableRepository) {
		this.queueTableRepository = queueTableRepository;
	}

	@Override
	public Integer totalRecords() {

		return totalRecord;
	}
//	Integer failedRecord;
//	@Override
//	public Integer failedRecords() {
//
//		return failedRecord;
//	}

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	@Autowired
	private GenderTableRepository genderTableRepository;

	@Autowired
	private ResponseExcelRepository responseExcelRepository;

	@Autowired
	private responseExcel2Repository responseExcel2Repository;

	@Override
	public PersonalDetails savePersonalDetails(PersonalDetailsDto personalDetailsDto) {

		String title = personalDetailsDto.getTitle();
		if (title == null || title.trim().isEmpty() || Pattern.matches("[A-Za-z]", title)) {
			throw new IllegalArgumentException("Title cannot be empty & only should contain characters");
		}

		String fullName = personalDetailsDto.getFullName();
		if (fullName == null || fullName.trim().isEmpty() || Pattern.matches("[A-Za-z]", fullName)) {
			throw new IllegalArgumentException("Full name cannot be empty & only should contain characters");
		}

		String dateOfBirth = personalDetailsDto.getDateOfBirth();
		if (dateOfBirth == null) {
			throw new IllegalArgumentException("Date of birth cannot be empty.");
		}

		String panNumber = personalDetailsDto.getPanNumber();
		if (panNumber == null || !Pattern.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}", panNumber)) {
			throw new IllegalArgumentException("PAN number must be in the format.");
		}
		Optional<PersonalDetails> existingPanNumber = personalDetailsRepository.findByPanNumber(panNumber);
		if (existingPanNumber.isPresent()) {
			throw new IllegalArgumentException("PAN number already exists.");
		}

		String emailId = personalDetailsDto.getEmailId();
		if (emailId == null || !Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", emailId)) {
			throw new IllegalArgumentException("Invalid email format.");
		}
		Optional<PersonalDetails> existingEmail = personalDetailsRepository.findByEmailId(emailId);
		if (existingEmail.isPresent()) {
			throw new IllegalArgumentException("Email already exists.");
		}

		String mobileNo = personalDetailsDto.getMobileNo();
		if (mobileNo == null || !Pattern.matches("[6-9]\\d{9}", mobileNo)) {
			throw new IllegalArgumentException("Mobile number must be exactly 10 digits & no characters allowed");
		}
		Optional<PersonalDetails> existingMobileNo = personalDetailsRepository.findByMobileNo(mobileNo);
		if (existingMobileNo.isPresent()) {
			throw new IllegalArgumentException("Mobile number already exists.");
		}

		String alternateMobileNo = personalDetailsDto.getAlternateMobileNo();
		if (alternateMobileNo != null && !Pattern.matches("[6-9]\\d{9}", alternateMobileNo)) {
			throw new IllegalArgumentException("Alternate mobile number must be exactly 10 digits");
		}

		String address = personalDetailsDto.getAddress();
		if (address == null || address.trim().isEmpty() || Pattern.matches("[A-Za-z]", address)) {
			throw new IllegalArgumentException("Address cannot be empty & only should contain characters");
		}

		String pincode = personalDetailsDto.getPincode();
		if (pincode == null || !Pattern.matches("4\\d{5}", pincode)) {
			throw new IllegalArgumentException("Pincode must be exactly 6 digits.");
		}

		String city = personalDetailsDto.getCity();
		if (city == null || city.trim().isEmpty() || Pattern.matches("[A-Za-z]", city)) {
			throw new IllegalArgumentException("City cannot be empty & only should contain characters");
		}

		String state = personalDetailsDto.getState();
		if (state == null || state.trim().isEmpty() || Pattern.matches("[A-Za-z]", state)) {
			throw new IllegalArgumentException("State cannot be empty & only should contain characters");
		}

		PersonalDetails pd = new PersonalDetails();

		String gender = personalDetailsDto.getGender();
		if (gender != null && !gender.isEmpty()) {
			try {
				pd.setGender(Gender.valueOf(gender.toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Invalid Gender value: " + gender);
			}
		} else {
			throw new IllegalArgumentException("Invalid Gender value");
		}

		String maritalStatus = personalDetailsDto.getMaritalStatus();
		if (maritalStatus != null && !maritalStatus.isEmpty()) {
			try {
				pd.setMaritalStatus(MaritalStatus.valueOf(maritalStatus.toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Invalid Marital Status value: " + maritalStatus);
			}
		} else {
			throw new IllegalArgumentException("Invalid Marital Status value");
		}

		String nationality = personalDetailsDto.getNationality();
		if (nationality != null && !nationality.isEmpty()) {
			try {
				pd.setNationality(Nationality.valueOf(nationality.toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Invalid nationality Status value: " + nationality);
			}
		} else {
			throw new IllegalArgumentException("Invalid nationality Status value");
		}

		String occupation = personalDetailsDto.getOccupation();
		if (occupation != null && !occupation.isEmpty()) {
			try {
				pd.setOccupation(Occupation.valueOf(occupation.toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Invalid occupation Status value: " + occupation);
			}
		} else {
			throw new IllegalArgumentException("Invalid occupation Status value");
		}

		String genderType = personalDetailsDto.getGender();
		if (genderType != null && !genderType.isEmpty()) {
			Optional<GenderTable> genderTableOptional = genderTableRepository.findByGenderType(genderType);

			if (genderTableOptional.isPresent()) {
				pd.setGenderId(genderTableOptional.get().getGenderId());
			} else {
				throw new IllegalArgumentException("Invalid gender value provided");
			}

		} else {
			throw new IllegalArgumentException("Gender cannot be null or empty");
		}

		pd.setTitle(personalDetailsDto.getTitle());
		pd.setFullName(fullName);
		pd.setDateOfBirth(dateOfBirth);
		pd.setPanNumber(panNumber);
//		pd.setGender(null);
//		pd.setGender(personalDetailsDto.getGender());
//		pd.setMaritalStatus(personalDetailsDto.getMaritalStatus());
//		pd.setNationality(personalDetailsDto.getNationality());
//		pd.setOccupation(personalDetailsDto.getOccupation());
		pd.setEmailId(emailId);
		pd.setMobileNo(mobileNo);
		pd.setAlternateMobileNo(alternateMobileNo);
		pd.setAddress(personalDetailsDto.getAddress());
		pd.setPincode(pincode);
		pd.setCity(personalDetailsDto.getCity());
		pd.setState(state);
		pd.setStatus('Y');

		return personalDetailsRepository.save(pd);
	}

	@Override
	public List<PersonalDetails> getAllPersonalDetails() {
		List<PersonalDetails> allPersonalDetails = personalDetailsRepository.findAll();
		List<PersonalDetails> activePersonalDetails = new ArrayList<>();
		for (PersonalDetails personalDetails : allPersonalDetails) {
			if ('Y' == personalDetails.getStatus()) {
				activePersonalDetails.add(personalDetails);
			}
		}

		if (activePersonalDetails == null || activePersonalDetails.isEmpty()) {
			throw new IllegalArgumentException("No personal details found.");
		}

		return activePersonalDetails;
	}

	@Override
	public Optional<PersonalDetails> getPersonalDetailsById(Integer personalId) {
		Optional<PersonalDetails> personalDetails = personalDetailsRepository.findById(personalId);

		if (personalDetails.isEmpty() || personalDetails.get().getStatus() == 'N') {
			throw new IllegalArgumentException("Personal details not found for id: " + personalId);
		}

		return personalDetails;
	}

	@Override
	public void deletePersonalDetails(Integer personalId) {
		Optional<PersonalDetails> personalDetails = personalDetailsRepository.findById(personalId);

		if (personalDetails.isEmpty() || personalDetails.get().getStatus() == 'N') {
			throw new IllegalArgumentException("Personal details not found for Id " + personalId);
		}

		PersonalDetails details = personalDetails.get();
		details.setStatus('N');

		personalDetailsRepository.save(details);
	}

	@Override
	public PersonalDetails updatePersonalDetails(Integer personalId, PersonalDetailsDto personalDetailsDto) {
		Optional<PersonalDetails> existingDetails = personalDetailsRepository.findById(personalId);

		if (!existingDetails.isPresent() || 'N' == existingDetails.get().getStatus()) {
			throw new IllegalArgumentException("Personal details not found for the provided ID.");
		}

		String title = personalDetailsDto.getTitle();
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("Title cannot be empty & should contain only characters");
		}

		String fullName = personalDetailsDto.getFullName();
		if (fullName == null || fullName.trim().isEmpty() || Pattern.matches("[A-Za-z]", fullName)) {
			throw new IllegalArgumentException("Full name cannot be empty & only should contain characters");
		}

		String dateOfBirth = personalDetailsDto.getDateOfBirth();
		if (dateOfBirth == null) {
			throw new IllegalArgumentException("Date of birth cannot be empty.");
		}

		String panNumber = personalDetailsDto.getPanNumber();
		if (panNumber == null || !Pattern.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}", panNumber)) {
			throw new IllegalArgumentException("PAN number must be in the format.");
		}

		String emailId = personalDetailsDto.getEmailId();
		if (emailId == null || !Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", emailId)) {
			throw new IllegalArgumentException("Invalid email format.");
		}

		String mobileNo = personalDetailsDto.getMobileNo();
		if (mobileNo == null || !Pattern.matches("[6-9]\\d{9}", mobileNo)) {
			throw new IllegalArgumentException("Mobile number must be exactly 10 digits & no characters allowed");
		}

		String alternateMobileNo = personalDetailsDto.getAlternateMobileNo();
		if (alternateMobileNo != null && !Pattern.matches("[6-9]\\d{9}", alternateMobileNo)) {
			throw new IllegalArgumentException("Alternate mobile number must be exactly 10 digits");
		}

		String address = personalDetailsDto.getAddress();
		if (address == null || address.trim().isEmpty() || Pattern.matches("[A-Za-z]", address)) {
			throw new IllegalArgumentException("Address cannot be empty & should contain only characters");
		}

		String pincode = personalDetailsDto.getPincode();
		if (pincode == null || !Pattern.matches("4\\d{5}", pincode)) {
			throw new IllegalArgumentException("Pincode must be exactly 6 digits.");
		}

		String city = personalDetailsDto.getCity();
		if (city == null || city.trim().isEmpty() || Pattern.matches("[A-Za-z]", city)) {
			throw new IllegalArgumentException("City cannot be empty & should contain only characters");
		}

		String state = personalDetailsDto.getState();
		if (state == null || state.trim().isEmpty() || Pattern.matches("[A-Za-z]", state)) {
			throw new IllegalArgumentException("State cannot be empty & should contain only characters");
		}

		PersonalDetails existingPD = existingDetails.get();

		// validation for gender dropdown
		String gender = personalDetailsDto.getGender();
		if (gender != null && !gender.isEmpty()) {
			try {
				existingPD.setGender(Gender.valueOf(gender.toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Invalid Gender value: " + gender);
			}
		} else {
			throw new IllegalArgumentException("Invalid Gender value");
		}

		String maritalStatus = personalDetailsDto.getMaritalStatus();
		if (maritalStatus != null && !maritalStatus.isEmpty()) {
			try {

				existingPD.setMaritalStatus(MaritalStatus.valueOf(maritalStatus.toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Invalid Marital Status value: " + maritalStatus);
			}
		} else {
			throw new IllegalArgumentException("Invalid Marital Status value");
		}

		String nationality = personalDetailsDto.getNationality();
		if (nationality != null && !nationality.isEmpty()) {
			try {
				existingPD.setNationality(Nationality.valueOf(nationality.toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Invalid nationality Status value: " + nationality);
			}
		} else {
			throw new IllegalArgumentException("Invalid nationality Status value");
		}

		String occupation = personalDetailsDto.getOccupation();
		if (occupation != null && !occupation.isEmpty()) {
			try {
				existingPD.setOccupation(Occupation.valueOf(occupation.toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Invalid occupation Status value: " + occupation);
			}
		} else {
			throw new IllegalArgumentException("Invalid occupation Status value");
		}

		String genderType = personalDetailsDto.getGender();
		if (genderType != null && !genderType.isEmpty()) {
			Optional<GenderTable> genderTableOptional = genderTableRepository.findByGenderType(genderType);

			if (genderTableOptional.isPresent()) {
				existingPD.setGenderId(genderTableOptional.get().getGenderId());
			} else {
				throw new IllegalArgumentException("Invalid gender value provided");
			}

		} else {
			throw new IllegalArgumentException("Gender cannot be null or empty");
		}

		if (existingDetails.isPresent()) {

			existingPD.setTitle(personalDetailsDto.getTitle());
			existingPD.setFullName(personalDetailsDto.getFullName());
			existingPD.setDateOfBirth(personalDetailsDto.getDateOfBirth());
			existingPD.setPanNumber(personalDetailsDto.getPanNumber());
			existingPD.setEmailId(personalDetailsDto.getEmailId());
			existingPD.setMobileNo(personalDetailsDto.getMobileNo());
			existingPD.setAlternateMobileNo(personalDetailsDto.getAlternateMobileNo());
			existingPD.setAddress(personalDetailsDto.getAddress());
			existingPD.setPincode(personalDetailsDto.getPincode());
			existingPD.setCity(personalDetailsDto.getCity());

			personalDetailsRepository.save(existingPD);
			return existingPD;
		}
		return null;
	}

	@Autowired
	private EntityManager entityManager;

	@Override
//	public List<PersonalDetails> getPersonalDetails(ProposerListing proposerListing) {
	public List<Map<String, Object>> getPersonalDetails(ProposerListing proposerListing) {

		String sortBy = proposerListing.getSortBy();
		String sortOrder = proposerListing.getSortOrder();

		sortBy = (sortBy != null && !sortBy.isEmpty()) ? sortBy : "personalId";
		sortOrder = (sortOrder != null && !sortOrder.isEmpty()) ? sortOrder : "DESC";

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PersonalDetails> criteriaQuery = criteriaBuilder.createQuery(PersonalDetails.class);
		Root<PersonalDetails> root = criteriaQuery.from(PersonalDetails.class);
		List<Predicate> predicates = new ArrayList<>();

		criteriaQuery.orderBy("ASC".equalsIgnoreCase(sortOrder) ? criteriaBuilder.asc(root.get(sortBy))
				: criteriaBuilder.desc(root.get(sortBy)));

		if (proposerListing.getSearchFilters() != null && proposerListing.getSearchFilters().size() > 0) {
			List<SearchFilter> filters = proposerListing.getSearchFilters();

//			for (SearchFilter filter : filters) {
			SearchFilter filter = filters.get(0);
			String fullName = filter.getFullName();
			String emailId = filter.getEmailId();
			String city = filter.getCity();
			Character status = filter.getStatus();

			if (fullName != null && !fullName.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("fullName"), fullName));
			}
			if (emailId != null && !emailId.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("emailId"), emailId));
			}
			if (city != null && !city.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("city"), city));
			}
			if (status != null) {
				predicates.add(criteriaBuilder.equal(root.get("status"), status));
			} else {
				predicates.add(criteriaBuilder.equal(root.get("status"), 'Y'));
			}
		}
//		}

		if (!predicates.isEmpty()) {
			criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		}
		TypedQuery<PersonalDetails> typedQuery = entityManager.createQuery(criteriaQuery);

		if (proposerListing.getPage() > 0 && proposerListing.getSize() > 0) {

			Integer page = proposerListing.getPage();
			Integer size = proposerListing.getSize();

			typedQuery.setFirstResult((page - 1) * size);
			typedQuery.setMaxResults(size);
//			return typedQuery.getResultList();
		}
		List<PersonalDetails> resultList = typedQuery.getResultList();
		int totalSize = resultList.size();
		totalRecord = totalSize;

		List<Map<String, Object>> result = new ArrayList<>();

		for (PersonalDetails pd : resultList) {
			Map<String, Object> map = new HashMap<>();
			map.put("fullName", pd.getFullName());
			map.put("emailId", pd.getEmailId());
			map.put("city", pd.getCity());

			result.add(map);
		}

//		return typedQuery.getResultList();
		return result;

	}

//	@Override
//	public void exportPersonalDetailsToExcel(HttpServletResponse response) throws IOException {
//		List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
//
//		XSSFWorkbook workbook = new XSSFWorkbook();
//
//		XSSFSheet sheet = workbook.createSheet("PersonalDetails");
//
//		Row headerRow = sheet.createRow(0);
//		String[] headers = {"Title", "Full Name", "Date of Birth", "PAN Number", "Gender",
//				"Marital Status", "Nationality", "Occupation", "Email ID", "Mobile No", "Alternate Mobile No",
//				"Address", "Pincode", "City", "State", "Status", "Created At", "Updated At", "Gender ID" };
//
//		for (int i = 0; i < headers.length; i++) {
//			headerRow.createCell(i).setCellValue(headers[i]);
//		}
//
//		int rowNum = 1;
//		for (PersonalDetails personalDetails : personalDetailsList) {
//			Row row = sheet.createRow(rowNum++);
////			row.createCell(0).setCellValue(personalDetails.getPersonalId());
//			row.createCell(0).setCellValue(personalDetails.getTitle());
//			row.createCell(1).setCellValue(personalDetails.getFullName());
//			row.createCell(2).setCellValue(
//					personalDetails.getDateOfBirth() != null ? personalDetails.getDateOfBirth().toString() : "");
//			row.createCell(3).setCellValue(personalDetails.getPanNumber());
//			row.createCell(4)
//					.setCellValue(personalDetails.getGender() != null ? personalDetails.getGender().toString() : "");
//			row.createCell(5).setCellValue(
//					personalDetails.getMaritalStatus() != null ? personalDetails.getMaritalStatus().toString() : "");
//			row.createCell(6).setCellValue(
//					personalDetails.getNationality() != null ? personalDetails.getNationality().toString() : "");
//			row.createCell(7).setCellValue(
//					personalDetails.getOccupation() != null ? personalDetails.getOccupation().toString() : "");
//			row.createCell(8).setCellValue(personalDetails.getEmailId());
//			row.createCell(9).setCellValue(personalDetails.getMobileNo());
//			row.createCell(10).setCellValue(personalDetails.getAlternateMobileNo());
//			row.createCell(11).setCellValue(personalDetails.getAddress());
//			row.createCell(12).setCellValue(personalDetails.getPincode());
//			row.createCell(13).setCellValue(personalDetails.getCity());
//			row.createCell(14).setCellValue(personalDetails.getState());
//			row.createCell(15)
//					.setCellValue(personalDetails.getStatus() != null ? personalDetails.getStatus().toString() : "");
//			row.createCell(16).setCellValue(
//					personalDetails.getCreatedAt() != null ? personalDetails.getCreatedAt().toString() : "");
//			row.createCell(17).setCellValue(
//					personalDetails.getUpdatedAt() != null ? personalDetails.getUpdatedAt().toString() : "");
//			row.createCell(18).setCellValue(personalDetails.getGenderId() != null ? personalDetails.getGenderId() : 0);
//		}
//
//		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//		response.setHeader("Content-Disposition", "attachment; filename=personal_details.xlsx");
//
//		workbook.write(response.getOutputStream());
//		workbook.close();
//	}
	@Override
//	@Scheduled(fixedRate = 5000)
	public void exportPersonalDetailsToExcel() throws IOException {
		List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("PersonalDetails");

		Row headerRow = sheet.createRow(0);
		String[] headers = { "Title", "Full Name", "Date of Birth", "PAN Number", "Gender", "Marital Status",
				"Nationality", "Occupation", "Email ID", "Mobile No", "Alternate Mobile No", "Address", "Pincode",
				"City", "State", "Status", "Created At", "Updated At", "Gender ID" };

		for (int i = 0; i < headers.length; i++) {
			headerRow.createCell(i).setCellValue(headers[i]);
		}

		int rowNum = 1;
		for (PersonalDetails pd : personalDetailsList) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(pd.getTitle());
			row.createCell(1).setCellValue(pd.getFullName());
			row.createCell(2).setCellValue(pd.getDateOfBirth() != null ? pd.getDateOfBirth().toString() : "");
			row.createCell(3).setCellValue(pd.getPanNumber());
			row.createCell(4).setCellValue(pd.getGender() != null ? pd.getGender().toString() : "");
			row.createCell(5).setCellValue(pd.getMaritalStatus() != null ? pd.getMaritalStatus().toString() : "");
			row.createCell(6).setCellValue(pd.getNationality() != null ? pd.getNationality().toString() : "");
			row.createCell(7).setCellValue(pd.getOccupation() != null ? pd.getOccupation().toString() : "");
			row.createCell(8).setCellValue(pd.getEmailId());
			row.createCell(9).setCellValue(pd.getMobileNo());
			row.createCell(10).setCellValue(pd.getAlternateMobileNo());
			row.createCell(11).setCellValue(pd.getAddress());
			row.createCell(12).setCellValue(pd.getPincode());
			row.createCell(13).setCellValue(pd.getCity());
			row.createCell(14).setCellValue(pd.getState());
			row.createCell(15).setCellValue(pd.getStatus() != null ? pd.getStatus().toString() : "");
			row.createCell(16).setCellValue(pd.getCreatedAt() != null ? pd.getCreatedAt().toString() : "");
			row.createCell(17).setCellValue(pd.getUpdatedAt() != null ? pd.getUpdatedAt().toString() : "");
			row.createCell(18).setCellValue(pd.getGenderId() != null ? pd.getGenderId() : 0);
		}

		String fileName = "personal_details_" + System.currentTimeMillis() + ".xlsx";
		FileOutputStream out = new FileOutputStream("C:\\download\\sample_" + fileName);
		workbook.write(out);
		workbook.close();
		out.close();

		System.out.println("Excel exported " + fileName);
	}

	@Override
	public String exportSamplePersonalDetailsToExcel() throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("PersonalDetails");

//		List<String> headers = Arrays.asList("Personal ID", "Title", "Full Name", "Date of Birth", "PAN Number",
//				"Gender", "Marital Status", "Nationality", "Occupation", "Email ID", "Mobile No", "Alternate Mobile No",
//				"Address", "Pincode", "City", "State", "Status", "Created At", "Updated At", "Gender ID");
		List<String> headers = Arrays.asList("Title", "Full Name*", "Date of Birth*", "PAN Number*", "Gender*",
				"Marital Status", "Nationality", "Occupation", "Email ID*", "Mobile No*", "Alternate Mobile No",
				"Address*", "Pincode*", "City*", "State*", "Status", "Created At", "Updated At", "Gender ID");

		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			headerRow.createCell(i).setCellValue(headers.get(i));
		}

		String uid = UUID.randomUUID().toString().replace("-", "");
		String filepath = "C:\\download\\sample_personal_details_" + uid + ".xlsx";
		try (FileOutputStream out = new FileOutputStream(filepath)) {
			workbook.write(out);
		}
		workbook.close();
		return filepath;
	}

	@Override
	public List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile file, Map<String, Integer> recordCount)
			throws IOException {
		List<PersonalDetails> savedExcelList = new ArrayList<>();

		recordCount.put("totalExcelCount", 0);
		recordCount.put("errorExcelCount", 0);

		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				ResponseExcel response = new ResponseExcel();
				XSSFRow row = sheet.getRow(i);
//				if (row == null) {
//					continue;
//				}
				boolean isEmptyRow = true;
				for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
					XSSFCell cell = row.getCell(j);
					if (cell != null && cell.getCellType() != CellType.BLANK
							&& (cell.getCellType() != CellType.STRING || !cell.getStringCellValue().trim().isEmpty())) {
						isEmptyRow = false;
						break;
					}
				}

				if (isEmptyRow)
					continue;

				recordCount.put("totalExcelCount", recordCount.get("totalExcelCount") + 1);

				PersonalDetails entity = new PersonalDetails();

				String title = getCellString(row.getCell(0));

				if (title != null && !title.trim().isEmpty()) {
					entity.setTitle(title);
					// continue;
				}

				String fullName = getCellString(row.getCell(1));
				if (fullName == null || fullName.trim().isEmpty() || !fullName.matches("[a-zA-Z\\s]+")) {
					response.setErrorField("fullName");
					response.setStatus(false);
					response.setUpdateMessage("Invalid full name");
					response.setUpdateMessage(fullName);
					responseExcelRepository.save(response);
					recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);
					continue;
				} else {
					entity.setFullName(fullName);
				}

				String dob = getCellString(row.getCell(2));
				if (dob == null || dob.trim().isEmpty()) {
					response.setErrorField("dateOfBirth");
					response.setStatus(false);
					response.setError("Invalid Date Of Birth");
					response.setUpdateMessage("Failure!!");
					responseExcelRepository.save(response);
					recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);

					continue;
				} else {
					entity.setDateOfBirth(dob);
				}

				String pan = getCellString(row.getCell(3)).toUpperCase().trim();
				if (!pan.matches("^[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}$")) {
					response.setErrorField("panNumber");
					response.setStatus(false);
					response.setError("Invalid Pan Number");
					response.setUpdateMessage("Failure!!");
					responseExcelRepository.save(response);
					recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);

					continue;
				} else {
					entity.setPanNumber(pan);
				}

				String genderString = getCellString(row.getCell(4)).toUpperCase();
				Gender gender = null;
				for (Gender g : Gender.values()) {
					if (g.name().equalsIgnoreCase(genderString)) {
						gender = g;
						break;
					}
				}
				if (gender == null) {
					response.setErrorField("gender");
					response.setStatus(false);
					response.setError("Invalid Gender");
					response.setUpdateMessage("Failure!!");
					responseExcelRepository.save(response);
					recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);

					continue;
				} else {
					entity.setGender(gender);
					entity.setGenderId(getGenderId(gender));
				}

				String maritalStatusStr = getCellString(row.getCell(5)).toUpperCase();
				MaritalStatus maritalStatus = null;
				for (MaritalStatus m : MaritalStatus.values()) {
					if (m.name().equalsIgnoreCase(maritalStatusStr)) {
						maritalStatus = m;
						break;
					}
				}
				if (maritalStatus != null) {
					entity.setMaritalStatus(maritalStatus);
				}
//					response.setErrorField("maritalStatus");
//					response.setStatus(false);
//					response.setError("Invalid Marital Status");
//					response.setUpdateMessage("Failure!!");
//					responseExcelRepository.save(response);
//					continue;
//				} else {
//					entity.setMaritalStatus(maritalStatus);
//				}

				String nationalityStr = getCellString(row.getCell(6)).toUpperCase();
				Nationality nationality = null;
				for (Nationality n : Nationality.values()) {
					if (n.name().equalsIgnoreCase(nationalityStr)) {
						nationality = n;
						break;
					}
				}
				if (nationality != null) {
					entity.setNationality(nationality);

				}
//					response.setErrorField("nationality");
//					response.setStatus(false);
//					response.setError("Invalid Nationality");
//					response.setUpdateMessage("Failure!!");
//					responseExcelRepository.save(response);
//					entity.setNationality(nationality);
//					continue;
//				} else {
//					entity.setNationality(nationality);
//				}

				String occupationStr = getCellString(row.getCell(7)).toUpperCase();
				Occupation occupation = null;
				for (Occupation o : Occupation.values()) {
					if (o.name().equalsIgnoreCase(occupationStr)) {
						occupation = o;
						break;
					}
				}
				if (occupation != null) {
					entity.setOccupation(occupation);
				}
//					response.setErrorField("occupation");
//					response.setStatus(false);
//					response.setError("Invalid Occupation");
//					response.setUpdateMessage("Failure!!");
//					responseExcelRepository.save(response);
//					continue;
//				} else {
//					entity.setOccupation(occupation);
//				}

				String email = getCellString(row.getCell(8));
				if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
					response.setErrorField("email");
					response.setStatus(false);
					response.setError("Invalid Email");
					response.setUpdateMessage("Failure!!");
					responseExcelRepository.save(response);
					recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);

					continue;
				} else {
					entity.setEmailId(email);
				}

				String mobileNumber = getCellString(row.getCell(9));
				if (mobileNumber == null || mobileNumber.isEmpty() || !mobileNumber.matches("^[6-9]\\d{9}$")) {
					response.setErrorField("mobileNumber");
					response.setStatus(false);
					response.setError("Invalid Mobile Number");
					response.setUpdateMessage("Failure!!");
					responseExcelRepository.save(response);
					recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);
					continue;
				} else {
					entity.setMobileNo(mobileNumber);
				}

				String alternateMobile = getCellString(row.getCell(10));
				if (alternateMobile != null && !alternateMobile.trim().isEmpty()
						&& alternateMobile.matches("^[6-9]\\d{9}$")) {
					entity.setAlternateMobileNo(alternateMobile);
					// continue;

				}

				String address = getCellString(row.getCell(11));
				if (address == null || address.trim().isEmpty()) {
					response.setErrorField("address");
					response.setStatus(false);
					response.setError("Invalid address");
					response.setUpdateMessage("Failure!!");
					responseExcelRepository.save(response);
					recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);
					continue;
				}
				entity.setAddress(address);

				String pincode = getCellString(row.getCell(12));
				if (pincode == null || !pincode.matches("^\\d{6}$")) {
					response.setErrorField("pincode");
					response.setStatus(false);
					response.setError("Invalid Pincode");
					response.setUpdateMessage("Failure!!");
					responseExcelRepository.save(response);
					recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);
					continue;
				}
				entity.setPincode(pincode);

				String city = getCellString(row.getCell(13));
				if (city == null || city.trim().isEmpty()) {
					response.setErrorField("city");
					response.setStatus(false);
					response.setError("Invalid City");
					response.setUpdateMessage("Failure!!");
					responseExcelRepository.save(response);
					recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);
					continue;
				} else {
					entity.setCity(city);
				}

				String state = getCellString(row.getCell(14));
				if (state == null || state.trim().isEmpty()) {
					response.setErrorField("state");
					response.setStatus(false);
					response.setError("Invalid State");
					response.setUpdateMessage("Failure!!");
					responseExcelRepository.save(response);
					recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);
					continue;
				} else {
					entity.setState(state);
				}

				entity.setStatus('Y');

				PersonalDetails saved = personalDetailsRepository.save(entity);
				response.setErrorField(String.valueOf(saved.getPersonalId()));
				response.setStatus(true);
				response.setError("No Error");
				response.setUpdateMessage("Success!!");
				responseExcelRepository.save(response);
				savedExcelList.add(saved);
			}
		}

		return savedExcelList;
	}

	private Integer getGenderId(Gender gender) {
		switch (gender) {
		case MALE:
			return 1;
		case FEMALE:
			return 2;
		case OTHER:
			return 3;
		default:
			return null;
		}
	}

	private String getCellString(Cell cell) {
		if (cell == null) {
			return "";
		}

		if (cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue().trim();
		} else if (cell.getCellType() == CellType.NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(cell.getDateCellValue());
			} else {
				return String.valueOf((long) cell.getNumericCellValue());
			}
		} else if (cell.getCellType() == CellType.BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == CellType.FORMULA) {
			return cell.getCellFormula();
		} else {
			return "";
		}
	}

	@Autowired
	private RestTemplate restTemplate;

	private static final String PRODUCT_API_URL = "https://fakestoreapi.com/products";

//	private static final String PRODUCT_API_URL = "https://dummyjson.com/products";
	@Override
	public List<Map<String, Object>> getAllProducts() {
		return restTemplate.getForObject(PRODUCT_API_URL, List.class);
	}

	@Override
	public List<PersonalDetails> importScheduleDetailsFromExcel(MultipartFile file, Map<String, Integer> recordCount)
			throws IOException {

		List<PersonalDetails> savedExcelList = new ArrayList<>();
		recordCount.put("totalExcelCount", 0);
		recordCount.put("errorExcelCount", 0);
		int validRecords = 0;

		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getLastRowNum();

			for (int i = 1; i <= totalRows; i++) {
				ResponseExcel2 response = new ResponseExcel2();
				XSSFRow row = sheet.getRow(i);
				// if (row == null) {
				// continue;
				// }
				boolean isEmptyRow = true;
				List<String> errorFields = new ArrayList<>();

				for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
					XSSFCell cell = row.getCell(j);
					if (cell != null && cell.getCellType() != CellType.BLANK
							&& (cell.getCellType() != CellType.STRING || !cell.getStringCellValue().trim().isEmpty())) {
						isEmptyRow = false;
						break;
					}
				}

				if (isEmptyRow)
					continue;

				recordCount.put("totalExcelCount", recordCount.get("totalExcelCount") + 1);

				PersonalDetails entity = new PersonalDetails();

				String title = getCellString(row.getCell(0));

				if (title != null && !title.trim().isEmpty()) {
					entity.setTitle(title);
					// continue;
				}

				String fullName = getCellString(row.getCell(1));
				if (fullName == null || fullName.trim().isEmpty() || !fullName.matches("[a-zA-Z\\s]+")) {
					errorFields.add("fullName");
				} else {
					entity.setFullName(fullName);
				}

				String dob = getCellString(row.getCell(2));
				if (dob == null || dob.trim().isEmpty()) {
					errorFields.add("dateOfBirth");
				} else {
					entity.setDateOfBirth(dob);
				}

				String pan = getCellString(row.getCell(3)).toUpperCase().trim();
				if (!pan.matches("^[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}$")) {
					errorFields.add("panNumber");

				} else {
					entity.setPanNumber(pan);
				}

				String genderString = getCellString(row.getCell(4)).toUpperCase();
				Gender gender = null;
				for (Gender g : Gender.values()) {
					if (g.name().equalsIgnoreCase(genderString)) {
						gender = g;
						break;
					}
				}
				if (gender == null) {
					errorFields.add("gender");

				} else {
					entity.setGender(gender);
					entity.setGenderId(getGenderId(gender));
				}

				String maritalStatusStr = getCellString(row.getCell(5)).toUpperCase();
				MaritalStatus maritalStatus = null;
				for (MaritalStatus m : MaritalStatus.values()) {
					if (m.name().equalsIgnoreCase(maritalStatusStr)) {
						maritalStatus = m;
						break;
					}
				}
				if (maritalStatus != null) {
					entity.setMaritalStatus(maritalStatus);
				}

				String nationalityStr = getCellString(row.getCell(6)).toUpperCase();
				Nationality nationality = null;
				for (Nationality n : Nationality.values()) {
					if (n.name().equalsIgnoreCase(nationalityStr)) {
						nationality = n;
						break;
					}
				}
				if (nationality != null) {
					entity.setNationality(nationality);

				}

				String occupationStr = getCellString(row.getCell(7)).toUpperCase();
				Occupation occupation = null;
				for (Occupation o : Occupation.values()) {
					if (o.name().equalsIgnoreCase(occupationStr)) {
						occupation = o;
						break;
					}
				}
				if (occupation != null) {
					entity.setOccupation(occupation);
				}

				String email = getCellString(row.getCell(8));
				if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
					errorFields.add("email");

				} else {
					entity.setEmailId(email);
				}

				String mobileNumber = getCellString(row.getCell(9));
				if (mobileNumber == null || mobileNumber.isEmpty() || !mobileNumber.matches("^[6-9]\\d{9}$")) {
					errorFields.add("mobileNumber");

				} else {
					entity.setMobileNo(mobileNumber);
				}

				String alternateMobile = getCellString(row.getCell(10));
				if (alternateMobile != null && !alternateMobile.trim().isEmpty()
						&& alternateMobile.matches("^[6-9]\\d{9}$")) {
					entity.setAlternateMobileNo(alternateMobile);
					// continue;

				}

				String address = getCellString(row.getCell(11));
				if (address == null || address.trim().isEmpty()) {
					errorFields.add("address");

				}
				entity.setAddress(address);

				String pincode = getCellString(row.getCell(12));
				if (pincode == null || !pincode.matches("^\\d{6}$")) {
					errorFields.add("pincode");

				}
				entity.setPincode(pincode);

				String city = getCellString(row.getCell(13));
				if (city == null || city.trim().isEmpty()) {
					errorFields.add("city");

				} else {
					entity.setCity(city);
				}

				String state = getCellString(row.getCell(14));
				if (state == null || state.trim().isEmpty()) {
					errorFields.add("state");

				} else {
					entity.setState(state);
				}

				entity.setStatus('Y');
				if (!errorFields.isEmpty()) {
					response.setErrorField(String.join(", ", errorFields));
					response.setError("Invalid Mandatory fields");
					response.setStatus(false);
					response.setUpdateMessage("Failure!!");
					responseExcel2Repository.save(response);
					recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);
					continue;
				}

				PersonalDetails saved = personalDetailsRepository.save(entity);
				savedExcelList.add(saved);
				validRecords++;
			}
		}
		if (validRecords >= 5) {
			QueueTable queue = new QueueTable();
			queue.setFilepath(file.getOriginalFilename());
			queue.setRowCount(recordCount.get("totalExcelCount"));
			queue.setRowRead(validRecords);
			queue.setIsProcessed('N');
			queue.setStatus('Y');
			queueTableRepository.save(queue);
			
			queue.setIsProcessed('Y');
			queueTableRepository.save(queue);
		} else {
			throw new IllegalArgumentException("Minimum 5 valid records required to process the file.");
		}
		return savedExcelList;
	}

	@Scheduled(fixedRate = 120000)
	public void processPendingRecords() throws Exception {
		System.out.println("Scheduler running at: " + LocalDateTime.now());
		List<QueueTable> pendingQueueEntries = queueTableRepository.findByIsProcessed('N');

		if (pendingQueueEntries.isEmpty()) {
			return;
		}

		for (QueueTable queue : pendingQueueEntries) {
			File file = new File(queue.getFilepath());

			// Skip non-existing files
			if (!file.exists()) {
				continue;
			}

			Map<String, Integer> recordCounts = new HashMap<>();
			FileInputStream fileInputStream = new FileInputStream(file);
			MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "application/vnd.ms-excel",
					fileInputStream);

			List<PersonalDetails> savedDetails = importPersonalDetailsFromExcel(multipartFile, recordCounts);

			if (!savedDetails.isEmpty()) {
				queue.setIsProcessed('Y');
				queue.setRowRead(recordCounts.get("totalExcelCount"));
				queue.setStatus('Y');
			} else {
				queue.setStatus('N');
			}

			queueTableRepository.save(queue);

			fileInputStream.close();
		}
	}

}
