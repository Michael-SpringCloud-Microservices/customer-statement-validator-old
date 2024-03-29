package com.rbank.customer.statement.validation.exception;


/**
 * @author Michael Philomin Raj
 *
 */

public class FileSizeNotAllowedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final int statusCode;

	public FileSizeNotAllowedException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;		
	}


	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}	
}

