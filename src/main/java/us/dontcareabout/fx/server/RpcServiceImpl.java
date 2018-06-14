package us.dontcareabout.fx.server;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import us.dontcareabout.fx.client.RpcService;
import us.dontcareabout.fx.shared.CapitalTX;
import us.dontcareabout.fx.shared.ForeignTX;
import us.dontcareabout.java.common.Paths;

public class RpcServiceImpl extends RemoteServiceServlet implements RpcService {
	private static final long serialVersionUID = 1L;
	private static final Setting SETTING = new Setting();
	private static final String CAPITAL = "capital.json";
	private static final String FOREIGN = "foreign.json";

	private static final File capitalFile = new Paths(SETTING.workspace()).append(CAPITAL).toFile();
	private static final File foreignFile = new Paths(SETTING.workspace()).append(FOREIGN).toFile();
	private static final Gson gson = new Gson();

	@SuppressWarnings("serial")
	private static final Type capitalType = new TypeToken<ArrayList<CapitalTX>>(){}.getType();
	@SuppressWarnings("serial")
	private static final Type foreignType = new TypeToken<ArrayList<ForeignTX>>(){}.getType();

	public RpcServiceImpl() {}

	@Override
	public ArrayList<CapitalTX> getCapitalList() {
		try {
			return gson.fromJson(
				Files.newReader(capitalFile, Charsets.UTF_8), capitalType
			);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@Override
	public ArrayList<ForeignTX> getForeignList() {
		try {
			return gson.fromJson(
				Files.newReader(foreignFile, Charsets.UTF_8), foreignType
			);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}
}
