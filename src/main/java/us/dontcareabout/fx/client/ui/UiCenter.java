package us.dontcareabout.fx.client.ui;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class UiCenter {
	private final static Viewport viewport = new Viewport();

	public static void mask(String message) {
		viewport.mask(message);
	}

	public static void processing() {
		mask("處理中......");
	}

	public static void unmask() {
		viewport.unmask();
	}

	private static void switchTo(Widget widget) {
		viewport.clear();
		viewport.add(widget);
		viewport.forceLayout();
	}
}
