package us.dontcareabout.fx.client.component;

import java.util.HashMap;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

import us.dontcareabout.fx.client.data.DataCenter;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent.ForeignTxReadyHandler;
import us.dontcareabout.fx.client.data.RateReadyEvent;
import us.dontcareabout.fx.client.data.RateReadyEvent.RateReadyHandler;
import us.dontcareabout.fx.client.data.TxSummary;
import us.dontcareabout.fx.shared.Currency;

public 	class CurrencyMarquee extends HorizontalLayoutContainer {
	private static final HorizontalLayoutData CURRENCY_HLD = new HorizontalLayoutData(140, 1, new Margins(1));

	private final HashMap<Currency, CurrencyLayer> currencyMap = new HashMap<>();

	public CurrencyMarquee() {
		//要先 new 好 CurrencyLayer
		//這樣 CurrencyLayer 才能處理一開始的 ForeignTxReady / RateReady
		for (Currency c : Currency.values()) {
			if (c == Currency.NTD) { continue; }
			currencyMap.put(c, new CurrencyLayer(c));
		}

		setScrollMode(ScrollMode.AUTOX);

		DataCenter.addForeignTxReady(new ForeignTxReadyHandler() {
			@Override
			public void onForeignTxReady(ForeignTxReadyEvent event) {
				update();
			}
		});
		DataCenter.addRateReady(new RateReadyHandler() {
			@Override
			public void onRateReady(RateReadyEvent event) {
				update();
			}
		});
	}

	private void update() {
		if (!DataCenter.isAllReady()) { return; }

		clear();

		//只留下還有餘額的貨幣
		for (Currency c : Currency.values()) {
			if (c == Currency.NTD) { continue; }

			TxSummary sum = DataCenter.getSummary(c);

			if (sum.balance == 0) { continue; }

			add(currencyMap.get(c), CURRENCY_HLD);
		}

		forceLayout();
	}
}