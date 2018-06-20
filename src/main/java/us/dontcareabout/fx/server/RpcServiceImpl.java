package us.dontcareabout.fx.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import us.dontcareabout.fx.client.RpcService;
import us.dontcareabout.fx.shared.CapitalTX;
import us.dontcareabout.fx.shared.ForeignTX;

public class RpcServiceImpl extends RemoteServiceServlet implements RpcService {
	private static final long serialVersionUID = 1L;

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
}
