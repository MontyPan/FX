package us.dontcareabout.fx.shared;

import java.io.Serializable;
import java.util.Date;

public class CapitalTX implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;

	/**
	 * 如果是 null 表示為本金面的異動，例如利息、本金增減。
	 */
	private Integer foreignId;

	private Date date;

	private double value;

	private String note;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getForeignId() {
		return foreignId;
	}

	public void setForeignId(Integer foreignId) {
		this.foreignId = foreignId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
