package us.dontcareabout.fx.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.DoubleField;

import us.dontcareabout.fx.client.data.DataCenter;
import us.dontcareabout.fx.shared.AlertParam;
import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.tool.CurrencyUtil;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.container.SimpleLayerContainer;

public class AlertParamPanel extends Composite {
	private static AlertParamPanelUiBinder uiBinder = GWT.create(AlertParamPanelUiBinder.class);
	interface AlertParamPanelUiBinder extends UiBinder<Widget, AlertParamPanel> {}

	@UiField(provided=true) SimpleLayerContainer titleLayer;
	@UiField(provided=true) SimpleLayerContainer submitLayer;
	@UiField DoubleField buyMin;
	@UiField DoubleField sellMax;

	private TextButton titleBtn = new TextButton();
	private TextButton submitBtn = new TextButton("確定送出");

	private AlertParam ap;

	public AlertParamPanel() {
		titleLayer = new SimpleLayerContainer(titleBtn);
		submitLayer = new SimpleLayerContainer(submitBtn);
		initWidget(uiBinder.createAndBindUi(this));

		submitBtn.setBgRadius(10);
		submitBtn.setBgColor(RGB.BLUE);
		submitBtn.setTextColor(RGB.WHITE);
		submitBtn.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				save();
			}
		});
	}

	public void setData(Currency currency) {
		ap = DataCenter.getAlertMap().get(currency);

		titleBtn.setText(CurrencyUtil.name(currency) + "價格警示設定");
		buyMin.setValue(ap.getBuyMin());
		sellMax.setValue(ap.getSellMax());
	}

	private void save() {
		ap.setBuyMin(buyMin.getCurrentValue());
		ap.setSellMax(sellMax.getCurrentValue());
		DataCenter.saveAlertParam();
	}
}
