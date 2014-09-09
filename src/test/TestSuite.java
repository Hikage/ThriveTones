package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ChordPairingTest.class, ChordTest.class,
		ProgressionGeneratorTest.class, SongTest.class, XMLReaderTest.class })
public class TestSuite {

}
