package us.dontcareabout.fx.client.component;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import us.dontcareabout.fx.client.ui.UiCenter;
import us.dontcareabout.fx.shared.Currency;

public class ToolPanel extends Composite {
	private static ToolPanelUiBinder uiBinder = GWT.create(ToolPanelUiBinder.class);
	interface ToolPanelUiBinder extends UiBinder<Widget, ToolPanel> {}

	@UiField CurrencyCB currencyCB;

	public ToolPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("buyBtn")
	void buyHandler(SelectEvent se) {
		tx(true);
	}

	@UiHandler("sellBtn")
	void sellHandler(SelectEvent se) {
		tx(false);
	}

	private void tx(boolean isBuy) {
		Currency currency = currencyCB.getCurrentValue();

		if (currency == null) { return; }

		UiCenter.txDialog(currency, isBuy);
	}
}
