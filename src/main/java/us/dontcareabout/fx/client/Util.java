package us.dontcareabout.fx.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

public class Util {
	public static final int SCROLL_HEIGHT = 16;
	public static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("yyyy/MM/dd");
	public static final NumberFormat NUMBER_FORMAT = NumberFormat.getFormat("#####.#");

	public static class RightColumnConfig<M, N> extends ColumnConfig<M, N> {

		public RightColumnConfig(ValueProvider<? super M, N> valueProvider, int width, String header) {
			super(valueProvider, width, header);
			setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		}
	}
}
