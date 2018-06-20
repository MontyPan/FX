package us.dontcareabout.fx.server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import us.dontcareabout.fx.shared.CapitalTX;
import us.dontcareabout.fx.shared.ForeignTX;
import us.dontcareabout.fx.shared.HasId;
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
	@SuppressWarnings("serial")
	private static final Type capitalType = new TypeToken<ArrayList<CapitalTX>>(){}.getType();
	@SuppressWarnings("serial")
	private static final Type foreignType = new TypeToken<ArrayList<ForeignTX>>(){}.getType();

	private static int idCounter;

	static {
		ArrayList<CapitalTX> capital = getCapitalList();
		ArrayList<ForeignTX> foreign = getForeignList();
		idCounter = Math.max(getMaxId(capital), getMaxId(foreign)) + 1;
	}

	public static void tx(ForeignTX foreign) {
		settingId(foreign);

		if (foreign.getRate() != 0) {
			CapitalTX capital = new CapitalTX();
			settingId(capital);
			capital.setDate(foreign.getDate());
			capital.setForeignId(foreign.getId());
			capital.setValue(Math.round(foreign.getRate() * foreign.getValue()) * -1);
			saveCapital(capital);
			foreign.setCapitalId(capital.getId());
		}

		saveForeign(foreign);
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

	public static void saveCapital(CapitalTX tx) {
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

	public static void saveForeign(ForeignTX tx) {
		Preconditions.checkArgument(tx.getId() != 0, "沒有 id");

		try {
			ArrayList<ForeignTX> result = getForeignList();
			result.add(tx);
			Files.write(
				gson.toJson(
					result,
					foreignType
				).getBytes(Charsets.UTF_8),
				foreignFile
			);
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			e.printStackTrace();
		}
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
