package us.dontcareabout.fx.client.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.fx.client.data.AlertParamReadyEvent.AlertParamReadyHandler;

public class AlertParamReadyEvent extends GwtEvent<AlertParamReadyHandler> {
	public static final Type<AlertParamReadyHandler> TYPE = new Type<AlertParamReadyHandler>();

	@Override
	public Type<AlertParamReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AlertParamReadyHandler handler) {
		handler.onAlertParamReady(this);
	}

	public interface AlertParamReadyHandler extends EventHandler{
		public void onAlertParamReady(AlertParamReadyEvent event);
	}
}
