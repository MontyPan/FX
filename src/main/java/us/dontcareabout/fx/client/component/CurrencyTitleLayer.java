package us.dontcareabout.fx.client.component;

import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;

import us.dontcareabout.fx.client.ui.ChangeCurrencyEvent;
import us.dontcareabout.fx.client.ui.ChangeCurrencyEvent.ChangeCurrencyHandler;
import us.dontcareabout.fx.client.ui.UiCenter;
import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.tool.CurrencyUtil;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;

public class CurrencyTitleLayer extends LayerSprite {
	private TextButton currencyTB = new TextButton("請選擇貨幣");
	private TextButton buyTB = new TextButton("買入");
	private TextButton sellTB = new TextButton("賣出");

	private Currency currency;

	public CurrencyTitleLayer() {
		currencyTB.setBgColor(RGB.BLUE);
		currencyTB.setTextColor(RGB.WHITE);
		currencyTB.setBgRadius(5);
		currencyTB.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				UiCenter.currencyDialog();
			}
		});
		add(currencyTB);

		buyTB.setBgColor(RGB.WHITE);
		buyTB.setTextColor(RGB.RED);
		buyTB.setBgRadius(5);
		buyTB.setHidden(true);
		buyTB.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				UiCenter.txDialog(currency, true);
			}
		});
		add(buyTB);

		sellTB.setBgColor(RGB.RED);
		sellTB.setTextColor(RGB.WHITE);
		sellTB.setBgRadius(5);
		sellTB.setHidden(true);
		sellTB.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				UiCenter.txDialog(currency, false);
			}
		});
		add(sellTB);

		UiCenter.addChangeCurrency(new ChangeCurrencyHandler() {
			@Override
			public void onChangeCurrency(ChangeCurrencyEvent event) {
				currency = event.data;
				currencyTB.setText(CurrencyUtil.name(currency));
				buyTB.setHidden(false);
				sellTB.setHidden(false);
				redraw();
			}
		});
	}

	@Override
	protected void adjustMember() {
		currencyTB.setLX(20);
		currencyTB.setLY(5);
		currencyTB.resize(140, 50);

		buyTB.setLX(200);
		buyTB.setLY(10);
		buyTB.resize(80, 40);

		sellTB.setLX(290);
		sellTB.setLY(10);
		sellTB.resize(80, 40);
	}
}
