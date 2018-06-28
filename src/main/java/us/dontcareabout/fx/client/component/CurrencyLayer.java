package us.dontcareabout.fx.client.component;

import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.fx.client.data.DataCenter;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent.ForeignTxReadyHandler;
import us.dontcareabout.fx.client.data.TxSummary;
import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.tool.CurrencyUtil;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;

public class CurrencyLayer extends LayerSprite {
	private Currency currency;
	private double balance;

	private TextButton nameTS = new TextButton();
	private LTextSprite balanceTS = new LTextSprite();
	private LTextSprite costTS = new LTextSprite();
	private LTextSprite cashOutTS = new LTextSprite();
	private LTextSprite rateTS = new LTextSprite();

	public CurrencyLayer(Currency c) {
		currency = c;

		setBgColor(RGB.GREEN);
		add(nameTS);
		add(balanceTS);
		add(costTS);
		add(cashOutTS);
		add(rateTS);

		nameTS.setTextColor(RGB.WHITE);
		nameTS.setBgColor(RGB.BLUE);
		nameTS.setText(CurrencyUtil.name(currency));

		makeUp(balanceTS);
		makeUp(costTS);
		makeUp(rateTS);
		makeUp(cashOutTS);

		DataCenter.addForeignTxReady(new ForeignTxReadyHandler() {
			@Override
			public void onForeignTxReady(ForeignTxReadyEvent event) {
				update();
			}
		});
	}

	@Override
	protected void adjustMember() {
		nameTS.setLX(0);
		nameTS.setLY(0);
		nameTS.resize(getWidth(), 30);

		rateTS.setLX(10);
		rateTS.setLY(35);

		balanceTS.setLX(10);
		balanceTS.setLY(65);

		costTS.setLX(10);
		costTS.setLY(85);

		cashOutTS.setLX(10);
		cashOutTS.setLY(105);
	}

	private void makeUp(LTextSprite ts) {
		ts.setFill(RGB.WHITE);
		ts.setFontSize(18);
	}

	private void update() {
		TxSummary result = DataCenter.getSummary(currency);
		setBalance(result.balance);
		setCost(result.cost);
	}


	private void setBalance(double value) {
		balance = value;
		balanceTS.setText("持有：" + value);
	}

	private void setCost(double value) {
		costTS.setText("成本：" + value);
	}

	private void setRate(double rate) {
		rateTS.setText("買匯：" + rate);
		cashOutTS.setText("套現：" + (rate * balance));
	}
}
