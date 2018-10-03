package com.ubs.vo;

public class TransactionVO {

	public TransactionVO(Long transactionId, String instrument, String transactionType, Long transactionQuantity) {
		super();
		this.transactionId = transactionId;
		this.instrument = instrument;
		this.transactionType = transactionType;
		this.transactionQuantity = transactionQuantity;
	}

	private Long transactionId;
	private String instrument;
	private String transactionType;
	private Long transactionQuantity;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Long getTransactionQuantity() {
		return transactionQuantity;
	}

	public void setTransactionQuantity(Long transactionQuantity) {
		this.transactionQuantity = transactionQuantity;
	}
}
