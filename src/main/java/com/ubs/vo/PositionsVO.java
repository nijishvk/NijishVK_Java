package com.ubs.vo;


public class PositionsVO implements Comparable<PositionsVO>{
	private String instrument;
	private Long account;
	private String accountType;
	private Long quantity;
	private Long delta;
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public Long getAccount() {
		return account;
	}
	public void setAccount(Long account) {
		this.account = account;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public Long getDelta() {
		return delta;
	}
	public void setDelta(Long delta) {
		this.delta = delta;
	}
	@Override
	public int compareTo(PositionsVO o) {
		return this.delta<o.delta?-1:
            this.delta>o.delta?1:0;
	}
	
	
}
