package us.dontcareabout.fx.server.rate;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import us.dontcareabout.fx.shared.Currency;

/**
 * 台灣銀行
 */
public class Bot extends RateFetcher {
	@Override
	void fetchAndParse() {
		try {
			//因為是純文字檔，所以無法直接用 Connection.get()
			//get() 裡頭會做 parse() 讓換行符號全部消失
			Connection conn = Jsoup.connect("https://rate.bot.com.tw/xrt/flcsv/0/day")
				.validateTLSCertificates(false);
			conn.request().method(Method.GET);
			conn.execute();
			String raw = conn.response().body();

			String[] lines = raw.split("\n");

			//第一行是 header，跳過不處理
			for (int i = 1; i < lines.length; i++) {
				String[] token = lines[i].trim().split(",");
				map.put(Currency.valueOf(token[0]), Double.parseDouble(token[3]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
