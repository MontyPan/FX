package us.dontcareabout.fx.client.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.fx.client.data.ForeignTxReadyEvent.ForeignTxReadyHandler;

public class ForeignTxReadyEvent extends GwtEvent<ForeignTxReadyHandler> {
	public static final Type<ForeignTxReadyHandler> TYPE = new Type<ForeignTxReadyHandler>();

	@Override
	public Type<ForeignTxReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ForeignTxReadyHandler handler) {
		handler.onForeignTxReady(this);
	}

	public interface ForeignTxReadyHandler extends EventHandler{
		public void onForeignTxReady(ForeignTxReadyEvent event);
	}
}
