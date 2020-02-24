package com.rbank.customer.statement.validation.service;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rbank.customer.statement.validation.constants.CustomHttpStatusCodes;
import com.rbank.customer.statement.validation.constants.ErrorMessages;
import com.rbank.customer.statement.validation.constants.FileTypes;
import com.rbank.customer.statement.validation.exception.EmptyFileException;
import com.rbank.customer.statement.validation.exception.FileParsingException;
import com.rbank.customer.statement.validation.exception.FileSizeNotAllowedException;
import com.rbank.customer.statement.validation.exception.UnsupportedFileFormatException;
import com.rbank.customer.statement.validation.model.RecordDetail;

/**
 * @author Michael Philomin Raj
 *
 */

@Service
public class FileValidationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileValidationService.class);

	@Autowired
	CSVProcessorService csvProcessorService;


	@Autowired
	XMLProcessorService xmlProcessorService;

	@Autowired
	private StatementRecordsValidationService statementRecordsValidationService;

	public void validateFileSize(MultipartFile file, String fileName, String fileType) {
		// Check for empty file
		if (fileType.equalsIgnoreCase(FileTypes.CSV_CONTENT_TYPE_1) || fileType.equalsIgnoreCase(FileTypes.CSV_CONTENT_TYPE_2)){
			isCsvFileEmpty(file, fileName);
		}else if(file.isEmpty()){
			throw new EmptyFileException(CustomHttpStatusCodes.HTTP_STATUS_233,ErrorMessages.FILE_CANT_BE_EMPTY);
		}

		// Check for file's allowed max size
		if(file.getSize()>8000000){
			throw new FileSizeNotAllowedException(CustomHttpStatusCodes.HTTP_STATUS_235,ErrorMessages.FILE_SIZE_NOT_ALLOWED);
		}

	}

	private void isCsvFileEmpty(MultipartFile file,String fileName){
		try (InputStream is = file.getInputStream();) {
			CSVParser csvParser = CSVParser.parse(is,StandardCharsets.US_ASCII, 
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			if(csvParser.getRecords().isEmpty()){
				throw new EmptyFileException(CustomHttpStatusCodes.HTTP_STATUS_233,ErrorMessages.FILE_CANT_BE_EMPTY);
			}
		}catch(EmptyFileException efe){
			throw efe;
		}catch(Exception e){
			LOGGER.error("Parsing file with fileName = {} failed. And the reason is : {}", fileName , e);
			throw new FileParsingException(CustomHttpStatusCodes.HTTP_STATUS_234,"Parsing file - " + fileName + " failed due to invalid data");
		}
	}

	/**
	 * @param file
	 * @return List<RecordDetail>
	 * @throws IOException
	 * This method will validate file type or size as per the business rule
	 * And then it will validate the records in the file with help of xml/csv processor and statement records validation service 
	 */
	public List<RecordDetail> validateFile(MultipartFile file) throws IOException{
		String fileName = file.getOriginalFilename();
		String fileType = file.getContentType();

		LOGGER.info("FileValidationService -> fileName={} and fileType={}" , fileName, fileType);

		validateFileSize(file, fileName, fileType);

		String fileExtension = getTheFileExtension(fileType);

		List<RecordDetail> recordDetails;

		switch (fileExtension) {
		case FileTypes.XML:
			recordDetails = xmlProcessorService.convertXmlToRecordDetails(file);
			return statementRecordsValidationService.validateStatementRecords(fileName, recordDetails);
		case FileTypes.CSV:
			recordDetails = csvProcessorService.convertCsvToRecordDetails(file);			
			return statementRecordsValidationService.validateStatementRecords(fileName, recordDetails);
		default:
			throw new UnsupportedFileFormatException(CustomHttpStatusCodes.HTTP_STATUS_232,String.join(" ", "The given file format",fileType,"is not supported"));
		}
	}

	private String getTheFileExtension(String fileType){
		String fileExtension = fileType;
		if (fileType.equalsIgnoreCase(FileTypes.XML_CONTENT_TYPE_1) || fileType.equalsIgnoreCase(FileTypes.XML_CONTENT_TYPE_2)){
			fileExtension = FileTypes.XML;
		}else if (fileType.equalsIgnoreCase(FileTypes.CSV_CONTENT_TYPE_1) || fileType.equalsIgnoreCase(FileTypes.CSV_CONTENT_TYPE_2)){
			fileExtension = FileTypes.CSV;
		}
		return fileExtension;
	}

}
