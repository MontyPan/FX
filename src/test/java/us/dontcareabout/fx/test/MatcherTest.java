package us.dontcareabout.fx.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import com.google.common.collect.Lists;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.ForeignTX;
import us.dontcareabout.fx.shared.tool.Matcher;

public class MatcherTest {
	private ArrayList<ForeignTX> txList = new ArrayList<>();

	@Before
	public void setup() {
		double[] rate	= {30,  31,	 32,  29,  30,  33,  32};
		double[] value	= {100, 100, 100, 100, 100, 100, 100};

		for (int i = 0; i < value.length; i++) {
			txList.add(mockUSD(i, new Date(i*86400000), value[i], rate[i]));
		}
	}

	/** 確認是檢查 balance 而不是 value */
	@Test
	public void balance_profit() {
		txList.get(1).sell(10);
		Assert.assertEquals(
			toStringAnswer(Matcher.match(txList, Currency.USD, 100, 31.5)), "0:10.0;1:90.0;"
		);
	}

	/** 確認是檢查 balance 而不是 value */
	public void balance_both() {
		txList.get(3).sell(50);
		Assert.assertEquals(
			toStringAnswer(Matcher.match(txList, Currency.USD, 150, 29.9)), "0:50.0;3:50.0;"
		);
	}

	/** 確認是檢查 balance 而不是 value */
	@Test
	public void balance_loss() {
		txList.get(5).sell(50);
		Assert.assertEquals(
			toStringAnswer(Matcher.match(txList, Currency.USD, 100, 34)), "2:50.0;5:50.0;"
		);
	}

	/**	單筆大於賣出數量 */
	@Test
	public void profit_1() {
		Assert.assertEquals(
			toStringAnswer(Matcher.match(txList, Currency.USD, 99, 31.5)), "1:99.0;"
		);
	}

	/**	單筆等於賣出數量 */
	@Test
	public void profit_2() {
		Assert.assertEquals(
			toStringAnswer(Matcher.match(txList, Currency.USD, 100, 31.5)), "1:100.0;"
		);
	}

	/**	多筆大於賣出數量 */
	@Test
	public void profit_3() {
		Assert.assertEquals(
			toStringAnswer(Matcher.match(txList, Currency.USD, 150, 31.5)), "0:50.0;1:100.0;"
		);
	}

	/** 同 {@link #profit_3()}，但是第二價位兩筆可選，要選日期早的 */
	@Test
	public void profit_4() {
		Assert.assertEquals(
			toStringAnswer(Matcher.match(txList, Currency.USD, 150, 29)), "0:50.0;3:100.0;"
		);
	}

	/** 有 profit 持有數量不夠、導致用到會 loss 的數量 */
	@Test
	public void both() {
		Assert.assertEquals(
			toStringAnswer(Matcher.match(txList, Currency.USD, 150, 29.9)), "0:50.0;3:100.0;"
		);
	}

	/**	單筆大於賣出數量 */
	@Test
	public void loss_1() {
		Assert.assertEquals(
			toStringAnswer(Matcher.match(txList, Currency.USD, 99, 34)), "5:99.0;"
		);
	}

	@Test
	public void loss_2() {
		Assert.assertEquals(
			toStringAnswer(Matcher.match(txList, Currency.USD, 150, 34)), "2:50.0;5:100.0;"
		);
	}

	/** 根本沒有該貨幣的買入紀錄 */
	@Test
	public void exception_1() {
		Assert.assertNull(
			Matcher.match(txList, Currency.AUD, 100, 1)
		);
	}

	/** 該貨幣持有金額不足賣出價格 */
	@Test
	public void exception_2() {
		Assert.assertNull(
			Matcher.match(txList, Currency.USD, 800, 31.5)
		);
	}

	private static String toStringAnswer(HashMap<ForeignTX, Double> result) {
		StringBuffer sb = new StringBuffer();
		ArrayList<ForeignTX> txList = Lists.newArrayList(result.keySet());
		Collections.sort(txList, new Comparator<ForeignTX>() {
			@Override
			public int compare(ForeignTX o1, ForeignTX o2) {
				return o1.getId() - o2.getId();
			}
		});

		for(ForeignTX tx : txList) {
			sb.append(tx.getId() + ":" + result.get(tx) + ";");
		}

		return sb.toString();
	}

	private static ForeignTX mock(int id, Currency c, Date date, double value, double rate) {
		ForeignTX tx = new ForeignTX();
		tx.setId(id);
		tx.setCurrency(c);
		tx.setDate(date);
		tx.setRate(rate);
		tx.setValue(value);
		return tx;
	}

	private static ForeignTX mockUSD(int id, Date date, double value, double rate) {
		return mock(id, Currency.USD, date, value, rate);
	}
}
