package com.example.practice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "queue_table")
public class QueueTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "queue_id")
	private Integer queueId;

	@Column(name = "row_read")
	private Integer rowRead;

	@Column(name = "row_count")
	private Integer rowCount;

	@Column(name = "is_processed")
	private Character isProcessed;

	@Column(name = "status")
	private Character status;

	@Column(name = "filepath")
	private String filepath;

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public QueueTable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getQueueId() {
		return queueId;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

	public Integer getRowRead() {
		return rowRead;
	}

	public void setRowRead(Integer rowRead) {
		this.rowRead = rowRead;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public Character getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(Character isProcessed) {
		this.isProcessed = isProcessed;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

}
