package us.dontcareabout.fx.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;

import us.dontcareabout.fx.client.data.AlertParamReadyEvent;
import us.dontcareabout.fx.client.data.AlertParamReadyEvent.AlertParamReadyHandler;
import us.dontcareabout.fx.client.data.CapitalTxReadyEvent;
import us.dontcareabout.fx.client.data.CapitalTxReadyEvent.CapitalTxReadyHandler;
import us.dontcareabout.fx.client.data.DataCenter;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent;
import us.dontcareabout.fx.client.data.ForeignTxReadyEvent.ForeignTxReadyHandler;
import us.dontcareabout.fx.client.data.RateReadyEvent;
import us.dontcareabout.fx.client.data.RateReadyEvent.RateReadyHandler;
import us.dontcareabout.fx.client.ui.UiCenter;
import us.dontcareabout.gwt.client.GFEP;

public class FXEP extends GFEP {
	@Override
	protected String version() { return "0.0.2"; }

	@Override
	protected String defaultLocale() { return "zh_TW"; }

	private int counter = 4;
	private GroupingHandlerRegistration ghr = new GroupingHandlerRegistration();
	private void initCheck() {
		counter--;
		if (counter == 0) {
			ghr.removeHandler();
			UiCenter.unmask();
		}
	}

	@Override
	protected void start() {
		UiCenter.mainView();
		ghr.add(
			DataCenter.addCapitalTxReady(new CapitalTxReadyHandler() {
				@Override
				public void onCapitalTxReady(CapitalTxReadyEvent event) {
					initCheck();
				}
			})
		);
		ghr.add(
			DataCenter.addForeignTxReady(new ForeignTxReadyHandler() {
				@Override
				public void onForeignTxReady(ForeignTxReadyEvent event) {
					initCheck();
				}
			})
		);
		ghr.add(
			DataCenter.addRateReady(new RateReadyHandler() {
				@Override
				public void onRateReady(RateReadyEvent event) {
					initCheck();
				}
			})
		);
		ghr.add(
			DataCenter.addAlertParamReady(new AlertParamReadyHandler() {
				@Override
				public void onAlertParamReady(AlertParamReadyEvent event) {
					initCheck();
				}
			})
		);
		UiCenter.processing();
		DataCenter.wantCapitalList();
		DataCenter.wantForeignList();
		DataCenter.wantRate();
		DataCenter.wantAlertParam();

		//由 client side 發起，定時更新匯率
		Scheduler.get().scheduleFixedDelay(
			new RepeatingCommand() {
				@Override
				public boolean execute() {
					DataCenter.wantRate();
					return true;
				}
			},
			60000	//TODO 改成可自行設定
		);
	}
}
