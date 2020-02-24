package com.rbank.customer.statement.validation.response;

import java.util.Date;
import java.util.List;

import com.rbank.customer.statement.validation.model.RecordDetail;

/**
 * @author Michael Philomin Raj
 *
 */

public class ValidationResult extends BaseResult {


	private List<RecordDetail> failedRecords;

	public ValidationResult(){

	}

	/**
	 * @param timestamp
	 * @param message
	 * @param reports
	 */
	public ValidationResult(Date timestamp, String message, List<RecordDetail> failedRecords) {
		super(timestamp,message);
		this.failedRecords = failedRecords;
	}

	/**
	 * @return the failedRecords
	 */
	public List<RecordDetail> getFailedRecords() {
		return failedRecords;
	}

	/**
	 * @param failedRecords the failedRecords to set
	 */
	public void setFailedRecords(List<RecordDetail> failedRecords) {
		this.failedRecords = failedRecords;
	}
}
