/**
 * 
 */
package com.rbank.customer.statement.validation.exception;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.rbank.customer.statement.validation.model.RecordDetail;

/**
 * @author Michael Philomin Raj
 *
 */
@RunWith(SpringRunner.class)
public class InvalidDataExceptionTest {

	@Test
	public void TestInvalidDataException(){
		List<RecordDetail> recordDetails = new ArrayList<RecordDetail>();

		RecordDetail recordDetail1 = new RecordDetail();
		recordDetail1.setReference(Long.parseLong("177666"));
		recordDetail1.setAccountNumber("NL93ABNA0585619023");
		recordDetail1.setDescription("Flowers for Rik Theuß");
		recordDetail1.setStartBalanceParsed(new BigDecimal("44.85"));
		recordDetail1.setMutationParsed(new BigDecimal("-22.24"));
		recordDetail1.setEndBalanceParsed(new BigDecimal("22.61"));
		recordDetails.add(recordDetail1);

		InvalidDataException invalidDataException = new InvalidDataException(231,recordDetails,"The given file : records.csv has invalid records");
		Assert.assertNotEquals(null, invalidDataException);
		assertEquals(231, invalidDataException.getStatusCode());
		assertEquals("The given file : records.csv has invalid records", invalidDataException.getMessage());
		Assert.assertNotEquals(null, invalidDataException.getFailedRecords());
		assertEquals(1, invalidDataException.getFailedRecords().size());

	}
}
