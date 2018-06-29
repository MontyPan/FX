package us.dontcareabout.fx.server.rate;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import us.dontcareabout.fx.shared.Currency;

/**
 * 中國信託
 */
public class Ctbc extends RateFetcher {
	@Override
	void fetchAndParse() {
		try {
			Document doc = Jsoup.connect("https://www.ctbcbank.com/CTCBPortalWeb/toPage?id=TW_RB_CM_ebank_018001").get();

			for (Currency c : Currency.values()) {
				if (c == Currency.NTD) { continue; }

				map.put(c, Double.valueOf(doc.select("#" + c + "_buyRate2").text()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
