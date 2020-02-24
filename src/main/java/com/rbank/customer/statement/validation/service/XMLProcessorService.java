package com.rbank.customer.statement.validation.service;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rbank.customer.statement.validation.constants.CustomHttpStatusCodes;
import com.rbank.customer.statement.validation.constants.FieldNames;
import com.rbank.customer.statement.validation.exception.FileParsingException;
import com.rbank.customer.statement.validation.model.RecordDetail;
import com.rbank.customer.statement.validation.model.Records;
import com.rbank.customer.statement.validation.util.BasicValidationUtil;

/**
 * @author Michael Philomin Raj
 *
 */

@Service
public class XMLProcessorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(XMLProcessorService.class);

	private Unmarshaller unmarshaller;

	/*@InitBinder
	public void buildUnmarshaller(){
		if(this.unmarshaller == null) {
			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Records.class);
				this.unmarshaller = jaxbContext.createUnmarshaller();
			} catch (JAXBException e) {
				LOGGER.error("XMLProcessorService - Exception at buildUnmarshaller : ", e);
			}
		}
	}*/

	/**
	 * 
	 * @param file
	 * @return List<RecordDetail>
	 * This method will parse the given 'XML' MultipartFile and convert it to list of RecordDetail
	 */
	public List<RecordDetail> convertXmlToRecordDetails(MultipartFile file){
		LocalDateTime startTime = LocalDateTime.now();
		String fileName = file.getOriginalFilename(); 
		List<RecordDetail> recordDetails = null;
		try {
			if(this.unmarshaller == null) {
				buildJaxbUnmarshaller();
			}
			Records records = (Records) unmarshaller.unmarshal(file.getInputStream());

			recordDetails = Arrays.asList(records.getRecordDetails()).stream().parallel().map(record -> {

				BasicValidationUtil.parseAndSetTransactionReference(record.getTransactionReference(), fileName, FieldNames.REFERENCE, record);

				BasicValidationUtil.parseAndSetAccountNumber(record.getAccountNumber(), fileName, FieldNames.ACCOUNT_NUMBER, record);

				BasicValidationUtil.parseAndSetDescription(record.getDescription(), fileName, FieldNames.DESCRIPTION, record);

				BasicValidationUtil.parseAndSetValue(record.getStartBalance(), fileName, FieldNames.START_BALANCE, record);

				BasicValidationUtil.parseAndSetValue(record.getMutation(), fileName, FieldNames.MUTATION, record);

				BasicValidationUtil.parseAndSetValue(record.getEndBalance(), fileName, FieldNames.END_BALANCE, record);

				return record;
			}).collect(Collectors.toList()); 
		}catch(Exception e) {
			LOGGER.error("XMLProcessorService - Exception at convertXmlToReportDetails : ", e);
			throw new FileParsingException(CustomHttpStatusCodes.HTTP_STATUS_234,String.join(" ", "Exception at convertXmlToReportDetails - FileName =",fileName,"- Failed due to invalid data"));			
		}
		LocalDateTime endTime = LocalDateTime.now();
		LOGGER.info("XMLProcessorService - Time taken to convert Xml file = {} is {} in milli seconds",fileName,ChronoUnit.MILLIS.between(startTime, endTime));
		return recordDetails;
	}

	private void buildJaxbUnmarshaller() throws JAXBException
	{
		JAXBContext jaxbContext = JAXBContext.newInstance(Records.class);
		this.unmarshaller = jaxbContext.createUnmarshaller();
	}
}
