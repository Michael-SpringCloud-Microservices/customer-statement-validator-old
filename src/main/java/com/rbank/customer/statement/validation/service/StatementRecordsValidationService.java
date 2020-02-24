package com.rbank.customer.statement.validation.service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rbank.customer.statement.validation.model.RecordDetail;

/**
 * @author Michael Philomin Raj
 *
 */

@Service
public class StatementRecordsValidationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatementRecordsValidationService.class);
	private static final String RECORD_DUPLICATED = "Record is duplicated";
	private static final String BALANCE_MISMATCHED = "End Balance is mismatched";

	/**
	 * 
	 * @param fileName
	 * @param recordDetails
	 * @return
	 */
	public List<RecordDetail> validateStatementRecords(String fileName, List<RecordDetail> recordDetails){
		LocalDateTime startTime = LocalDateTime.now();
		List<RecordDetail> failedRecords = new ArrayList<>();
		//Identifying failed records ( ie. records having the duplication of Transaction reference or the improper End Balance)
		List<Long> processedRecords = new ArrayList<>();

		recordDetails.forEach(recordDetail ->{
			if(!recordDetail.isValid()){ 
				failedRecords.add(recordDetail); // records which failed in basic validation due to invalid data 
			}else{ 
				if(isFailedWithBusinessRuels(processedRecords,recordDetail)){
					failedRecords.add(recordDetail); // records which failed with given business rules validation like duplication or improper end-balance 
				}
			} 
			processedRecords.add(recordDetail.getReference()); 
		});
		processedRecords.clear();
		LocalDateTime endTime = LocalDateTime.now();
		LOGGER.info("StatementRecordsValidationService - Time taken to validateStatementRecords of fileName={} is {} in milli seconds",fileName,ChronoUnit.MILLIS.between(startTime, endTime)); 
		return failedRecords; 
	}

	private boolean isFailedWithBusinessRuels(List<Long> processedRecords,RecordDetail recordDetail){
		boolean isDuplicate  = isDuplicated(processedRecords,recordDetail);
		boolean hasInvalidEndBalance = false;
		if(!isDuplicate)
			hasInvalidEndBalance = hasInvalidEndBalance(recordDetail);

		return isDuplicate || hasInvalidEndBalance;
	}

	private boolean isDuplicated(List<Long> processedRecords,RecordDetail recordDetail){
		boolean res = false;
		if(recordDetail.getReference()!=0 && !processedRecords.isEmpty() && processedRecords.contains(recordDetail.getReference())){
			recordDetail.setValid(false);
			recordDetail.getFailureReasons().add(RECORD_DUPLICATED);
			res = true;
		}
		return res;
	}

	private boolean hasInvalidEndBalance(RecordDetail recordDetail){
		boolean res = false;		
		BigDecimal startBalance = recordDetail.getStartBalanceParsed().setScale(2);
		BigDecimal mutation = recordDetail.getMutationParsed().setScale(2);
		BigDecimal endBalance = recordDetail.getEndBalanceParsed().setScale(2);		
		if((startBalance.add(mutation)).compareTo(endBalance)!=0){
			recordDetail.setValid(false);
			recordDetail.getFailureReasons().add(BALANCE_MISMATCHED);
			res = true;
		}
		return res;
	}
}

