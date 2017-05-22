package com.enterprises.wayne.iamfine.sign_up.model;

import com.enterprises.wayne.iamfine.sign_in.model.SignInValidator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpValidatorTest {

	private SignUpValidator validator;

	@Before
	public void setup() {
		validator = new SignUpValidator();
	}

	@Test
	public void testIsValidEmail() {
		assertEquals(false, validator.isValidEmail(null));
		assertEquals(false, validator.isValidEmail(" "));
		assertEquals(false, validator.isValidEmail(""));
		assertEquals(true, validator.isValidEmail("any thing"));
		assertEquals(true, validator.isValidEmail("test@mail.com"));
	}

	@Test
	public void testIsValidName() {
		assertEquals(false, validator.isValidName(null));
		assertEquals(false, validator.isValidName(" "));
		assertEquals(false, validator.isValidName(""));
		assertEquals(true, validator.isValidName("any thing"));
		assertEquals(true, validator.isValidName("abc123"));
	}

	@Test
	public void testIsValidPassword() {
		assertEquals(false, validator.isValidPassword(null));
		assertEquals(false, validator.isValidPassword(" "));
		assertEquals(false, validator.isValidPassword(""));
		assertEquals(true, validator.isValidPassword("any thing"));
		assertEquals(true, validator.isValidPassword("abc123"));
	}


}