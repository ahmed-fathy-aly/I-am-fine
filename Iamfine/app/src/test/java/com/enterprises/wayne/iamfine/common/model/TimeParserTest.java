package com.enterprises.wayne.iamfine.common.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeParserTest {

	private TimeParser parser;

	@Before
	public void setup() {
		parser = new TimeParser();
	}

	@Test
	public void testParseFail() {
		assertEquals(-1, parser.parseServerTime(null));
		assertEquals(-1, parser.parseServerTime("wrong format"));
	}

	@Test
	public void testParseSuccess() {
		assertTrue(parser.parseServerTime("2017-06-11T10:51:40.741Z") > 0);
	}
}