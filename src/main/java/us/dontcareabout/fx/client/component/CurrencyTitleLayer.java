package us.dontcareabout.fx.client.component;

import us.dontcareabout.fx.client.ui.ChangeCurrencyEvent;
import us.dontcareabout.fx.client.ui.ChangeCurrencyEvent.ChangeCurrencyHandler;
import us.dontcareabout.fx.client.ui.UiCenter;
import us.dontcareabout.fx.shared.tool.CurrencyUtil;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;

public class CurrencyTitleLayer extends LayerSprite {
	private LTextSprite title = new LTextSprite();

	public CurrencyTitleLayer() {
		title.setFontSize(30);
		add(title);

		UiCenter.addChangeCurrency(new ChangeCurrencyHandler() {
			@Override
			public void onChangeCurrency(ChangeCurrencyEvent event) {
				title.setText(CurrencyUtil.name(event.data));
				redraw();
			}
		});
	}

	@Override
	protected void adjustMember() {
		title.setLX(20);
		title.setLY(10);
	}
}
