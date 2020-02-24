package com.rbank.customer.statement.validation.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

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
public class RecordDetailTest {

	@Test
	public void testRecordDetails(){
		
		RecordDetail recordDetail1 = new RecordDetail();
		recordDetail1.setReference(Long.parseLong("177666"));
		recordDetail1.setAccountNumber("NL93ABNA0585619023");
		recordDetail1.setDescription("Flowers for Rik Theuß");
		recordDetail1.setStartBalanceParsed(new BigDecimal("44.85"));
		recordDetail1.setMutationParsed(new BigDecimal("-22.24"));
		recordDetail1.setEndBalanceParsed(new BigDecimal("22.61"));
		recordDetail1.setValid(true);

		Assert.assertNotEquals(null, recordDetail1);
		Assert.assertNotEquals(null, recordDetail1.toString());
		assertEquals(Long.parseLong("177666"), recordDetail1.getReference().longValue());
		assertEquals("NL93ABNA0585619023", recordDetail1.getAccountNumber());
		assertEquals("Flowers for Rik Theuß", recordDetail1.getDescription());
		assertEquals(new BigDecimal("44.85"), recordDetail1.getStartBalanceParsed()); // Third parameter stands for delta. '0' means it should have to have the exact match 
		assertEquals(new BigDecimal("-22.24"), recordDetail1.getMutationParsed());
		assertEquals(new BigDecimal("22.61"), recordDetail1.getEndBalanceParsed());
		Assert.assertTrue(recordDetail1.isValid());

	}
}
