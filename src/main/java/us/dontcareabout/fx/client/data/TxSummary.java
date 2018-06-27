package us.dontcareabout.fx.client.data;

public class TxSummary {
	public final double balance;
	public final double cost;
	public final double profit;


	public TxSummary(double balance, double cost, double profit) {
		this.balance = balance;
		this.cost = cost;
		this.profit = profit;
	}
}
