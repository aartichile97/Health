package com.example.practice.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "person_info")
public class PersonalDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer personalId;

	@Column(name = "title")
	private String title;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "pan_number")
	private String panNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;

	@Enumerated(EnumType.STRING)
	@Column(name = "marital_status")
	private MaritalStatus maritalStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "nationality")
	private Nationality nationality;

	@Enumerated(EnumType.STRING)
	@Column(name = "occupation")
	private Occupation occupation;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "alternate_mobile_no")
	private String alternateMobileNo;

	@Column(name = "address")
	private String address;

	@Column(name = "pincode")
	private String pincode;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	 
    @Column(name = "gender_id") 
	private Integer genderId;

	public PersonalDetails(Integer personalId, String title, String fullName, LocalDate dateOfBirth, String panNumber,
			Gender gender, MaritalStatus maritalStatus, Nationality nationality, Occupation occupation, String emailId,
			String mobileNo, String alternateMobileNo, String address, String pincode, String city, String state,
			Character status, LocalDateTime createdAt, LocalDateTime updatedAt, Integer  genderId) {
		super();
		this.personalId = personalId;
		this.title = title;
		this.fullName = fullName;
		this.dateOfBirth = dateOfBirth;
		this.panNumber = panNumber;
		this.gender = gender;
		this.maritalStatus = maritalStatus;
		this.nationality = nationality;
		this.occupation = occupation;
		this.emailId = emailId;
		this.mobileNo = mobileNo;
		this.alternateMobileNo = alternateMobileNo;
		this.address = address;
		this.pincode = pincode;
		this.city = city;
		this.state = state;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.genderId = genderId;
	}

	public Integer getPersonalId() {
		return personalId;
	}

	public void setPersonalId(Integer id) {
		this.personalId = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public Nationality getNationality() {
		return nationality;
	}

	public void setNationality(Nationality nationality) {
		this.nationality = nationality;
	}

	public Occupation getOccupation() {
		return occupation;
	}

	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAlternateMobileNo() {
		return alternateMobileNo;
	}

	public void setAlternateMobileNo(String alternateMobileNo) {
		this.alternateMobileNo = alternateMobileNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public PersonalDetails() {
	}

}
