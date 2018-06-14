package us.dontcareabout.fx.shared;

import java.io.Serializable;
import java.util.Date;

public class ForeignTX implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;

	/**
	 * 如果是 null 表示為該貨幣自身的異動，例如利息。
	 */
	private Integer capitalId;

	private Date date;

	private Currency currency;

	private double value;

	private double rate;

	private String note;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getCapitalId() {
		return capitalId;
	}

	public void setCapitalId(Integer capitalId) {
		this.capitalId = capitalId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public double getValue() {
		return value;
	}

	/**
	 * @return 存入金額，恆為正值。如果是 null 表示此筆紀錄為轉出。
	 */
	public Double getIncome() {
		return value > 0 ? value : null;
	}

	/**
	 * @return 轉出金額，恆為正值。如果是 null 表示此筆紀錄為存入。
	 */
	public Double getOutgoing() {
		return value < 0 ? value * -1 : null;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
