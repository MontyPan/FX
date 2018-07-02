package us.dontcareabout.fx.shared.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.ForeignTX;

/**
 * 負責在賣出時找尋對應買入紀錄。
 * 因為行為類似交易撮合，所以取名作 Matcher。
 */
public class Matcher {
	/** 價格高的優先、價格相同以日期早的優先。 */
	private static final Comparator<ForeignTX> profitComparator = new Comparator<ForeignTX>() {
		@Override
		public int compare(ForeignTX o1, ForeignTX o2) {
			if (o1.getRate() == o2.getRate()) {
				return o1.getDate().compareTo(o2.getDate());
			} else {
				return Double.valueOf(o2.getRate()).compareTo(o1.getRate());
			}
		}
	};

	/** 價格低的優先、價格相同以日期早的優先。 */
	private static final Comparator<ForeignTX> lossComparator = new Comparator<ForeignTX>() {
		@Override
		public int compare(ForeignTX o1, ForeignTX o2) {
			if (o1.getRate() == o2.getRate()) {
				return o1.getDate().compareTo(o2.getDate());
			} else {
				return Double.valueOf(o1.getRate()).compareTo(o2.getRate());
			}
		}
	};

	/**
	 * 找出該貨幣的買入紀錄當中，可以湊滿賣出金額的交易紀錄。
	 * <p>
	 * 挑選優先順序：
	 * <ol>
	 * 	<li>
	 * 		買入價格比賣出價格低
	 * 		<ol>
	 * 			<li>價格較高</li>
	 * 			<li>日期較舊</li>
	 * 		</ol>
	 * 	</li>
	 * 	<li>
	 * 		買入價格比賣出價格高
	 * 		<ol>
	 * 			</li>價格較低</li>
	 * 			</li>日期較舊</li>
	 * 		</ol>
	 * 	</li>
	 * </ol>
	 *
	 * @param value 欲賣出的金額（正值）
	 * @return 「key 是買入紀錄、value 是該筆買入紀錄要負責賣出的數量」的 map
	 */
	public static HashMap<ForeignTX, Double> match(ArrayList<ForeignTX> txList, Currency currency, double value, double rate) {
		HashMap<ForeignTX, Double> result = new HashMap<>();

		//先找賣出會賺的交易紀錄
		for (ForeignTX tx : profitTX(txList, currency, value, rate)) {
			if (value <= tx.getBalance()) {
				result.put(tx, value);
				value = 0;
			} else {
				result.put(tx, tx.getBalance());
				value = CurrencyUtil.subtract(value, tx.getBalance());
			}

			if (value == 0) { return result; }
		}

		//value 還是沒用完，只好找賣出會賠的交易紀錄
		//其實跟上一段邏輯完全一樣，但是懶得抽共用了（才兩次！才兩次而已！ [被毆飛]）
		for (ForeignTX tx : lossTX(txList, currency, value, rate)) {
			if (value <= tx.getBalance()) {
				result.put(tx, value);
				value = 0;
			} else {
				result.put(tx, tx.getBalance());
				value = CurrencyUtil.subtract(value, tx.getBalance());
			}

			if (value == 0) { return result; }
		}

		return null;
	}

	public static ArrayList<ForeignTX> profitTX(ArrayList<ForeignTX> txList, Currency currency, double value, double rate) {
		ArrayList<ForeignTX> result = Lists.newArrayList(
			Iterables.filter(txList, new ProfitFilter(currency, rate))
		);

		Collections.sort(result, profitComparator);
		return result;
	}

	public static ArrayList<ForeignTX> lossTX(ArrayList<ForeignTX> txList, Currency currency, double value, double rate) {
		ArrayList<ForeignTX> result = Lists.newArrayList(
			Iterables.filter(txList, new LossFilter(currency, rate))
		);

		Collections.sort(result, lossComparator);
		return result;
	}

	private static class TxFilter implements Predicate<ForeignTX> {
		final Currency currency;
		final double rate;

		TxFilter(Currency c, double r) {
			currency = c;
			rate = r;
		}

		@Override
		public boolean apply(ForeignTX input) {
			return input.getCurrency() == currency && input.getBalance() > 0;
		}
	}

	private static class ProfitFilter extends TxFilter {
		ProfitFilter(Currency c, double r) {
			super(c, r);
		}

		@Override
		public boolean apply(ForeignTX input) {
			return super.apply(input) && input.getRate() < rate;
		}
	}

	private static class LossFilter extends TxFilter {
		LossFilter(Currency c, double r) {
			super(c, r);
		}

		@Override
		public boolean apply(ForeignTX input) {
			return super.apply(input) && input.getRate() >= rate;
		}
	}
}
