package us.dontcareabout.fx.client.component;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiConstructor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import us.dontcareabout.fx.client.Util;
import us.dontcareabout.fx.client.Util.RightColumnConfig;
import us.dontcareabout.fx.client.data.DataCenter;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent.ForeignTxReadyHandler;
import us.dontcareabout.fx.client.ui.ChangeCurrencyEvent;
import us.dontcareabout.fx.client.ui.ChangeCurrencyEvent.ChangeCurrencyHandler;
import us.dontcareabout.fx.client.ui.UiCenter;
import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.ForeignTX;
import us.dontcareabout.gxt.client.model.GetValueProvider;

public class ForeignGrid extends Grid<ForeignTX> {
	private static final Properties properties = GWT.create(Properties.class);

	private Filter filter = new Filter();

	@UiConstructor
	public ForeignGrid(boolean isBuy) {
		super(new ListStore<>(properties.id()), genColumnModel(isBuy));

		filter.setBuyOrSell(isBuy);

		getStore().addFilter(filter);
		getStore().setEnableFilters(true);

		getView().setAutoFill(true);
		getView().setEmptyText("請選擇貨幣");

		DataCenter.addForeignTxReady(new ForeignTxReadyHandler() {
			@Override
			public void onForeignTxReady(ForeignTxReadyEvent event) {
				getStore().clear();
				getStore().addAll(DataCenter.getForeignList());
				getView().refresh(false);
			}
		});

		UiCenter.addChangeCurrency(new ChangeCurrencyHandler() {
			@Override
			public void onChangeCurrency(ChangeCurrencyEvent event) {
				changeCurrency(event.data);
			}
		});
	}

	private void changeCurrency(Currency currency) {
		if (filter.getCurrency() == currency) { return; }

		filter.setCurrency(currency);
		getStore().setEnableFilters(false);
		getStore().setEnableFilters(true);
	}

	private static ColumnModel<ForeignTX> genColumnModel(boolean isBuy) {
		ArrayList<ColumnConfig<ForeignTX, ?>> result = new ArrayList<>();
		result.add(new ColumnConfig<ForeignTX, String>(Properties.date, 150, "日期"));
		result.add(new RightColumnConfig<>(properties.rate(), 100, "匯率"));

		if (isBuy) {
			result.add(new RightColumnConfig<>(properties.income(), 100, "存入"));
			result.add(new RightColumnConfig<>(properties.balance(), 100, "結餘"));
		} else {
			result.add(new RightColumnConfig<>(properties.outgoing(), 100, "轉出"));
			result.add(new RightColumnConfig<>(properties.profit(), 100, "盈餘"));
		}

		result.add(new ColumnConfig<ForeignTX, String>(properties.note(), 200, "備註"));
		return new ColumnModel<>(result);
	}

	interface Properties extends PropertyAccess<ForeignTX> {
		ValueProvider<ForeignTX, String> date = new GetValueProvider<ForeignTX, String>() {
			@Override
			public String getValue(ForeignTX object) {
				return Util.DATE_FORMAT.format(object.getDate());
			}
		};

		ModelKeyProvider<ForeignTX> id();
		ValueProvider<ForeignTX, Double> income();
		ValueProvider<ForeignTX, Double> outgoing();
		ValueProvider<ForeignTX, Double> rate();
		ValueProvider<ForeignTX, String> note();
		ValueProvider<ForeignTX, Double> profit();
		ValueProvider<ForeignTX, Double> balance();
	}

	private class Filter implements StoreFilter<ForeignTX> {
		private Currency currency;
		private boolean isBuy;

		void setCurrency(Currency c) { this.currency = c; }

		void setBuyOrSell(boolean isBuy) { this.isBuy = isBuy; }

		Currency getCurrency() { return currency; }

		@Override
		public boolean select(Store<ForeignTX> store, ForeignTX parent, ForeignTX item) {
			if (currency == null) { return false; }
			if (currency != item.getCurrency()) { return false; }
			return isBuy ? item.getBalance() > 0 : item.getValue() < 0;
		}
	}
}
