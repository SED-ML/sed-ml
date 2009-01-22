package org.miase.jlibsedml.api;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.miase.jlibsedml.api.SedMLError;
import org.miase.jlibsedml.api.UniformTimeCourseValidator;
import org.miase.jlibsedml.generated.UniformTimeCourse;

public class UniformTimeCourseValidatorTest {
     UniformTimeCourseValidator validator = new UniformTimeCourseValidator();
	@Before
	public void setUp() throws Exception {
	}

	private static final double VALID_INIT =0;
	private static final double VALID_START =4;
	private static final double VALID_END =10;
	private UniformTimeCourse createValidUniformTimeCourse() {
		UniformTimeCourse utc= new UniformTimeCourse();
		utc.setId("ax");
		utc.setInitialTime(VALID_INIT);
		utc.setOutputStartTime(VALID_START);
		utc.setOutputEndTime(VALID_END);
		return utc;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidateForCorrectSetup() {
		UniformTimeCourse utc= createValidUniformTimeCourse();
		assertNoErrors(validator.validate(utc));
	}

	private void assertNoErrors(List<SedMLError> validate) {
		assertTrue(validate.size()==0);	
	}
	@Test
	public void assertStartTimeAfterEndTimeError() {
		UniformTimeCourse utc= createValidUniformTimeCourse();
		utc.setOutputStartTime(VALID_END +1);
		assertEquals(UniformTimeCourseValidator.START_AFTER_END, validator.validate(utc).get(0).getMessage());
	}
	
	@Test
	public void assertStartTimeEqualsEndTimeError() {
		UniformTimeCourse utc= createValidUniformTimeCourse();
		utc.setOutputStartTime(VALID_END);
		assertEquals(UniformTimeCourseValidator.START_AFTER_END, validator.validate(utc).get(0).getMessage());
	}
	
	@Test
	public void assertInitiTimeAfterStartTimeError() {
		UniformTimeCourse utc= createValidUniformTimeCourse();
		utc.setInitialTime(VALID_START + 1);
		assertEquals(UniformTimeCourseValidator.INITIAL_AFTER_START, validator.validate(utc).get(0).getMessage());
	}
	
	@Test
	public void assertInitialTimeCanEqualStartTime() {
		UniformTimeCourse utc= createValidUniformTimeCourse();
		utc.setInitialTime(VALID_START );
		assertNoErrors(validator.validate(utc));
	}

}
