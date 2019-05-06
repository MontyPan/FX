package us.dontcareabout.fx.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import us.dontcareabout.fx.client.RpcService;
import us.dontcareabout.fx.server.rate.Bot;
import us.dontcareabout.fx.server.rate.RateFetcher;
import us.dontcareabout.fx.shared.CapitalTX;
import us.dontcareabout.fx.shared.Currency;
import us.dontcareabout.fx.shared.ForeignTX;

public class RpcServiceImpl extends RemoteServiceServlet implements RpcService {
	private static final long serialVersionUID = 1L;

	private RateFetcher rateFetcher = new Bot();

	@Override
	public ArrayList<CapitalTX> getCapitalList() {
		return Bank.getCapitalList();
	}

	@Override
	public ArrayList<ForeignTX> getForeignList() {
		return Bank.getForeignList();
	}

	@Override
	public void saveTX(ForeignTX foreignTX) {
		Bank.tx(foreignTX);
	}

	@Override
	public HashMap<Currency, Double> getRateMap() {
		return rateFetcher.get();
	}
}
