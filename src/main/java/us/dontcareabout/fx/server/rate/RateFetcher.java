package us.dontcareabout.fx.server.rate;

import java.util.Date;
import java.util.HashMap;

import us.dontcareabout.fx.shared.Currency;

public abstract class RateFetcher {
	HashMap<Currency, Double> map = new HashMap<>();

	private Date lastUpdate = new Date(0);

	static {
		//HTTPS 連線需要，統一放在這裡
		System.setProperty("https.protocols", "TLSv1.2");
	}

	public final HashMap<Currency, Double> get(){
		Date now = new Date();

		//TODO 時間間隔抽成設定檔
		if (now.getTime() - lastUpdate.getTime() > 60 * 1000) {
			fetchAndParse();
			lastUpdate = now;
		}

		return map;
	}

	/**
	 * 抓網頁、取值並塞進 {@link #map} 的各銀行實作
	 */
	abstract void fetchAndParse();
}
