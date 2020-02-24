package com.rbank.customer.statement.validation.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Michael Philomin Raj
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private String transactionReference;

	private Long reference;

	@JsonIgnore
	private String accountNumber;

	private String description;
	
	@JsonIgnore
	private String startBalance;

	@JsonIgnore
	private String mutation;

	@JsonIgnore
	private String endBalance;

	@JsonIgnore
	private BigDecimal startBalanceParsed;

	@JsonIgnore
	private BigDecimal mutationParsed;

	@JsonIgnore
	private BigDecimal endBalanceParsed;

	private boolean isValid = true;

	private List<String> failureReasons = new ArrayList<>();

	/**
	 * @return the reference
	 */
	public Long getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(Long reference) {
		this.reference = reference;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}	
	
	/**
	 * @return the transactionReference
	 */
	public String getTransactionReference() {
		return transactionReference;
	}

	/**
	 * @param transactionReference the transactionReference to set
	 */
	@XmlAttribute(name = "reference")
	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	/**
	 * @return the startBalance
	 */
	public String getStartBalance() {
		return startBalance;
	}

	/**
	 * @param startBalance the startBalance to set
	 */
	public void setStartBalance(String startBalance) {
		this.startBalance = startBalance;
	}

	/**
	 * @return the mutation
	 */
	public String getMutation() {
		return mutation;
	}

	/**
	 * @param mutation the mutation to set
	 */
	public void setMutation(String mutation) {
		this.mutation = mutation;
	}

	/**
	 * @return the endBalance
	 */
	public String getEndBalance() {
		return endBalance;
	}

	/**
	 * @param endBalance the endBalance to set
	 */
	public void setEndBalance(String endBalance) {
		this.endBalance = endBalance;
	}

	/**
	 * @return the startBalanceParsed
	 */
	public BigDecimal getStartBalanceParsed() {
		return startBalanceParsed;
	}

	/**
	 * @param startBalanceParsed the startBalanceParsed to set
	 */
	public void setStartBalanceParsed(BigDecimal startBalanceParsed) {
		this.startBalanceParsed = startBalanceParsed;
	}

	/**
	 * @return the mutationParsed
	 */
	public BigDecimal getMutationParsed() {
		return mutationParsed;
	}

	/**
	 * @param mutationParsed the mutationParsed to set
	 */
	public void setMutationParsed(BigDecimal mutationParsed) {
		this.mutationParsed = mutationParsed;
	}

	/**
	 * @return the endBalanceParsed
	 */
	public BigDecimal getEndBalanceParsed() {
		return endBalanceParsed;
	}

	/**
	 * @param endBalanceParsed the endBalanceParsed to set
	 */
	public void setEndBalanceParsed(BigDecimal endBalanceParsed) {
		this.endBalanceParsed = endBalanceParsed;
	}

	/**
	 * @return the isValid
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * @param isValid the isValid to set
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * @return the failureReasons
	 */
	public List<String> getFailureReasons() {
		return failureReasons;
	}

	/**
	 * @param failureReasons the failureReasons to set
	 */
	public void setFailureReasons(List<String> failureReasons) {
		this.failureReasons = failureReasons;
	}
}
