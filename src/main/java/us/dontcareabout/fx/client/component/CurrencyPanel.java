package us.dontcareabout.fx.client.component;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import us.dontcareabout.fx.client.ui.UiCenter;

public class CurrencyPanel extends Composite {
	private static CurrencyPanelUiBinder uiBinder = GWT.create(CurrencyPanelUiBinder.class);
	interface CurrencyPanelUiBinder extends UiBinder<Widget, CurrencyPanel> {}

	@UiField CurrencyCB currencyCB;

	public CurrencyPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void cleanValue() {
		currencyCB.clear();
	}

	@UiHandler("submitBtn")
	void buyHandler(SelectEvent se) {
		if (currencyCB.getCurrentValue() != null) {
			UiCenter.changeCurrency(currencyCB.getCurrentValue());
		}
		UiCenter.closeDialog();
	}
}
