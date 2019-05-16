package us.dontcareabout.fx.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DoubleField;
import com.sencha.gxt.widget.core.client.form.TextField;

import us.dontcareabout.fx.client.data.DataCenter;
import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.ForeignTX;
import us.dontcareabout.fx.shared.tool.CurrencyUtil;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.container.SimpleLayerContainer;

public class TxPanel extends Composite {
	private static TxPanelUiBinder uiBinder = GWT.create(TxPanelUiBinder.class);
	interface TxPanelUiBinder extends UiBinder<Widget, TxPanel> {}

	@UiField(provided=true) SimpleLayerContainer titleLayer;
	@UiField(provided=true) SimpleLayerContainer submitLayer;
	@UiField DateField date;
	@UiField DoubleField rate;
	@UiField DoubleField value;
	@UiField TextField note;

	private TextButton titleBtn = new TextButton();
	private TextButton submitBtn = new TextButton("確定送出");

	/** true 是買入、false 是賣出 */
	private boolean txType;
	private Currency currency;

	public TxPanel() {
		titleLayer = new SimpleLayerContainer(titleBtn);
		submitLayer = new SimpleLayerContainer(submitBtn);
		initWidget(uiBinder.createAndBindUi(this));

		submitBtn.setBgRadius(10);
		submitBtn.setTextColor(RGB.WHITE);
		submitBtn.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				transaction();
			}
		});
	}

	public void setType(Currency c, boolean isBuy) {
		currency = c;
		txType = isBuy;
		titleBtn.setText(isBuy ? "買進：" + CurrencyUtil.name(c) : "賣出：" + CurrencyUtil.name(c));
		titleBtn.setTextColor(isBuy ? RGB.RED : RGB.BLUE);
		submitBtn.setBgColor(isBuy ? RGB.RED : RGB.BLUE);
		date.clear();
		rate.clear();
		value.clear();
		note.clear();
	}

	private void transaction() {
		//懶得用 editor framework 了...... Zzzz
		ForeignTX foreignTX = new ForeignTX();
		foreignTX.setCurrency(currency);
		foreignTX.setDate(date.getCurrentValue() == null ? new DateWrapper().clearTime().asDate() : date.getCurrentValue());
		foreignTX.setNote(note.getCurrentValue());
		foreignTX.setRate(rate.getCurrentValue());
		foreignTX.setValue(txType ? value.getCurrentValue() : value.getCurrentValue() * -1);
		DataCenter.transaction(foreignTX);
	}
}
