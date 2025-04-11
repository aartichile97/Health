package com.example.practice.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.practice.dto.PersonalDetailsDto;
import com.example.practice.entity.Gender;
import com.example.practice.entity.GenderTable;
import com.example.practice.entity.MaritalStatus;
import com.example.practice.entity.Nationality;
import com.example.practice.entity.Occupation;
import com.example.practice.entity.PersonalDetails;
import com.example.practice.listing.ProposerListing;
import com.example.practice.listing.SearchFilter;
import com.example.practice.repository.GenderTableRepository;
import com.example.practice.repository.PersonalDetailsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

@Service
public class PersonalDetailsServiceImpl implements PersonalDetailsService {

	Integer totalRecord = 0;

	@Override
	public Integer totalRecords() {

		return totalRecord;
	}

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	@Autowired
	private GenderTableRepository genderTableRepository;

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

		LocalDate dateOfBirth = personalDetailsDto.getDateOfBirth();
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

		LocalDate dateOfBirth = personalDetailsDto.getDateOfBirth();
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
	public List<PersonalDetails> getPersonalDetails(ProposerListing proposerListing) {

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
			return typedQuery.getResultList();
		}
		List<PersonalDetails> resultList = typedQuery.getResultList();
		int totalSize = resultList.size();
		totalRecord = totalSize;
		return typedQuery.getResultList();
	}

	@Override
	public String exportPersonalDetailsToExcel() throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("PersonalDetails");

		List<String> headers = Arrays.asList("Personal ID", "Title", "Full Name", "Date of Birth", "PAN Number",
				"Gender", "Marital Status", "Nationality", "Occupation", "Email ID", "Mobile No", "Alternate Mobile No",
				"Address", "Pincode", "City", "State", "Status", "Created At", "Updated At", "Gender ID");

		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			headerRow.createCell(i).setCellValue(headers.get(i));
		}

//		String filepath = "C:\\download\\sample_personal_details.xlsx";
		String uid = UUID.randomUUID().toString().replace("-", "");
		String filepath = "C:\\download\\sample_personal_details_" + uid + ".xlsx";
		try (FileOutputStream out = new FileOutputStream(filepath)) {
			workbook.write(out);
		}
		workbook.close();
		return filepath;
	}

//	@Override
//	public List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile file) throws IOException {
//
//		List<PersonalDetails> savedExcelList = new ArrayList<>();
//		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//				XSSFRow row = sheet.getRow(i);
//				if (row == null)
//					continue;
//
//				PersonalDetailsDto dto = new PersonalDetailsDto();
//				dto.setTitle(getCellString(row.getCell(0)));
//				dto.setFullName(getCellString(row.getCell(1)));
//
//				Cell dobCell = row.getCell(2);
//				if (dobCell != null && dobCell.getCellType() == CellType.NUMERIC
//						&& DateUtil.isCellDateFormatted(dobCell)) {
//					dto.setDateOfBirth(dobCell.getLocalDateTimeCellValue().toLocalDate());
//				}
//
//				dto.setPanNumber(getCellString(row.getCell(3)));
//				dto.setGender(getCellString(row.getCell(4)));
//				dto.setMaritalStatus(getCellString(row.getCell(5)));
//				dto.setNationality(getCellString(row.getCell(6)));
//				dto.setOccupation(getCellString(row.getCell(7)));
//				dto.setEmailId(getCellString(row.getCell(8)));
//				dto.setMobileNo(getCellString(row.getCell(9)));
//				dto.setAlternateMobileNo(getCellString(row.getCell(10)));
//				dto.setAddress(getCellString(row.getCell(11)));
//				dto.setPincode(getCellString(row.getCell(12)));
//				dto.setCity(getCellString(row.getCell(13)));
//				dto.setState(getCellString(row.getCell(14)));
//
//				PersonalDetails saveDetails = savePersonalDetails(dto);
//				savedExcelList.add(saveDetails);
//				
//				
//			}
//		}
//		return savedExcelList;
//	}

	@Override
	public List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile file) throws IOException {

		List<PersonalDetails> savedExcelList = new ArrayList<>();

		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				if (row == null)
					continue;

				PersonalDetailsDto dto = new PersonalDetailsDto();

				// Validate Title
				String title = getCellString(row.getCell(0)).trim();
				if (title.isEmpty() || !title.matches("^[a-zA-Z. ]+$")) {
					System.out.println("❌ Skipping Row " + i + ": Invalid Title = " + title);
					continue;
				}
				dto.setTitle(title);

				dto.setFullName(getCellString(row.getCell(1)));

				Cell dobCell = row.getCell(2);
				if (dobCell != null && dobCell.getCellType() == CellType.NUMERIC
						&& DateUtil.isCellDateFormatted(dobCell)) {
					dto.setDateOfBirth(dobCell.getLocalDateTimeCellValue().toLocalDate());
				}

				dto.setPanNumber(getCellString(row.getCell(3)));
				dto.setGender(getCellString(row.getCell(4)));
				dto.setMaritalStatus(getCellString(row.getCell(5)));
				dto.setNationality(getCellString(row.getCell(6)));
				dto.setOccupation(getCellString(row.getCell(7)));
				dto.setEmailId(getCellString(row.getCell(8)));
				dto.setMobileNo(getCellString(row.getCell(9)));
				dto.setAlternateMobileNo(getCellString(row.getCell(10)));
				dto.setAddress(getCellString(row.getCell(11)));
				dto.setPincode(getCellString(row.getCell(12)));
				dto.setCity(getCellString(row.getCell(13)));
				dto.setState(getCellString(row.getCell(14)));

				try {
					PersonalDetails saveDetails = savePersonalDetails(dto);
					savedExcelList.add(saveDetails);
				} catch (IllegalArgumentException e) {
					System.out.println("❌ Error saving row " + i + ": " + e.getMessage());
				}
			}
		}
		return savedExcelList;
	}

	private String getCellString(Cell cell) {
		if (cell == null)
			return "";
		if (cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.NUMERIC && !DateUtil.isCellDateFormatted(cell)) {
			return String.valueOf((long) cell.getNumericCellValue());
		} else if (cell.getCellType() == CellType.BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else {
			return "";
		}

	}

}
