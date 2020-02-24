package com.rbank.customer.statement.validation.util;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rbank.customer.statement.validation.constants.ErrorMessages;
import com.rbank.customer.statement.validation.constants.FieldNames;
import com.rbank.customer.statement.validation.model.RecordDetail;

/**
 * @author Michael Philomin Raj
 *
 */

public class BasicValidationUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicValidationUtil.class);
	
	private BasicValidationUtil() {
		
	}

	public static void parseAndSetTransactionReference(String param, String fileName, String fieldName, RecordDetail recordDetail){
		if(isNotEmptyOrNull(param, fileName, fieldName, recordDetail) && isNumeric(param, fileName, fieldName, recordDetail)) {
			recordDetail.setReference(Long.valueOf(param.trim()));
		}
	}

	public static void parseAndSetAccountNumber(String param, String fileName, String fieldName, RecordDetail recordDetail){
		if(isNotEmptyOrNull(param, fileName, fieldName, recordDetail) && isAlphanumeric(param, fileName, fieldName, recordDetail)){
			recordDetail.setAccountNumber(param.trim());
		}
	}

	public static void parseAndSetDescription(String param , String fileName, String fieldName, RecordDetail recordDetail){
		if(isNotEmptyOrNull(param, fileName, fieldName, recordDetail)){
			recordDetail.setDescription(param.trim());
		}
	}

	public static void parseAndSetValue(String param, String fileName, String fieldName, RecordDetail recordDetail) {
		if(isNotEmptyOrNull(param, fileName, fieldName, recordDetail)){
			convertToBigDecimalAndSetValue(param, fileName, fieldName, recordDetail);
		}
	}

	public static void convertToBigDecimalAndSetValue(String param, String fileName, String fieldName, RecordDetail recordDetail) {
		try{
			BigDecimal result = new BigDecimal(param);
			switch (fieldName) {
			case FieldNames.START_BALANCE:
				recordDetail.setStartBalanceParsed(result);
			    break;
			case FieldNames.MUTATION:
				recordDetail.setMutationParsed(result);
			    break;
			default:
				recordDetail.setEndBalanceParsed(result);
			    break;
			}
		}catch(NumberFormatException | NullPointerException e){
			LOGGER.error("Parsing file - fileName = {} failed with {}. And the reason is : ", fileName , fieldName, e);	
			recordDetail.getFailureReasons().add(constructFailureReason(fileName, fieldName, ErrorMessages.SHOULD_BE_VALID_DECIMAL));
			recordDetail.setValid(false);
		}
	}

	private static boolean isNotEmptyOrNull(String param, String fileName, String fieldName, RecordDetail recordDetail) {
		boolean result = true;
		if (StringUtils.isBlank(param) || param.equals("Null")) {
			recordDetail.setReference(null);
			recordDetail.getFailureReasons().add(constructFailureReason(fileName, fieldName, ErrorMessages.CANT_BE_BLANK_OR_NULL));
			recordDetail.setValid(false);
			result = false;
		}
		return result;
	}

	private static boolean isNumeric(String param, String fileName, String fieldName, RecordDetail recordDetail) {
		boolean result = true;
		if(!StringUtils.isNumeric(param)){			
			recordDetail.getFailureReasons().add(constructFailureReason(fileName, fieldName, ErrorMessages.SHOULD_BE_NUMERIC));
			recordDetail.setValid(false);
			result = false;
		}
		return result;
	}

	private static boolean isAlphanumeric(String param, String fileName, String fieldName, RecordDetail recordDetail) {
		boolean result = true;
		if(!StringUtils.isAlphanumeric(param) || param.length()!=18){ // A Dutch IBAN should always contain exactly 18 digits and letters	
			recordDetail.getFailureReasons().add(constructFailureReason(fileName, fieldName, ErrorMessages.SHOULD_BE_ALPHA_NUMERIC));
			recordDetail.setValid(false);
			result = false;
		}
		return result;
	}


	private static String constructFailureReason(String fileName, String fieldName , String message){
		return String.join(" ","Parsing file -",fileName,"failed due to invalid",fieldName,":",fieldName,message);
	}

}
