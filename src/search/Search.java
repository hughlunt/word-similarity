package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.mit.jwi.item.ISynsetID;

public class Search {

	public static void main(String[] args) throws IOException {
				
		String[] words = getWords();
		Comparer c = new Comparer();

		// get all synsets of each word
		List<ISynsetID> word1Synsets = c.getSynsetIDs(words[0]);
		List<ISynsetID> word2Synsets = c.getSynsetIDs(words[1]);
		
		if (match(c, word1Synsets, word2Synsets))
			return;
		
		// Get hypernyms and hyponyms of word 1.
		List<ISynsetID> hypernyms = c.getHypernyms(word1Synsets);
		if (match(c, hypernyms, word2Synsets))
			return;
		
		List<ISynsetID> hyponyms = c.getHyponyms(word1Synsets);
		if (match(c, hyponyms, word2Synsets))
			return;
		
		// Create a list of all relations to this word
		List<Relation> relations = getRelations(hypernyms, hyponyms);
		
		// Create a list of synsets visited to this point
		List<ISynsetID> visited = new ArrayList<ISynsetID>(word1Synsets);
		
		List<String> path = checkRelations(relations, visited, c, word2Synsets, 0);
		if (path != null) {
//			int word1Depth = calculateDepth(path, matchingDepth);
//			int commonNodeDepth = getLCNDepth(path, matchingDepth);
//			int upwardSteps = getUpwardSteps(path);
//			int downwardSteps = getDownwardSteps(path)
			System.out.println(path);
			System.out.println("all Done");
		}
		else 
			System.out.println("no match");
	}

	
	private static boolean match(Comparer c, List<ISynsetID> synset1,
			List<ISynsetID> synset2) {
		return c.synsetMatch(synset1, synset2);
	}


	private static List<Relation> getRelations(List<ISynsetID> hypernyms,
			List<ISynsetID> hyponyms) {
		
		List<Relation> relations = new ArrayList<Relation>();
		
		if (hypernyms != null && hypernyms.size() != 0) 
			relations.add (new Relation(hypernyms, "hyper"));
		
		if (hyponyms != null && hyponyms.size() != 0) 
			relations.add (new Relation(hyponyms, "hypo"));
		
		return relations;
	}


	private static List<String> checkRelations(List<Relation> relations, List<ISynsetID> visited, Comparer c, 
			List<ISynsetID> word2Synsets, int counter) {
		
		// Don't proceed if more than 5 steps away
		/* TODO modify this to be a function of depth */
		if (counter > 5)
			return null;
		
		List<Relation> newRelations = new ArrayList<Relation>();
		
		for (Relation r : relations) {

			List<String> upwardPath = getPath(r.getPath(), "hyper");
			List<String> downwardPath = getPath(r.getPath(), "hypo");
				
			List<ISynsetID> hypernyms = c.getHypernyms(r.getSynsets());
			hypernyms.removeAll(visited);
			if (match(c, hypernyms, word2Synsets))
				return upwardPath;
			
			
			List<ISynsetID> hyponyms = c.getHyponyms(r.getSynsets());
			hyponyms.removeAll(visited);
			if (match(c, hyponyms, word2Synsets))
				return downwardPath;
			
			// Add all used synsets to the visited list
			visited.addAll(r.getSynsets());
			
			// Create two new relations
			if (hypernyms != null && hypernyms.size() != 0) {
				newRelations.add (new Relation(hypernyms, upwardPath));
			}
			
			if (hyponyms != null && hyponyms.size() != 0) {
				newRelations.add (new Relation(hyponyms, downwardPath));
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
