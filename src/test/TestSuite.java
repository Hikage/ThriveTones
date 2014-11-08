package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * TestSuite.java
 * Runs all of the test classes
 */

@RunWith(Suite.class)
@SuiteClasses({ ChordTest.class, ChordDictionaryTest.class, ProgressionGeneratorTest.class,
	SongSegmentTest.class, XMLReaderTest.class })
public class TestSuite {}
