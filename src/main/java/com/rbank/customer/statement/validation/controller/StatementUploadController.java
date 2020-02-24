package com.rbank.customer.statement.validation.controller;


import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rbank.customer.statement.validation.constants.CustomHttpStatusCodes;
import com.rbank.customer.statement.validation.exception.InvalidDataException;
import com.rbank.customer.statement.validation.model.RecordDetail;
import com.rbank.customer.statement.validation.response.BaseResult;
import com.rbank.customer.statement.validation.response.ValidationResult;
import com.rbank.customer.statement.validation.service.FileValidationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Michael Philomin Raj
 *
 */

@RestController
@RequestMapping("/statement")
public class StatementUploadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatementUploadController.class);

	@Autowired
	private FileValidationService fileValidationService;

	/**
	 * @param file
	 * @return ResponseEntity<BaseResult>
	 * @throws IOException
	 * This method will receive the uploaded file, validate it and then returns back the failed record(s) to user  
	 */
	@PostMapping(value = "/upload")  
	@ApiOperation(value = "Make a POST request to upload the file - Only xml or csv formats are allowed",produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiResponses({
		@ApiResponse(code = 230, message = "The file is valid", response = BaseResult.class),
		@ApiResponse(code = 231, message = "The file has invalid record(s)", response = ValidationResult.class),
		@ApiResponse(code = 232, message = "The uploaded file format is not supported", response = BaseResult.class),
		@ApiResponse(code = 233, message = "The uploaded file is empty", response = BaseResult.class),
		@ApiResponse(code = 234, message = "The uploaded file could not be parsed", response = BaseResult.class),
		@ApiResponse(code = 235, message = "The file size exceeds the allowed limit", response = BaseResult.class)
	})
	public ResponseEntity<BaseResult> validateFileUpload(
			@ApiParam(name = "file", value = "Select the file to Upload", required = true)
			@RequestParam("file") MultipartFile file) throws IOException  {
		String fileName = file.getOriginalFilename();
		LOGGER.info("The uploaded file naame ={}", fileName);
		List<RecordDetail> failedRecords = fileValidationService.validateFile(file);
		if(!failedRecords.isEmpty()){
			throw new InvalidDataException(CustomHttpStatusCodes.HTTP_STATUS_231,failedRecords, String.join(" ", "The given file",fileName,"has invalid records"));
		}
		BaseResult validationSuccess = new BaseResult(new Date(),String.join(" ", "All the records in the given file",fileName,"are valid"));
		return ResponseEntity.status(CustomHttpStatusCodes.HTTP_STATUS_230).body(validationSuccess);
	}	
}