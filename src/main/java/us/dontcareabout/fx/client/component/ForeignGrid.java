package us.dontcareabout.fx.client.component;

import java.util.ArrayList;
import java.util.Random;

import com.google.gwt.core.shared.GWT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import us.dontcareabout.fx.client.Util;
import us.dontcareabout.fx.shared.ForeignTX;
import us.dontcareabout.gxt.client.model.GetValueProvider;

public class ForeignGrid extends Grid<ForeignTX> {
	private static final Properties properties = GWT.create(Properties.class);

	public ForeignGrid() {
		super(new ListStore<>(properties.id()), genColumnModel());
		getView().setAutoFill(true);
		for (int i = 0 ; i < 10; i++) {
			getStore().add(mock());
		}
	}

	int counter;
	private ForeignTX mock() {
		ForeignTX result = new ForeignTX();
		result.setId(counter++);
		result.setDate(new DateWrapper().addDays(-counter).asDate());
		result.setValue(new Random().nextInt(10000));
		result.setRate(new Random().nextDouble());
		result.setNote(result.getId() + "??");
		return result;
	}

	private static ColumnModel<ForeignTX> genColumnModel() {
		ArrayList<ColumnConfig<ForeignTX, ?>> result = new ArrayList<>();
		result.add(new ColumnConfig<ForeignTX, String>(Properties.date, 100, "日期"));
		result.add(new ColumnConfig<ForeignTX, Double>(properties.income(), 100, "存入"));
		result.add(new ColumnConfig<ForeignTX, Double>(properties.outgoing(), 100, "轉出"));
		result.add(new ColumnConfig<ForeignTX, Double>(properties.rate(), 100, "匯率"));
		result.add(new ColumnConfig<ForeignTX, String>(properties.note(), 200, "備註"));
		return new ColumnModel<>(result);
	}

	interface Properties extends PropertyAccess<ForeignTX> {
		ValueProvider<ForeignTX, String> date = new GetValueProvider<ForeignTX, String>() {
			@Override
			public String getValue(ForeignTX object) {
				return Util.DATE_FORMAT.format(object.getDate());
			}
		};

		ModelKeyProvider<ForeignTX> id();
		ValueProvider<ForeignTX, Double> income();
		ValueProvider<ForeignTX, Double> outgoing();
		ValueProvider<ForeignTX, Double> rate();
		ValueProvider<ForeignTX, String> note();
	}
}
