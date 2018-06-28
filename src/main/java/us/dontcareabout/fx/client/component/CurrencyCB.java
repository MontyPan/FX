package us.dontcareabout.fx.client.component;

import java.util.Arrays;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.tool.CurrencyUtil;

public class CurrencyCB extends ComboBox<Currency> {
	public CurrencyCB() {
		super(
			new ListStore<>(
				new ModelKeyProvider<Currency>() {
					@Override
					public String getKey(Currency item) {
						return item.name();
					}
				}
			),
			new LabelProvider<Currency>() {
				@Override
				public String getLabel(Currency item) {
					return CurrencyUtil.name(item);
				}
			}
		);
		getStore().addAll(Arrays.asList(Currency.values()));
		setTriggerAction(TriggerAction.ALL);
	}
}
