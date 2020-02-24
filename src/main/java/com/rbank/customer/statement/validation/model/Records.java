package com.rbank.customer.statement.validation.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Michael Philomin Raj
 *
 */

@XmlRootElement(name = "records")
public class Records
{
	private RecordDetail[] recordDetails;

	/**
	 * @return the recordDetails
	 */
	public RecordDetail[] getRecordDetails() {
		return recordDetails;
	}

	/**
	 * @param recordDetails the recordDetails to set
	 */
	@XmlElement(name = "record")
	public void setRecordDetails(RecordDetail[] recordDetails) {
		this.recordDetails = recordDetails;
	}

	
}
