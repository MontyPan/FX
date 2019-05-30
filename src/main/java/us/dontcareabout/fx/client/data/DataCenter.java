package us.dontcareabout.fx.client.data;

import java.util.HashMap;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

import us.dontcareabout.fx.client.RpcService;
import us.dontcareabout.fx.client.RpcServiceAsync;
import us.dontcareabout.fx.client.data.AlertParamReadyEvent.AlertParamReadyHandler;
import us.dontcareabout.fx.client.data.CapitalTxReadyEvent.CapitalTxReadyHandler;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent.ForeignTxReadyHandler;
import us.dontcareabout.fx.client.data.RateReadyEvent.RateReadyHandler;
import us.dontcareabout.fx.client.ui.UiCenter;
import us.dontcareabout.fx.shared.AlertParam;
import us.dontcareabout.fx.shared.CapitalTX;
import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.ForeignTX;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();
	private final static RpcServiceAsync rpc = GWT.create(RpcService.class);

	private static List<CapitalTX> capitalList;
	public static List<CapitalTX> getCapitalList() {
		return capitalList;
	}

	public static void wantCapitalList() {
		rpc.getCapitalList(new AsyncCallback<List<CapitalTX>>() {
			@Override
			public void onSuccess(List<CapitalTX> result) {
				capitalList = result;
				eventBus.fireEvent(new CapitalTxReadyEvent());
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

	public static HandlerRegistration addCapitalTxReady(CapitalTxReadyHandler handler) {
		return eventBus.addHandler(CapitalTxReadyEvent.TYPE, handler);
	}

	private static List<ForeignTX> foreignList;
	public static List<ForeignTX> getForeignList() {
		return foreignList;
	}

	public static void wantForeignList() {
		rpc.getForeignList(new AsyncCallback<List<ForeignTX>>() {
			@Override
			public void onSuccess(List<ForeignTX> result) {
				foreignList = result;
				summaryMap.clear();
				eventBus.fireEvent(new ForeignTxReadyEvent());
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static HandlerRegistration addForeignTxReady(ForeignTxReadyHandler handler) {
		return eventBus.addHandler(ForeignTxReadyEvent.TYPE, handler);
	}

	public static void transaction(ForeignTX foreignTX) {
		rpc.saveTX(foreignTX, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				wantCapitalList();
				wantForeignList();
				UiCenter.closeDialog();
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

	private static HashMap<Currency,Double> rateMap;
	public static HashMap<Currency,Double> getRateMap() {
		return rateMap;
	}

	public static void wantRate() {
		rpc.getRateMap(new AsyncCallback<HashMap<Currency,Double>>() {
			@Override
			public void onSuccess(HashMap<Currency, Double> result) {
				rateMap = result;
				summaryMap.clear();
				eventBus.fireEvent(new RateReadyEvent());
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

	public static HandlerRegistration addRateReady(RateReadyHandler handler) {
		return eventBus.addHandler(RateReadyEvent.TYPE, handler);
	}

	public static boolean isAllReady() {
		return rateMap != null && foreignList != null;
	}

	// ======== 交易數據計算區 ======== //
	private static final HashMap<Currency, TxSummary> summaryMap = new HashMap<>();
	public static TxSummary getSummary(final Currency currency) {
		TxSummary result = summaryMap.get(currency);

		if (result == null) {
			result = calSummary(currency);
			summaryMap.put(currency, result);
		}

		return result;
	}

	private static TxSummary calSummary(final Currency currency) {
		Iterable<ForeignTX> txList = Iterables.filter(foreignList, new Predicate<ForeignTX>() {
			@Override
			public boolean apply(ForeignTX input) {
				return input.getCurrency() == currency;
			}
		});

		double profit = 0;
		double balance = 0;
		double cost = 0;

		for (ForeignTX tx : txList) {
			profit += tx.getProfit();
			balance += tx.getBalance();
			cost += tx.getBalance() * tx.getRate();
		}

		return new TxSummary(currency, balance, cost, profit);
	}
	// ================ //

	private static HashMap<Currency, AlertParam> alertMap;
	public static HashMap<Currency, AlertParam> getAlertMap() {
		return alertMap;
	}

	public static void saveAlertParam() {
		rpc.saveAlertMap(alertMap, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				wantAlertParam();
				UiCenter.closeDialog();
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

	public static void wantAlertParam() {
		rpc.getAlertMap(new AsyncCallback<HashMap<Currency,AlertParam>>() {
			@Override
			public void onSuccess(HashMap<Currency, AlertParam> result) {
				alertMap = result;
				eventBus.fireEvent(new AlertParamReadyEvent());
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

	public static HandlerRegistration addAlertParamReady(AlertParamReadyHandler handler) {
		return eventBus.addHandler(AlertParamReadyEvent.TYPE, handler);
	}

	public static void saveAlertParam(HashMap<Currency, AlertParam> map) {
		rpc.saveAlertMap(map, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				wantAlertParam();
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}
}
