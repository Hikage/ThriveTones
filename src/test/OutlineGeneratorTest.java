package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import thriveTones.OutlineGenerator;
import thriveTones.SongSegment.SongPart;

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
		assertEquals(9, grammar.size());
		assertEquals(2, grammar.get('I').length);
		assertEquals(3, grammar.get('B').length);
		assertEquals(2, grammar.get('M').length);
		assertEquals(2, grammar.get('F').length);
		assertEquals(2, grammar.get('P').length);
		assertEquals(2, grammar.get('C').length);
		assertEquals(3, grammar.get('S').length);
		assertEquals(2, grammar.get('E').length);
		assertEquals(3, grammar.get('O').length);
		assertEquals("iB", grammar.get('I')[0]);
		assertEquals("F", grammar.get('M')[0]);
		assertEquals("pcvpcSE", grammar.get('P')[1]);
		assertEquals("O", grammar.get('E')[1]);
	}

	/**
	 * Tests expandGrammar()
	 */
	@Test
	public void testExpandGrammar(){
		try {
			String outline = outline_generator.expandGrammar("I");
			System.out.println(outline);
			assertTrue("co".contains(Character.toString(outline.charAt(outline.length() - 1))));
			assertFalse("sb".contains(Character.toString(outline.charAt(outline.length() - 1))));

			outline = outline_generator.expandGrammar("C");
			assertFalse(outline.contains("p"));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Tests buildOutline()
	 */
	@Test
	public void testBuildOutline(){
		try{
			ArrayList<SongPart> outline = outline_generator.buildOutline();
			System.out.println(outline.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
