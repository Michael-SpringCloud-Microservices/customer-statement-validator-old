package com.rbank.customer.statement.validation.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rbank.customer.statement.validation.constants.CustomHttpStatusCodes;
import com.rbank.customer.statement.validation.constants.FieldNames;
import com.rbank.customer.statement.validation.exception.FileParsingException;
import com.rbank.customer.statement.validation.model.RecordDetail;
import com.rbank.customer.statement.validation.util.BasicValidationUtil;

/**
 * @author Michael Philomin Raj
 *
 */

@Service
public class CSVProcessorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CSVProcessorService.class);

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public List<RecordDetail> convertCsvToRecordDetails(MultipartFile file) throws IOException{
		LocalDateTime startTime = LocalDateTime.now();
		String fileName = file.getOriginalFilename(); 
		List<RecordDetail> recordDetails = null;
		try (InputStream is = file.getInputStream();) {
			CSVParser csvParser = CSVParser.parse(is,StandardCharsets.US_ASCII,CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			recordDetails = csvParser.getRecords().stream().parallel().map(csvRecord -> {
				RecordDetail recordDetail = new RecordDetail();
				BasicValidationUtil.parseAndSetTransactionReference(csvRecord.get(FieldNames.REFERENCE), fileName, FieldNames.REFERENCE, recordDetail);
				BasicValidationUtil.parseAndSetAccountNumber(csvRecord.get(FieldNames.ACCOUNT_NUMBER), fileName, FieldNames.ACCOUNT_NUMBER, recordDetail);
				BasicValidationUtil.parseAndSetDescription(csvRecord.get(FieldNames.DESCRIPTION), fileName, FieldNames.DESCRIPTION, recordDetail);
				BasicValidationUtil.parseAndSetValue(csvRecord.get(FieldNames.START_BALANCE), fileName, FieldNames.START_BALANCE, recordDetail );
				BasicValidationUtil.parseAndSetValue(csvRecord.get(FieldNames.MUTATION), fileName, FieldNames.MUTATION, recordDetail);
				BasicValidationUtil.parseAndSetValue(csvRecord.get(FieldNames.END_BALANCE), fileName, FieldNames.END_BALANCE, recordDetail);
				return recordDetail;
			}).collect(Collectors.toList());
		}catch(Exception e){
			LOGGER.error("Parsing file with fileName = {} failed. And the reason is : {}", fileName , e);
			throw new FileParsingException(CustomHttpStatusCodes.HTTP_STATUS_234,String.join(" ", "Parsing file -",fileName,"failed due to invalid data"));
		}
		LocalDateTime endTime = LocalDateTime.now();
		LOGGER.info("CSVProcessorService - Time taken to convert Csv fileName ={} is {} in milli seconds",fileName,ChronoUnit.MILLIS.between(startTime, endTime)); 
		return recordDetails;
	}
}
