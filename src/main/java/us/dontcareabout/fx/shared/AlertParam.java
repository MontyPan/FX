package us.dontcareabout.fx.shared;

import java.io.Serializable;

/**
 * 這裡的買入、賣出是指銀行方的匯率。若值為 null 表示沒有設定。
 */
public class AlertParam implements Serializable {
	private static final long serialVersionUID = 1L;

	private Double buyMin;
	private Double sellMax;

	public Double getBuyMin() {
		return buyMin;
	}
	public void setBuyMin(Double buyMin) {
		this.buyMin = buyMin;
	}
	public Double getSellMax() {
		return sellMax;
	}
	public void setSellMax(Double sellMax) {
		this.sellMax = sellMax;
	}
}
