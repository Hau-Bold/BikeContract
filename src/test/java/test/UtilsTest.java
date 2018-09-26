package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import customer.Customer;
import utils.Utils;

/**
 * Unit test for simple ClientBikeContract.
 */
public class UtilsTest {

	@Test
	public void customerNotValidTest() {

		Customer customer = new Customer("Hans", "Dampf", "", "", "", "", "", "");

		assertEquals(Boolean.FALSE, Utils.isCustomerValid(customer));

	}

	@Test
	public void customerValidTest() {

		Customer customer = new Customer("Hans", "Dampf", "a", "a", "a", "a", "", "");

		assertEquals(Boolean.TRUE, Utils.isCustomerValid(customer));

	}

	// @Test
	// public void formatDoubleCorrectTest() {
	// Double actual = 0.5;
	//
	// Double expected = 0.50;
	//
	// assertEquals(expected, Utils.formatDouble(actual));
	//
	// }

}