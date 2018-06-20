package us.dontcareabout.fx.client.ui;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.Viewport;

import us.dontcareabout.fx.client.component.TxPanel;
import us.dontcareabout.fx.shared.Currency;

public class UiCenter {
	private final static Viewport viewport = new Viewport();
	static {
		RootPanel.get().add(viewport);
	}

	private final static Window dialog = new Window();
	static {
		dialog.setModal(true);
		dialog.setResizable(false);
	}

	public static void mask(String message) {
		viewport.mask(message);
	}

	public static void processing() {
		mask("處理中......");
	}

	public static void unmask() {
		viewport.unmask();
	}

	public static void closeDialog() {
		dialog.hide();
	}

	private static void dialog(Widget widget, int width, int height) {
		dialog.clear();
		dialog.add(widget);
		dialog.show();
		dialog.setPixelSize(width, height);
		dialog.center();
	}

	private static void switchTo(Widget widget) {
		viewport.clear();
		viewport.add(widget);
		viewport.forceLayout();
	}

	//////////////////////////////////////////////////////////////////

	private static Widget mainView;
	public static void mainView() {
		if (mainView == null) {
			mainView = new MainView();
		}

		switchTo(mainView);
	}

	//////////////////////////////////////////////////////////////////

	private static TxPanel txPanel;
	public static void txDialog(Currency currency, boolean isBuy) {
		if (txPanel == null) { txPanel = new TxPanel(); }

		txPanel.setType(currency, isBuy);
		dialog(txPanel, 200, 250);
	}
}
