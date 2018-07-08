package us.dontcareabout.fx.client.component;

import java.util.Comparator;
import java.util.HashMap;

import com.google.common.collect.ComparisonChain;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreSortEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

import us.dontcareabout.fx.client.data.DataCenter;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent.ForeignTxReadyHandler;
import us.dontcareabout.fx.client.data.RateReadyEvent;
import us.dontcareabout.fx.client.data.RateReadyEvent.RateReadyHandler;
import us.dontcareabout.fx.client.data.TxSummary;
import us.dontcareabout.fx.shared.Currency;

public 	class SummaryList extends HorizontalLayoutContainer {
	private static final HorizontalLayoutData CURRENCY_HLD = new HorizontalLayoutData(140, 1, new Margins(1));

	private final ListStore<TxSummary> store = new ListStore<>(new ModelKeyProvider<TxSummary>() {
		@Override
		public String getKey(TxSummary item) {
			return item.currency.name();
		}
	});
	private final HashMap<Currency, SummaryLayer> currencyMap = new HashMap<>();

	public SummaryList() {
		//要先 new 好 CurrencyLayer
		//這樣 CurrencyLayer 才能處理一開始的 ForeignTxReady / RateReady
		for (Currency c : Currency.values()) {
			if (c == Currency.NTD) { continue; }
			currencyMap.put(c, new SummaryLayer(c));
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
		store.addStoreHandlers(new StoreHandlers<TxSummary>(){
			@Override
			public void onAdd(StoreAddEvent<TxSummary> event) { refresh(); }

			@Override
			public void onRemove(StoreRemoveEvent<TxSummary> event) { refresh(); }

			@Override
			public void onFilter(StoreFilterEvent<TxSummary> event) { refresh(); }

			@Override
			public void onClear(StoreClearEvent<TxSummary> event) { refresh(); }

			@Override
			public void onUpdate(StoreUpdateEvent<TxSummary> event) { refresh(); }

			@Override
			public void onDataChange(StoreDataChangeEvent<TxSummary> event) { refresh(); }

			@Override
			public void onRecordChange(StoreRecordChangeEvent<TxSummary> event) { refresh(); }

			@Override
			public void onSort(StoreSortEvent<TxSummary> event) { refresh(); }
		});
		store.addFilter(new StoreFilter<TxSummary>() {
			@Override
			public boolean select(Store<TxSummary> store, TxSummary parent, TxSummary item) {
				//只留下還有餘額的貨幣
				return item.balance != 0;
			}
		});
		store.setEnableFilters(true);
		store.addSortInfo(
			new StoreSortInfo<TxSummary>(
				new Comparator<TxSummary>() {
					@Override
					public int compare(TxSummary o1, TxSummary o2) {
						return ComparisonChain.start().compare(o1.cost, o2.cost).result();
					}
				},
				SortDir.DESC
			)
		);
	}

	private void update() {
		if (!DataCenter.isAllReady()) { return; }

		store.clear();

		for (Currency c : Currency.values()) {
			if (c == Currency.NTD) { continue; }

			store.add(DataCenter.getSummary(c));
		}
	}

	private void refresh() {
		clear();

		for (TxSummary sum : store.getAll()) {
			add(currencyMap.get(sum.currency), CURRENCY_HLD);
		}

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				forceLayout();
			}
		});
	}
}