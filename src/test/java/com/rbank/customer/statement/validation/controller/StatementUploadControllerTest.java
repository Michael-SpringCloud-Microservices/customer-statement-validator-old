package com.rbank.customer.statement.validation.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.rbank.customer.statement.validation.constants.CustomHttpStatusCodes;
import com.rbank.customer.statement.validation.exception.InvalidDataException;
import com.rbank.customer.statement.validation.exception.UnsupportedFileFormatException;
import com.rbank.customer.statement.validation.model.RecordDetail;
import com.rbank.customer.statement.validation.response.BaseResult;
import com.rbank.customer.statement.validation.service.FileValidationService;

/**
 * @author Michael Philomin Raj
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class StatementUploadControllerTest {

	@InjectMocks
	StatementUploadController statementUploadController;

	@Mock
	FileValidationService fileValidationService;


	@Test
	public void CsvFileWithGivenData() throws Exception {
		String message = "All the records in the given file " + "records.csv" + " are valid";
		File csvFile = new File(this.getClass().getResource("/csv/records.csv").getFile());
		InputStream is = new FileInputStream(csvFile);
		MockMultipartFile multipartFile = new MockMultipartFile("csv", "records.csv", "text/csv", is);
		is.close();
		List<RecordDetail> reports = new ArrayList<RecordDetail>();		
		Mockito.when(fileValidationService.validateFile(multipartFile)).thenReturn(reports);

		ResponseEntity<BaseResult> result = statementUploadController.validateFileUpload(multipartFile);
		Assert.assertNotEquals(null, result);
		Assert.assertEquals(CustomHttpStatusCodes.HTTP_STATUS_230, result.getStatusCodeValue());
		assertEquals(message, result.getBody().getMessage());
		verify(fileValidationService, times(1)).validateFile(multipartFile);
	}

	@Test
	public void xmlFileWithGivenData() throws Exception {
		String message = "All the records in the given file " + "records.xml" + " are valid";
		File xmlFile = new File(this.getClass().getResource("/xml/records.xml").getFile());
		InputStream is = new FileInputStream(xmlFile);
		MockMultipartFile multipartFile = new MockMultipartFile("xml", "records.xml", "text/xml", is);
		is.close();
		List<RecordDetail> reports = new ArrayList<RecordDetail>();
		Mockito.when(fileValidationService.validateFile(multipartFile)).thenReturn(reports);
		ResponseEntity<BaseResult> result = statementUploadController.validateFileUpload(multipartFile);
		Assert.assertNotEquals(null, result);
		Assert.assertEquals(CustomHttpStatusCodes.HTTP_STATUS_230, result.getStatusCodeValue());
		assertEquals(message, result.getBody().getMessage());
		verify(fileValidationService, times(1)).validateFile(multipartFile);
	}

	@Test(expected = InvalidDataException.class)
	public void CsvFileWithGivenInvalidData() throws Exception {
		File csvFile = new File(this.getClass().getResource("/csv/records.csv").getFile());
		InputStream is = new FileInputStream(csvFile);
		MockMultipartFile multipartFile = new MockMultipartFile("csv", "records.csv", "text/csv", is);
		is.close();
		List<RecordDetail> reports = new ArrayList<RecordDetail>();
		RecordDetail report1 = new RecordDetail();
		report1.setTransactionReference("112806");
		report1.setDescription("Clothes from Peter de Vries");
		reports.add(report1);
		RecordDetail report2 = new RecordDetail();
		report2.setTransactionReference("112806");
		report2.setDescription("Tickets for Erik Dekker");
		reports.add(report2);
		Mockito.when(fileValidationService.validateFile(multipartFile)).thenReturn(reports);	
		statementUploadController.validateFileUpload(multipartFile);	
	}

	@Test(expected = InvalidDataException.class)
	public void xmlFileWithGivenInvalidData() throws Exception {
		File xmlFile = new File(this.getClass().getResource("/xml/records.xml").getFile());
		InputStream is = new FileInputStream(xmlFile);
		MockMultipartFile multipartFile = new MockMultipartFile("xml", "records.xml", "text/xml", is);
		is.close();
		List<RecordDetail> reports = new ArrayList<RecordDetail>();
		RecordDetail report1 = new RecordDetail();
		report1.setTransactionReference("154270");
		report1.setDescription("Candy for Peter de Vries");
		reports.add(report1);
		RecordDetail report2 = new RecordDetail();
		report2.setTransactionReference("154270");
		report2.setDescription("Tickets for Vincent Dekker");
		reports.add(report2);
		Mockito.when(fileValidationService.validateFile(multipartFile)).thenReturn(reports);
		statementUploadController.validateFileUpload(multipartFile);	
	}

	@Test(expected = UnsupportedFileFormatException.class)
	public void unsupportedFileFormatWithTextFile() throws Exception {
		File textFile = new File(this.getClass().getResource("/text/records.txt").getFile());
		InputStream is = new FileInputStream(textFile);
		MockMultipartFile multipartFile = new MockMultipartFile("txt", "records.txt", "text/plain", is);
		is.close();
		Mockito.when(fileValidationService.validateFile(multipartFile)).thenThrow(new UnsupportedFileFormatException(232,"The given file format txt is not supported"));
		statementUploadController.validateFileUpload(multipartFile);		
	}  



}
