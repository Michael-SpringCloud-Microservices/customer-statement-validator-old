/**
 * 
 */
package com.rbank.customer.statement.validation.response;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.rbank.customer.statement.validation.response.BaseResult;

/**
 * @author Michael Philomin Raj
 *
 */
@RunWith(SpringRunner.class)
public class BaseResultTest {

	
	@Test
	public void testBaseResult(){		
		Date sysDate = new Date();
		BaseResult baseResult = new BaseResult(sysDate,"File can't be empty");			
		Assert.assertNotEquals(null, baseResult);
		Assert.assertNotEquals(null, baseResult.toString());
		assertEquals(sysDate, baseResult.getTimestamp());
		assertEquals("File can't be empty", baseResult.getMessage());
	}
}
