package us.dontcareabout.fx.client.component;

import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;

import us.dontcareabout.fx.client.Util;
import us.dontcareabout.fx.client.data.DataCenter;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent.ForeignTxReadyHandler;
import us.dontcareabout.fx.client.data.RateReadyEvent;
import us.dontcareabout.fx.client.data.RateReadyEvent.RateReadyHandler;
import us.dontcareabout.fx.client.data.TxSummary;
import us.dontcareabout.fx.client.ui.UiCenter;
import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.tool.CurrencyUtil;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;

public class SummaryLayer extends LayerSprite {
	private Currency currency;
	private double balance;

	private TextButton nameTS = new TextButton();
	private LTextSprite balanceTS = new LTextSprite();
	private LTextSprite costTS = new LTextSprite();
	private LTextSprite cashOutTS = new LTextSprite();
	private LTextSprite rateTS = new LTextSprite();

	public SummaryLayer(Currency c) {
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
		nameTS.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				UiCenter.changeCurrency(currency);
			}
		});

		makeUp(balanceTS);
		makeUp(costTS);
		makeUp(rateTS);
		makeUp(cashOutTS);

		DataCenter.addForeignTxReady(new ForeignTxReadyHandler() {
			@Override
			public void onForeignTxReady(ForeignTxReadyEvent event) {
				update();
				redraw();
			}
		});

		DataCenter.addRateReady(new RateReadyHandler() {
			@Override
			public void onRateReady(RateReadyEvent event) {
				update();
				redraw();
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
		if (!DataCenter.isAllReady()) { return; }

		TxSummary result = DataCenter.getSummary(currency);
		setBalance(result.balance);
		setCost(result.cost);
		setRate(DataCenter.getRateMap().get(currency));
	}

	private void setBalance(double value) {
		balance = value;
		balanceTS.setText("持有：" + Util.NUMBER_FORMAT.format(value));
	}

	private void setCost(double value) {
		costTS.setText("成本：" + Util.NUMBER_FORMAT.format(value));
	}

	private void setRate(double rate) {
		rateTS.setText("買匯：" + rate);
		cashOutTS.setText("套現：" + Util.NUMBER_FORMAT.format((rate * balance)));
	}
}
