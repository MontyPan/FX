package us.dontcareabout.fx.server.rate;

import java.util.HashMap;

import us.dontcareabout.fx.shared.Currency;

public abstract class RateFetcher {
	HashMap<Currency, Double> map = new HashMap<>();

	static {
		//HTTPS 連線需要，統一放在這裡
		System.setProperty("https.protocols", "TLSv1.2");
	}

	public final HashMap<Currency, Double> get(){
		fetchAndParse();
		return map;
	}

	/**
	 * 實作抓網頁、取值並塞進 {@link #map} 的各銀行實作
	 */
	abstract void fetchAndParse();
}
