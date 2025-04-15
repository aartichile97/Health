package com.example.practice.response;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ResponseExcel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "response_id")
	private Integer responseId;

	@Column(name = "status")
	private Boolean status;// true or false

	@Column(name = "error")
	private String error;// actual error msg

	@Column(name = "update_message")
	private String updateMessage;// success or failed msg

	@Column(name = "error_field")
	private String errorField;// if failed-field name, success- primary key Id

	public ResponseExcel() {
	}

	public ResponseExcel(Integer responseId, Boolean status, String error, String updateMessage, String errorField) {
		super();
		this.responseId = responseId;
		this.status = status;
		this.error = error;
		this.updateMessage = updateMessage;
		this.errorField = errorField;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getUpdateMessage() {
		return updateMessage;
	}

	public void setUpdateMessage(String updateMessage) {
		this.updateMessage = updateMessage;
	}

	public String getErrorField() {
		return errorField;
	}

	public void setErrorField(String errorField) {
		this.errorField = errorField;
	}

}
