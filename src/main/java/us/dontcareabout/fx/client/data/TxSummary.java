package us.dontcareabout.fx.client.data;

import us.dontcareabout.fx.shared.Currency;

public class TxSummary {
	public final Currency currency;
	public final double balance;
	public final double cost;
	public final double profit;


	public TxSummary(Currency currency, double balance, double cost, double profit) {
		this.currency = currency;
		this.balance = balance;
		this.cost = cost;
		this.profit = profit;
	}
}
