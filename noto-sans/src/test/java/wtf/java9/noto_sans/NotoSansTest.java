package wtf.java9.noto_sans;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

class NotoSansTest {

	@Test
	void createNotoSansScaler() throws Exception {
		Assertions.assertTimeout(
				Duration.of(250, ChronoUnit.MILLIS),
				NotoSans::createScaler);
	}

}
