package us.dontcareabout.fx.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import us.dontcareabout.fx.client.component.CurrencyTitleLayer;

public class ForeignView extends Composite {
	private static ForeignViewUiBinder uiBinder = GWT.create(ForeignViewUiBinder.class);
	interface ForeignViewUiBinder extends UiBinder<Widget, ForeignView> {}

	@UiField(provided=true) CurrencyTitleLayer title;

	public ForeignView() {
		//XXX LayerSprite 沒有 setLayoutData() 只好這樣搞...
		title = new CurrencyTitleLayer();
		title.asWidget().setLayoutData(new VerticalLayoutData(1, 60));

		initWidget(uiBinder.createAndBindUi(this));
	}
}
