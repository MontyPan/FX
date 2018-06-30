package us.dontcareabout.fx.test;

import org.junit.Test;

import us.dontcareabout.fx.shared.ForeignTX;

public class ForeignTxTest {
	@Test
	public void sell() {
		ForeignTX tx = new ForeignTX();
		tx.setValue(100);
		tx.sell(1);

		//只要還有餘額都要能順利賣出
		tx.sell(1);
		tx.sell(98);
	}
}
