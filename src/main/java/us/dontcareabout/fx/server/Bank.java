package us.dontcareabout.fx.server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import us.dontcareabout.fx.shared.CapitalTX;
import us.dontcareabout.fx.shared.ForeignTX;
import us.dontcareabout.fx.shared.HasId;
import us.dontcareabout.fx.shared.tool.CurrencyUtil;
import us.dontcareabout.fx.shared.tool.Matcher;
import us.dontcareabout.java.common.Paths;

/**
 * 負責交易紀錄的存取。
 */
public class Bank {
	private static final Setting SETTING = new Setting();
	private static final String CAPITAL = "capital.json";
	private static final String FOREIGN = "foreign.json";

	private static final File capitalFile = new Paths(SETTING.workspace()).append(CAPITAL).toFile();
	private static final File foreignFile = new Paths(SETTING.workspace()).append(FOREIGN).toFile();

	private static final Gson gson = new Gson();
	private static final Type capitalType = new TypeToken<ArrayList<CapitalTX>>(){}.getType();
	private static final Type foreignType = new TypeToken<ArrayList<ForeignTX>>(){}.getType();

	private static int idCounter;

	static {
		ArrayList<CapitalTX> capital = getCapitalList();
		ArrayList<ForeignTX> foreign = getForeignList();
		idCounter = Math.max(getMaxId(capital), getMaxId(foreign)) + 1;
	}

	public static void tx(ForeignTX foreign) {
		ArrayList<ForeignTX> foreignList = getForeignList();

		//要先判斷如果是賣出的交易，先解決夠不夠賣、對應買入紀錄更新 balance 的邏輯
		if (foreign.getValue() < 0) {
			HashMap<ForeignTX, Double> target = Matcher.match(foreignList, foreign.getCurrency(), foreign.getValue() * -1, foreign.getRate());

			//XXX 目前打算靠前端擋，不過嚴格說起來好像應該炸個 exception 比較好？
			if (target == null) { return; }

			//更新對應的買入資料、以及計算該次賣出交易盈虧
			double profit = 0.0;
			for (ForeignTX tx : target.keySet()) {
				double amount = target.get(tx);

				//profit += (foreign.getRate() - tx.getRate()) * amount;
				profit = CurrencyUtil.add(
					profit,
					CurrencyUtil.multiply(
						CurrencyUtil.subtract(foreign.getRate(), tx.getRate()),
						amount
					)
				);

				tx.sell(amount);
			}

			foreign.setProfit(profit);
		}

		settingId(foreign);

		if (foreign.getRate() != 0) {
			CapitalTX capital = new CapitalTX();
			settingId(capital);
			capital.setDate(foreign.getDate());
			capital.setForeignId(foreign.getId());
			capital.setValue(Math.round(foreign.getRate() * foreign.getValue()) * -1);
			capital.setNote((foreign.getValue() > 0 ? "買入" : "賣出") + CurrencyUtil.name(foreign.getCurrency()));
			addCapital(capital);
			foreign.setCapitalId(capital.getId());
		}

		foreignList.add(foreign);

		try {
			saveForeign(foreignList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<CapitalTX> getCapitalList() {
		try {
			return gson.fromJson(
				Files.newReader(capitalFile, Charsets.UTF_8), capitalType
			);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public static void addCapital(CapitalTX tx) {
		Preconditions.checkArgument(tx.getId() != 0, "沒有 id");

		try {
			ArrayList<CapitalTX> result = getCapitalList();
			result.add(tx);
			Files.write(
				gson.toJson(
					result,
					capitalType
				).getBytes(Charsets.UTF_8),
				capitalFile
			);
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<ForeignTX> getForeignList() {
		try {
			return gson.fromJson(
				Files.newReader(foreignFile, Charsets.UTF_8), foreignType
			);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	private static void addForeign(ForeignTX tx) {
		Preconditions.checkArgument(tx.getId() != 0, "沒有 id");

		try {
			ArrayList<ForeignTX> result = getForeignList();
			result.add(tx);
			saveForeign(result);
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	private static void saveForeign(ArrayList<ForeignTX> foreignList) throws IOException {
		Files.write(
			gson.toJson(
				foreignList,
				foreignType
			).getBytes(Charsets.UTF_8),
			foreignFile
		);
	}

	private static <T extends HasId> int getMaxId(ArrayList<T> list) {
		if (list.size() == 0) { return 0; }

		Collections.sort(list, HasId.comparator);
		return list.get(list.size() - 1).getId();
	}

	private static void settingId(HasId hasId) {
		hasId.setId(idCounter);
		idCounter++;
	}
}
