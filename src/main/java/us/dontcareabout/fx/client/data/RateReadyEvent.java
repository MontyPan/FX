package us.dontcareabout.fx.client.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.fx.client.data.RateReadyEvent.RateReadyHandler;

public class RateReadyEvent extends GwtEvent<RateReadyHandler> {
	public static final Type<RateReadyHandler> TYPE = new Type<RateReadyHandler>();

	@Override
	public Type<RateReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RateReadyHandler handler) {
		handler.onRateReady(this);
	}

	public interface RateReadyHandler extends EventHandler{
		public void onRateReady(RateReadyEvent event);
	}
}
