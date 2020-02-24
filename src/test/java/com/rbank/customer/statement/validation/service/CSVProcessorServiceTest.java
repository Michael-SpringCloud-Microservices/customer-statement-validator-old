package com.rbank.customer.statement.validation.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.rbank.customer.statement.validation.model.RecordDetail;


/**
 * @author Michael Philomin Raj
 *
 */

@RunWith(SpringRunner.class)
public class CSVProcessorServiceTest {

	@InjectMocks
	CSVProcessorService csvProcessorService;
	
	@Test
	public void testForInvalidTransactionReference()throws IOException{
		File csvFile = new File(this.getClass().getResource("/csv/records-with-invalid-transaction-reference.csv").getFile());
		InputStream is = new FileInputStream(csvFile);
		MockMultipartFile multipartFile = new MockMultipartFile("csv", "records-with-invalid-transaction-reference.csv", "text/csv", is);
		is.close();
		List<RecordDetail> result = csvProcessorService.convertCsvToRecordDetails(multipartFile);
		Assert.assertNotEquals(null, result);
		assertEquals(6, (result.stream().filter(r->!r.isValid()).collect(Collectors.toList())).size());
	}
	
	@Test
	public void testForInvalidAccountNumber()throws IOException{
		File csvFile = new File(this.getClass().getResource("/csv/records-with-invalid-account-number.csv").getFile());
		InputStream is = new FileInputStream(csvFile);
		MockMultipartFile multipartFile = new MockMultipartFile("csv", "records-with-invalid-account-number.csv", "text/csv", is);
		is.close();
		List<RecordDetail> result = csvProcessorService.convertCsvToRecordDetails(multipartFile);
		Assert.assertNotEquals(null, result);
		assertEquals(11, (result.stream().filter(r->!r.isValid()).collect(Collectors.toList())).size());
	}
	
	@Test
	public void testForInvalidStartBalance()throws IOException{
		File csvFile = new File(this.getClass().getResource("/csv/records-with-invalid-start-balance.csv").getFile());
		InputStream is = new FileInputStream(csvFile);
		MockMultipartFile multipartFile = new MockMultipartFile("csv", "records-with-invalid-start-balance.csv", "text/csv", is);
		is.close();
		List<RecordDetail> result = csvProcessorService.convertCsvToRecordDetails(multipartFile);
		Assert.assertNotEquals(null, result);
		assertEquals(8,(result.stream().filter(r->!r.isValid()).collect(Collectors.toList())).size());
	}
	
	@Test
	public void testForInvalidMutation()throws IOException{
		File csvFile = new File(this.getClass().getResource("/csv/records-with-invalid-mutation.csv").getFile());
		InputStream is = new FileInputStream(csvFile);
		MockMultipartFile multipartFile = new MockMultipartFile("csv", "records-with-invalid-mutation.csv", "text/csv", is);
		is.close();
		List<RecordDetail> result = csvProcessorService.convertCsvToRecordDetails(multipartFile);
		Assert.assertNotEquals(null, result);
		assertEquals(8,(result.stream().filter(r->!r.isValid()).collect(Collectors.toList())).size());
	}
	
	
	@Test
	public void testForInvalidEndBalance()throws IOException{
		File csvFile = new File(this.getClass().getResource("/csv/records-with-invalid-end-balance.csv").getFile());
		InputStream is = new FileInputStream(csvFile);
		MockMultipartFile multipartFile = new MockMultipartFile("csv", "records-with-invalid-end-balance.csv", "text/csv", is);
		is.close();
		List<RecordDetail> result = csvProcessorService.convertCsvToRecordDetails(multipartFile);
		Assert.assertNotEquals(null, result);
		assertEquals(8,(result.stream().filter(r->!r.isValid()).collect(Collectors.toList())).size());
	}
}
