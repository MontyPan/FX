package us.dontcareabout.fx.shared;

import java.io.Serializable;
import java.util.Date;

import com.google.common.base.Preconditions;

import us.dontcareabout.fx.shared.tool.CurrencyUtil;

public class ForeignTX implements Serializable, HasId{
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

	/**
	 * 對於買入的紀錄而言，意義為尚未賣出的餘額（參見 {@link #getBalance()}），
	 * 對於賣出的紀錄而言，意義為該筆賣出的獲利（參見 {@link #getProfit()}）。
	 */
	private double balance;

	@Override
	public int getId() {
		return id;
	}

	@Override
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
		this.balance = value > 0 ? value : 0;
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

	public void setProfit(double v) {
		Preconditions.checkState(value < 0, "買入的紀錄不計算利潤");
		balance = v;
	}

	/**
	 * 只有賣出（{@link #value} < 0）的紀錄才有意義。
	 *
	 * @return 賣出時的利潤
	 */
	public double getProfit() {
		return value < 0 ? balance : 0;
	}

	/**
	 * 只有買入（{@link #value} > 0）的紀錄才有意義。
	 * 在賣出時會扣掉對應的數值，直到為 0。
	 *
	 * @return 該筆交易還未賣出的餘額
	 */
	public double getBalance() {
		return value > 0 ? balance : 0;
	}

	public void sell(double v) {
		Preconditions.checkArgument(v > 0, "賣出負數？不用這麼扭曲吧？");
		Preconditions.checkState(value > 0, "賣出的紀錄不能再賣出");
		Preconditions.checkState(balance >= v, "餘額不足");

		balance = CurrencyUtil.subtract(balance, v);
	}
}
