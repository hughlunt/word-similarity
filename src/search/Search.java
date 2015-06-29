package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.mit.jwi.item.ISynsetID;

public class Search {

	public static void main(String[] args) throws IOException {
				
		String[] words = getWords();
		Comparer c = new Comparer();

		// get all synsets of each word
		List<ISynsetID> word1Synsets = c.getSynsetIDs(words[0]);
		List<ISynsetID> word2Synsets = c.getSynsetIDs(words[1]);
		
		if (match(c, word1Synsets, word2Synsets) != null)
			return;
		
		// Get hypernyms and hyponyms of word 1.
		List<DerivedSynset> derivedSets = new ArrayList<DerivedSynset>();
		for (ISynsetID sid : word1Synsets) {
			derivedSets.addAll(getDerivations(sid, c.getHypernyms(sid), c.getHyponyms(sid))); 
		}
		
		if (match(c, getHypernyms(derivedSets), word2Synsets) != null)
			return;
		
		if (match(c, getHyponyms(derivedSets), word2Synsets) != null)
			return;
		
		// Create a list of synsets visited to this point
		List<ISynsetID> visited = new ArrayList<ISynsetID>(word1Synsets);
		
		System.out.println (checkRelations(derivedSets, visited, c, word2Synsets, 0).getSynsetPath());
//		if (path != null) {
//			int word1Depth = calculateDepth(path, matchingDepth);
//			int commonNodeDepth = getLCNDepth(path, matchingDepth);
//			int upwardSteps = getUpwardSteps(path);
//			int downwardSteps = getDownwardSteps(path)
//			System.out.println(path);
//			System.out.println("all Done");
//		}
//		else 
//			System.out.println("no match");
	}

	
	private static List<ISynsetID> getHypernyms(List<DerivedSynset> derivedSets) {
		List<ISynsetID> hypernyms = new ArrayList<ISynsetID>();
		for (DerivedSynset synset : derivedSets) {
			if (synset.getType() == "hyper") {
				hypernyms.add(synset.getSynsetID());
			}
		}
		return hypernyms;
	}
	
	private static List<ISynsetID> getHyponyms(List<DerivedSynset> derivedSets) {
		List<ISynsetID> hyponyms = new ArrayList<ISynsetID>();
		for (DerivedSynset synset : derivedSets) {
			if (synset.getType() == "hyper") {
				hyponyms.add(synset.getSynsetID());
			}
		}
		return hyponyms;
	}


	private static List<DerivedSynset> getDerivations(
			ISynsetID sid, List<ISynsetID> hypernyms, List<ISynsetID> hyponyms) {
		List<ISynsetID> path = new ArrayList<ISynsetID>();
		path.add(sid);
		List<DerivedSynset> derivedSets = new ArrayList<DerivedSynset>();
		for (ISynsetID hypernym : hypernyms) {
			derivedSets.add(new DerivedSynset(path, hypernym, "hyper"));
		}
		for (ISynsetID hyponym : hyponyms) {
			derivedSets.add(new DerivedSynset(path, hyponym, "hypo"));
		}
		return derivedSets;
	}


	private static ISynsetID match(Comparer c, List<ISynsetID> synset1,
			List<ISynsetID> synset2) {
		return c.synsetMatch(synset1, synset2);
	}


	private static DerivedSynset checkRelations(List<DerivedSynset> relations, List<ISynsetID> visited, Comparer c, 
			List<ISynsetID> word2Synsets, int counter) {
		
		// Don't proceed if more than 5 steps away
		/* TODO modify this to be a function of depth */
		if (counter > 5)
			return null;
		
		List<DerivedSynset> newRelations = new ArrayList<DerivedSynset>();
		
		for (DerivedSynset r : relations) {
			
			List<String> upwardPath = getPath(r.getPath(), "hyper");
			List<String> downwardPath = getPath(r.getPath(), "hypo");
			List<ISynsetID> synsetPath = new ArrayList<ISynsetID>();
			synsetPath.addAll(r.getSynsetPath());
			synsetPath.add(r.getSynsetID());
			
			List<ISynsetID> hypernyms = c.getHypernyms(r.getSynsetID());
			List<ISynsetID> hyponyms = c.getHyponyms(r.getSynsetID());
			hypernyms.removeAll(visited);
			hyponyms.removeAll(visited);
			
			ISynsetID matchingSynset = match(c, hypernyms, word2Synsets);
			if (matchingSynset != null)
				return new DerivedSynset(synsetPath, matchingSynset, "hyper", upwardPath);
			
			matchingSynset = match(c, hyponyms, word2Synsets);
			if (matchingSynset != null)
				return new DerivedSynset(synsetPath, matchingSynset, "hypo", downwardPath);
			
			// Add all used synsets to the visited list
			visited.add(r.getSynsetID());
			
			// Create two new relations
			if (hypernyms != null && hypernyms.size() != 0) {
				for (ISynsetID hypernym : hypernyms) {
					newRelations.add(new DerivedSynset(synsetPath, hypernym, "hyper", upwardPath));
				}
			}
			
			if (hyponyms != null && hyponyms.size() != 0) {
				for (ISynsetID hyponym : hyponyms) {
					newRelations.add(new DerivedSynset(synsetPath, hyponym, "hypo", downwardPath));
				}
			}
		}
			counter++;
			return checkRelations(newRelations, visited, c, word2Synsets, counter); 
	}
	
	private static List<String> getPath(List<String> currentPath, String newDirection) {
		List<String> path = new ArrayList<String>();
		path.addAll(currentPath);
		path.add(newDirection);
		return path;
	}
	
	private static String[] getWords() {
		String[] words = {"organism", "terrier"};
		return words;
	}

}
