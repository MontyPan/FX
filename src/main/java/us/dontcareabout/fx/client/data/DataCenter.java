package us.dontcareabout.fx.client.data;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

import us.dontcareabout.fx.client.RpcService;
import us.dontcareabout.fx.client.RpcServiceAsync;
import us.dontcareabout.fx.client.data.CapitalTxReadyEvent.CapitalTxReadyHandler;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent.ForeignTxReadyHandler;
import us.dontcareabout.fx.client.ui.UiCenter;
import us.dontcareabout.fx.shared.CapitalTX;
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
}
