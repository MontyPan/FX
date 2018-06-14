package us.dontcareabout.fx.client.component;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import us.dontcareabout.fx.client.Util;
import us.dontcareabout.fx.shared.CapitalTX;
import us.dontcareabout.gxt.client.model.GetValueProvider;

public class CapitalGrid extends Grid<CapitalTX> {
	private static final Properties properties = GWT.create(Properties.class);

	public CapitalGrid() {
		super(new ListStore<>(properties.id()), genColumnModel());
		getView().setAutoFill(true);
	}

	private static ColumnModel<CapitalTX> genColumnModel() {
		ArrayList<ColumnConfig<CapitalTX, ?>> result = new ArrayList<>();
		result.add(new ColumnConfig<CapitalTX, String>(Properties.date, 100, "日期"));
		result.add(new ColumnConfig<CapitalTX, Double>(properties.income(), 100, "存入"));
		result.add(new ColumnConfig<CapitalTX, Double>(properties.outgoing(), 100, "轉出"));
		result.add(new ColumnConfig<CapitalTX, String>(properties.note(), 200, "備註"));
		return new ColumnModel<>(result);
	}

	interface Properties extends PropertyAccess<CapitalTX> {
		ValueProvider<CapitalTX, String> date = new GetValueProvider<CapitalTX, String>() {
			@Override
			public String getValue(CapitalTX object) {
				return Util.DATE_FORMAT.format(object.getDate());
			}
		};

		ModelKeyProvider<CapitalTX> id();
		ValueProvider<CapitalTX, Double> income();
		ValueProvider<CapitalTX, Double> outgoing();
		ValueProvider<CapitalTX, String> note();
	}
}
