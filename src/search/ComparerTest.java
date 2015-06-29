package search;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.mit.jwi.item.ISynsetID;

public class ComparerTest {
	
	Comparer c;

	@Before
	public void setUp() throws Exception {
		this.c = new Comparer();
	}

	
	@Test
	public void testGetSynsetIDs() {
		
		// "dogs" is not in WordNet so should be null 
		List <ISynsetID> synsets = c.getSynsetIDs("dogs");
		assertEquals(synsets, null);
		
		// "dog" has 7 senses in WordNet => 7 synonym sets
		synsets = c.getSynsetIDs("dog");
		assertEquals(synsets.size(),7);
	}

	@Test
	public void testSynsetMatch() {
		//"Dog" and "hotdog" are in a matching synonym set
		List<ISynsetID> word1Synsets = c.getSynsetIDs("dog");
		List<ISynsetID> word2Synsets = c.getSynsetIDs("hotdog");
		assertTrue(c.synsetMatch(word1Synsets, word2Synsets)!= null);
		
		//"Cat" is a hypernym of "feline", so not in same synset
		List<ISynsetID> word3Synsets = c.getSynsetIDs("cat");
		List<ISynsetID> word4Synsets = c.getSynsetIDs("feline");
		assertTrue(c.synsetMatch(word3Synsets, word4Synsets) == null);
	}

	@Test
	public void testgetHypernyms() {
		// Noun dog has 8 hypernyms
		List<ISynsetID> synsets = c.getSynsetIDs("dog");
		List<ISynsetID> hypernyms = new ArrayList<ISynsetID>();
		for (ISynsetID sid : synsets) {
			hypernyms.addAll(c.getHypernyms(sid));
		}
		assertEquals (hypernyms.size(), 8);
	}
	
	@Test
	public void testgetHyponyms() {
		// Noun dog has 20 hyponyms
		List<ISynsetID> synsets = c.getSynsetIDs("dog");
		List<ISynsetID> hyponyms = new ArrayList<ISynsetID>();
		for (ISynsetID sid : synsets) {
			hyponyms.addAll(c.getHyponyms(sid));
		}
		assertEquals (hyponyms.size(), 20);
	}
	
}
