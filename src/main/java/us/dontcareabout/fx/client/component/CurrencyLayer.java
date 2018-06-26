package us.dontcareabout.fx.client.component;

import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;

import us.dontcareabout.fx.client.Util;
import us.dontcareabout.fx.client.ui.UiCenter;
import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.tool.CurrencyUtil;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;

public class CurrencyLayer extends LayerSprite {
	private Currency currency;
	private double summary;
	private double rate;

	private TextButton nameTS = new TextButton();
	private LTextSprite summaryTS = new LTextSprite();
	private LTextSprite costTS = new LTextSprite();
	private LTextSprite cashOutTS = new LTextSprite();
	private LTextSprite rateTS = new LTextSprite();
	private TextButton buyBtn = new TextButton("買入");
	private TextButton sellBtn = new TextButton("賣出");

	public CurrencyLayer(Currency c) {
		currency = c;

		setBgColor(RGB.GREEN);
		add(nameTS);
		add(summaryTS);
		add(costTS);
		add(cashOutTS);
		add(rateTS);
		add(buyBtn);
		add(sellBtn);

		nameTS.setTextColor(RGB.WHITE);
		nameTS.setBgColor(RGB.BLUE);
		nameTS.setText(CurrencyUtil.name(currency));

		makeUp(summaryTS);
		makeUp(costTS);
		makeUp(rateTS);
		makeUp(cashOutTS);

		makeUp(buyBtn);
		makeUp(sellBtn);

		buyBtn.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				UiCenter.txDialog(currency, true);
			}
		});

		sellBtn.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				UiCenter.txDialog(currency, false);
			}
		});
	}

	public void setSummary(double value) {
		summary = value;
		summaryTS.setText("持有：" + value);
	}

	public void setCost(double value) {
		costTS.setText("成本：" + value);
	}

	public void setRate(double value) {
		rate = value;
		rateTS.setText("買匯：" + value);
		cashOutTS.setText("套現：" + (rate * summary));
	}

	@Override
	protected void adjustMember() {
		nameTS.setLX(0);
		nameTS.setLY(0);
		nameTS.resize(getWidth(), 30);

		summaryTS.setLX(20);
		summaryTS.setLY(60);

		costTS.setLX(20);
		costTS.setLY(90);

		rateTS.setLX(20);
		rateTS.setLY(130);

		cashOutTS.setLX(20);
		cashOutTS.setLY(160);

		buyBtn.setLX(5);
		buyBtn.setLY(getHeight() - 35 - Util.SCROLL_HEIGHT);
		buyBtn.resize(80, 30);

		sellBtn.setLX(90);
		sellBtn.setLY(getHeight() - 35 - Util.SCROLL_HEIGHT);
		sellBtn.resize(80, 30);
	}

	private void makeUp(TextButton btn) {
		btn.setBgColor(RGB.YELLOW);
		btn.setBgRadius(10);
	}

	private void makeUp(LTextSprite ts) {
		ts.setFill(RGB.WHITE);
		ts.setFontSize(22);
	}
}
