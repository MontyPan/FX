package us.dontcareabout.fx.client.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.fx.client.data.CapitalTxReadyEvent.CapitalTxReadyHandler;

public class CapitalTxReadyEvent extends GwtEvent<CapitalTxReadyHandler> {
	public static final Type<CapitalTxReadyHandler> TYPE = new Type<CapitalTxReadyHandler>();

	@Override
	public Type<CapitalTxReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CapitalTxReadyHandler handler) {
		handler.onCapitalTxReady(this);
	}

	public interface CapitalTxReadyHandler extends EventHandler{
		public void onCapitalTxReady(CapitalTxReadyEvent event);
	}
}
