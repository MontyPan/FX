package us.dontcareabout.fx.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.widget.core.client.Composite;

import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.container.SimpleLayerContainer;

public class TxPanel extends Composite {
	private static TxPanelUiBinder uiBinder = GWT.create(TxPanelUiBinder.class);
	interface TxPanelUiBinder extends UiBinder<Widget, TxPanel> {}

	@UiField(provided=true) SimpleLayerContainer titleLayer;
	@UiField(provided=true) SimpleLayerContainer submitLayer;

	private TextButton titleBtn = new TextButton();
	private TextButton submitBtn = new TextButton("確定送出");

	public TxPanel() {
		titleLayer = new SimpleLayerContainer(titleBtn);
		submitLayer = new SimpleLayerContainer(submitBtn);
		initWidget(uiBinder.createAndBindUi(this));

		submitBtn.setBgRadius(10);
		submitBtn.setTextColor(RGB.WHITE);
	}

	public void setType(boolean isBuy) {
		titleBtn.setText(isBuy ? "買進來" : "賣出去");
		titleBtn.setTextColor(isBuy ? RGB.RED : RGB.BLUE);
		submitBtn.setBgColor(isBuy ? RGB.RED : RGB.BLUE);
	}
}
