package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ChordTest.class, ChordDictionaryTest.class, ProgressionGeneratorTest.class, SongSegmentTest.class, XMLReaderTest.class })
public class TestSuite {

}
