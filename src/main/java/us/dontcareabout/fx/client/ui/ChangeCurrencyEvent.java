package us.dontcareabout.fx.client.ui;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.fx.client.ui.ChangeCurrencyEvent.ChangeCurrencyHandler;
import us.dontcareabout.fx.shared.Currency;

public class ChangeCurrencyEvent extends GwtEvent<ChangeCurrencyHandler> {
	public static final Type<ChangeCurrencyHandler> TYPE = new Type<ChangeCurrencyHandler>();

	public final Currency data;

	public ChangeCurrencyEvent(Currency currency) {
		this.data = currency;
	}

	@Override
	public Type<ChangeCurrencyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ChangeCurrencyHandler handler) {
		handler.onChangeCurrency(this);
	}

	public interface ChangeCurrencyHandler extends EventHandler{
		public void onChangeCurrency(ChangeCurrencyEvent event);
	}
}
