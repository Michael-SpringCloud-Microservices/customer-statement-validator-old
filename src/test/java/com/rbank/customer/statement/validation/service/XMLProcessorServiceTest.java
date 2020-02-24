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
public class XMLProcessorServiceTest {
	
	@InjectMocks
	XMLProcessorService xmlProcessorService;

	@Test
	public void testConvertXmlToReportDetails() throws IOException{
		File xmlFile = new File(this.getClass().getResource("/xml/records.xml").getFile());
		InputStream is = new FileInputStream(xmlFile);
		MockMultipartFile multipartFile = new MockMultipartFile("xml", "records.xml", "text/xml", is);
		is.close();		
		List<RecordDetail> result = xmlProcessorService.convertXmlToRecordDetails(multipartFile);
		Assert.assertNotEquals(null, result);
		assertEquals(10, result.size());
	}
	
	@Test
	public void testForInvalidTransactionReference()throws IOException{
		File xmlFile = new File(this.getClass().getResource("/xml/records-with-invalid-transaction-reference.xml").getFile());
		InputStream is = new FileInputStream(xmlFile);
		MockMultipartFile multipartFile = new MockMultipartFile("xml", "records-with-invalid-transaction-reference.xml", "text/xml", is);
		is.close();
		List<RecordDetail> result = xmlProcessorService.convertXmlToRecordDetails(multipartFile);
		Assert.assertNotEquals(null, result);
		assertEquals(6, (result.stream().filter(r->!r.isValid()).collect(Collectors.toList())).size());
	}
	
	@Test
	public void testForInvalidAccountNumber()throws IOException{
		File xmlFile = new File(this.getClass().getResource("/xml/records-with-invalid-account-number.xml").getFile());
		InputStream is = new FileInputStream(xmlFile);
		MockMultipartFile multipartFile = new MockMultipartFile("xml", "records-with-invalid-account-number.xml", "text/xml", is);
		is.close();
		List<RecordDetail> result = xmlProcessorService.convertXmlToRecordDetails(multipartFile);
		Assert.assertNotEquals(null, result);
		assertEquals(8, (result.stream().filter(r->!r.isValid()).collect(Collectors.toList())).size());
	}
	
	@Test
	public void testForInvalidStartBalance()throws IOException{
		File xmlFile = new File(this.getClass().getResource("/xml/records-with-invalid-start-balance.xml").getFile());
		InputStream is = new FileInputStream(xmlFile);
		MockMultipartFile multipartFile = new MockMultipartFile("xml", "records-with-invalid-start-balance.xml", "text/xml", is);
		is.close();
		List<RecordDetail> result = xmlProcessorService.convertXmlToRecordDetails(multipartFile);
		Assert.assertNotEquals(null, result);
		assertEquals(6,(result.stream().filter(r->!r.isValid()).collect(Collectors.toList())).size());
	}
	
	@Test
	public void testForInvalidMutation()throws IOException{
		File xmlFile = new File(this.getClass().getResource("/xml/records-with-invalid-mutation.xml").getFile());
		InputStream is = new FileInputStream(xmlFile);
		MockMultipartFile multipartFile = new MockMultipartFile("xml", "records-with-invalid-mutation.xml", "text/xml", is);
		is.close();
		List<RecordDetail> result = xmlProcessorService.convertXmlToRecordDetails(multipartFile);
		Assert.assertNotEquals(null, result);
		assertEquals(5,(result.stream().filter(r->!r.isValid()).collect(Collectors.toList())).size());
	}
	
	@Test
	public void testForInvalidEndBalance()throws IOException{
		File xmlFile = new File(this.getClass().getResource("/xml/records-with-invalid-end-balance.xml").getFile());
		InputStream is = new FileInputStream(xmlFile);
		MockMultipartFile multipartFile = new MockMultipartFile("xml", "records-with-invalid-end-balance.xml", "text/xml", is);
		is.close();
		List<RecordDetail> result = xmlProcessorService.convertXmlToRecordDetails(multipartFile);
		Assert.assertNotEquals(null, result);
		assertEquals(6,(result.stream().filter(r->!r.isValid()).collect(Collectors.toList())).size());
	}	
}
