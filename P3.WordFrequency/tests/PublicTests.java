package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import frequency.WordFrequency;

public class PublicTests {

	@Test
	public void testUMD() {
		String answer = WordFrequency.freq("http://www.cs.umd.edu/class/summer2015/cmsc132/projects/P3_WordFrequency/test1.html",20);
		assertTrue(TestsSupport.isCorrect("pubUMD.txt", answer));
	}

	@Test
	public void testEinstein() {
		String answer = WordFrequency.freq("einstein.txt",20);
		assertTrue(TestsSupport.isCorrect("pubEinstein.txt", answer));
	}
	
	@Test
	public void test1() {
		String answer = WordFrequency.freq("test1.txt",10);
		assertTrue(TestsSupport.isCorrect("pubTest1.txt", answer));
	}
	@Test
	public void testSyllabus() {
		String answer = WordFrequency.freq("https://www.cs.umd.edu/class/summer2015/cmsc132/syllabus.shtml",200);
		assertTrue(TestsSupport.isCorrect("pubSyllabus.txt", answer));
	}
	
	@Test
	public void testWar_peace() {
		String answer = WordFrequency.freq("war_peace.txt",50);
		assertTrue(TestsSupport.isCorrect("pubWar_peace.txt", answer));
	}
	
}
