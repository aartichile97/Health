package com.example.practice.response;

public class ResponseHandler {

	private Boolean status;
	private long totalRecords;
	private Object data;
	private String message;

	public ResponseHandler() {
		super();
	}

	public ResponseHandler(Boolean status, long totalRecords, Object data, String message) {
		super();
		this.status = status;
		this.totalRecords = totalRecords;
		this.data = data;
		this.message = message;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

}
