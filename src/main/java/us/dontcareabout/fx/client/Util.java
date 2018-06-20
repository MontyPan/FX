package us.dontcareabout.fx.client;

import com.google.gwt.i18n.client.DateTimeFormat;

import us.dontcareabout.fx.shared.Currency;

public class Util {
	public static final int SCROLL_HEIGHT = 16;
	public static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("yyyy/MM/dd");

	public static String currencyName(Currency c) {
		switch(c) {
		case AUD:
			return "澳幣";
		case CAD:
			return "加拿大幣";
		case CHF:
			return "瑞士法郎";
		case CNY:
			return "人民幣";
		case EUR:
			return "歐元";
		case GBP:
			return "英鎊";
		case HKD:
			return "港幣";
		case IDR:
			return "印尼盾";
		case JPY:
			return "日圓";
		case KRW:
			return "韓圜";
		case MYR:
			return "馬來幣";
		case NTD:
			return "新台幣";
		case NZD:
			return "紐西蘭幣";
		case PHP:
			return "菲披索";
		case SEK:
			return "瑞典幣";
		case SGD:
			return "新加坡幣";
		case THB:
			return "泰銖";
		case USD:
			return "美金";
		case VND:
			return "越南盾";
		case ZAR:
			return "南非幣";
		default:
			return "P 幣";
		}
	}
}
