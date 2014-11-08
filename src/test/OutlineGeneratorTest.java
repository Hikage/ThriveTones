package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import thriveTones.OutlineGenerator;

/**
 * "ThriveTones" Song Generator
 * Copyright © 2014 Brianna Shade
 * bshade@pdx.edu
 *
 * OutlineGeneratorTest.java
 * Tests the OutlineGenerator class
 */

public class OutlineGeneratorTest {
	private static OutlineGenerator outline_generator;

	/**
	 * Initializes the generator object
	 */
	@BeforeClass
	public static void init() {
		outline_generator = new OutlineGenerator();
	}

	/**
	 * Tests class initialization
	 */
	@Test
	public void testInitialization(){
		HashMap<Character, String[]> grammar = outline_generator.getGrammar();
		assertEquals(6, grammar.size());
		assertEquals(2, grammar.get('I').length);
		assertEquals(3, grammar.get('B').length);
		assertEquals(2, grammar.get('M').length);
		assertEquals(4, grammar.get('C').length);
		assertEquals(3, grammar.get('S').length);
		assertEquals(2, grammar.get('E').length);
		assertEquals("iB", grammar.get('I')[0]);
		assertEquals("CvM", grammar.get('M')[0]);
		assertEquals("o", grammar.get('E')[1]);
	}

	/**
	 * Tests expandGrammar()
	 */
	@Test
	public void testExpandGrammar(){
		try {
			String outline = outline_generator.expandGrammar("I");
			System.out.println(outline);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
