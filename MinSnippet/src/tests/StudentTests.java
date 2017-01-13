package tests;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.Arrays;

import student_classes.MinimumSnippet;

public class StudentTests {
	static MinimumSnippet get(String [] document , String[] terms){
		return new MinimumSnippet(Arrays.asList(document),Arrays.asList(terms));
	}
	@Test
	public void testMinimumSnippet() {
		MinimumSnippet newSnippet = get(new String []{"1","2","3"}, new String[] {"1"});
		assertTrue(newSnippet.foundAllTerms());
		assertEquals(0,newSnippet.getStartingPos());
		assertEquals(0,newSnippet.getStartingPos());
		assertEquals(1,newSnippet.getLength());
		assertEquals(0,newSnippet.getPos(0));
	}

	@Test
	public void testFoundAllTerms() {
		MinimumSnippet otherSnippet = get(new String []{"1","2","3"}, new String[] {"1","2"});
		assertFalse(otherSnippet.foundAllTerms()== false);
		MinimumSnippet newSnippet = get(new String []{"1","2","3"}, new String[] {"1"});
		assertTrue(newSnippet.foundAllTerms() == true);
		

	}

	@Test
	public void testGetStartingPos() {
		MinimumSnippet newSnippet = get(new String []{"1","2","3"}, new String[] {"1"});
		assertEquals(0,newSnippet.getStartingPos());
		assertTrue(newSnippet.foundAllTerms());
	}

	@Test
	public void testGetEndingPos() {
		MinimumSnippet newSnippet = get(new String []{"1","2","3"}, new String[] {"2"});
		assertEquals(1,newSnippet.getStartingPos());

	}

	@Test
	public void testGetLength() {
		MinimumSnippet newSnippet = get(new String []{"1","2","3"}, new String[] {"1","2"});
		assertEquals(2,newSnippet.getLength());

	}

	@Test
	public void testGetPos() {
		MinimumSnippet newSnippet = get(new String []{"1","2","3"}, new String[] {"1"});
		assertEquals(0,newSnippet.getPos(0));
	}

}
