package us.dontcareabout.fx.client.component;

import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;

import us.dontcareabout.fx.client.data.DataCenter;
import us.dontcareabout.fx.client.ui.ChangeCurrencyEvent;
import us.dontcareabout.fx.client.ui.ChangeCurrencyEvent.ChangeCurrencyHandler;
import us.dontcareabout.fx.client.ui.UiCenter;
import us.dontcareabout.fx.shared.AlertParam;
import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.tool.CurrencyUtil;
import us.dontcareabout.gxt.client.draw.Cursor;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.util.TextUtil;

public class CurrencyTitleLayer extends LayerSprite {
	private TextButton currencyTB = new TextButton("請選擇貨幣");
	private TextButton buyTB = new TextButton("買入");
	private TextButton sellTB = new TextButton("賣出");
	private AlertLayer alertLayer = new AlertLayer();

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

		alertLayer.setBgRadius(5);
		alertLayer.setHidden(true);
		alertLayer.setMemberCursor(Cursor.POINTER);
		alertLayer.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				UiCenter.alertParamDialog(currency);
			}
		});
		add(alertLayer);

		UiCenter.addChangeCurrency(new ChangeCurrencyHandler() {
			@Override
			public void onChangeCurrency(ChangeCurrencyEvent event) {
				currency = event.data;
				currencyTB.setText(CurrencyUtil.name(currency));
				buyTB.setHidden(false);
				sellTB.setHidden(false);
				alertLayer.setHidden(false);
				alertLayer.refresh();
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

		alertLayer.setLX(getWidth() - 410);
		alertLayer.setLY(5);
		alertLayer.resize(390, 50);
	}

	private class AlertLayer extends LayerSprite {
		private LTextSprite buyTitle = new LTextSprite("買匯：");
		private LTextSprite buyMin = new LTextSprite("--");
		private LTextSprite sellTitle = new LTextSprite("賣匯：");
		private LTextSprite sellMax = new LTextSprite("--");
		//拿掉 dummy text 之後沒再測試，結果後來發現這裡有詭異的問題
		//簡單地說結論：必須要等到第二次 refresh() 以後
		//buyMin, sellMax 的垂直位置才會在正確的位置
		//但是在 centerY() 當中 bbox.getHeight() 看不出什麼異常...... Orz
		//現在 buyMin, sellMax 在初始時給 dummy text
		//是為了在普通小數位數下第一次顯示能趨近正常...... (艸
		//XXX 有閒情逸致的時候再回頭來作 SSCCE 查真相

		AlertLayer() {
			setBgColor(RGB.ORANGE);

			buyTitle.setFontSize(24);
			buyTitle.setLX(10);
			buyMin.setLX(85);
			sellTitle.setFontSize(24);
			sellTitle.setLX(200);
			sellMax.setLX(275);

			add(buyTitle);
			add(buyMin);
			add(sellTitle);
			add(sellMax);
		}

		@Override
		protected void adjustMember() {
			TextUtil.autoResize(buyMin, 100, 35);
			TextUtil.autoResize(sellMax, 100, 35);

			centerY(buyTitle);
			centerY(buyMin);
			centerY(sellTitle);
			centerY(sellMax);
		}

		private void centerY(LTextSprite ts) {
			ts.setLY((getHeight() - ts.getBBox().getHeight()) / 2 + TextUtil.getYOffset(ts));
		}

		void refresh() {
			buyMin.setText("");
			sellMax.setText("");

			AlertParam ap = DataCenter.getAlertMap().get(currency);

			if (ap == null) { return; }

			buyMin.setText(ap.getBuyMin() == null ? "" : String.valueOf(ap.getBuyMin()));
			sellMax.setText(ap.getSellMax() == null ? "" : String.valueOf(ap.getSellMax()));
			adjustMember();
		}
	}
}
