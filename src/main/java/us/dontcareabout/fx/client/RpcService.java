package us.dontcareabout.fx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import us.dontcareabout.fx.shared.CapitalTX;
import us.dontcareabout.fx.shared.ForeignTX;

@RemoteServiceRelativePath("RPC")
public interface RpcService extends RemoteService{
	public List<CapitalTX> getCapitalList();

	public List<ForeignTX> getForeignList();

	public void saveTX(ForeignTX foreignTX);
}
