package com.rbank.customer.statement.validation.handler;


import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rbank.customer.statement.validation.exception.EmptyFileException;
import com.rbank.customer.statement.validation.exception.FileParsingException;
import com.rbank.customer.statement.validation.exception.FileSizeNotAllowedException;
import com.rbank.customer.statement.validation.exception.InvalidDataException;
import com.rbank.customer.statement.validation.exception.UnsupportedFileFormatException;
import com.rbank.customer.statement.validation.response.BaseResult;
import com.rbank.customer.statement.validation.response.ValidationResult;

/**
 * @author Michael Philomin Raj
 *
 */

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(InvalidDataException.class)
	public final ResponseEntity<ValidationResult> handleInvalidDataException(InvalidDataException ex, WebRequest request){
		log.error("The given file validation is failed as it has invalid data : ", ex);
		ValidationResult validationResult = new ValidationResult(new Date(), 
				ex.getMessage(), ex.getFailedRecords());		
		return ResponseEntity.status(ex.getStatusCode()).body(validationResult);
	}

	@ExceptionHandler(UnsupportedFileFormatException.class)
	public final ResponseEntity<BaseResult> handleUnsupportedFileFormatException(UnsupportedFileFormatException ex, WebRequest request){
		log.error("The given file validation is failed as the format or type is not supported : ", ex);
		BaseResult validationResult = new BaseResult(new Date(), 
				ex.getMessage());
		return ResponseEntity.status(ex.getStatusCode()).body(validationResult);		
	}

	@ExceptionHandler(EmptyFileException.class)
	public final ResponseEntity<BaseResult> handleAllExceptions(EmptyFileException ex, WebRequest request){
		log.error("The given file validation is failed as the file is empty : ", ex);		
		BaseResult validationResult = new BaseResult(new Date(), 
				ex.getMessage());
		return ResponseEntity.status(ex.getStatusCode()).body(validationResult);
	}

	@ExceptionHandler(FileParsingException.class)
	public final ResponseEntity<BaseResult> handleFileParsingException(FileParsingException ex, WebRequest request){
		log.error("The given file validation is failed as the given file is not parsable : ", ex);
		BaseResult validationResult = new BaseResult(new Date(), 
				ex.getMessage());
		return ResponseEntity.status(ex.getStatusCode()).body(validationResult);
	}

	@ExceptionHandler(IOException.class)
	public final ResponseEntity<BaseResult> handleIOException(IOException ex, WebRequest request){
		log.error("The given file validation is failed due to IO exception : ", ex);
		BaseResult validationResult = new BaseResult(new Date(), 
				ex.getMessage());
		return new ResponseEntity<>(validationResult, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(FileSizeNotAllowedException.class)
	public final ResponseEntity<BaseResult> handleFileSizeNotAllowedException(FileSizeNotAllowedException ex, WebRequest request){
		log.error("The given file size is larger than the allowed one. Details : ", ex);
		BaseResult validationResult = new BaseResult(new Date(), 
				ex.getMessage());
		return ResponseEntity.status(ex.getStatusCode()).body(validationResult);
	}

}
