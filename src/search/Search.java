package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.mit.jwi.item.ISynsetID;

public class Search {

	public static void main(String[] args) throws IOException {
				
		String[] words = getWords();
		Comparer c = new Comparer();

		// get all senses of each word
		List<ISynsetID> word1Synsets = c.getSynsetIDs(words[0]);
		List<ISynsetID> word2Synsets = c.getSynsetIDs(words[1]);
		
		// If overlap between synsets then there's a match
		if (c.synsetMatch(word1Synsets, word2Synsets)) {
			System.out.println ("Synset match");
			return;
		}
		
		// Create a list of synsets visited
		List<ISynsetID> visited = new ArrayList<ISynsetID>(word1Synsets);
		
		// Get hypernyms and hyponyms of word 1.
		List<ISynsetID> hypernyms = c.getHypernyms(word1Synsets);
		
		// If overlap between synsets then there's a match
		if (c.synsetMatch(hypernyms, word2Synsets)) {
			System.out.println ("hypernym synset match");
			return;
		}
		
		List<ISynsetID> hyponyms = c.getHyponyms(word1Synsets);
	
		// If overlap between synsets then there's a match
		if (c.synsetMatch(hyponyms, word2Synsets)) {
			System.out.println ("hyponym synset match");
			return;
		}
		
		// Create a list of all relations to this word
		// And direction (up and down) from starting point
		List<Relation> relations = new ArrayList<Relation>();

		
		if (hypernyms != null && hypernyms.size() != 0) {
			relations.add (new Relation(hypernyms, "hyper"));
		}
		
		if (hyponyms != null && hyponyms.size() != 0) {
			relations.add (new Relation(hyponyms, "hypo"));
		}
		
		int counter = 0;
		if (checkRelations(relations, visited, c, word2Synsets, counter)) {
			System.out.println("all Done");
		}
		else {
			System.out.println("no match");
		}
		

	}
	
	private static boolean checkRelations(List<Relation> relations, List<ISynsetID> visited, Comparer c, List<ISynsetID> word2Synsets, int counter) {
		
		if (counter > 5)
			return false;
		
		List<Relation> newRelations = new ArrayList<Relation>();
		
		for (Relation r : relations) {

			List<ISynsetID> hypernyms = c.getHypernyms(r.getSynsets());
			hypernyms.removeAll(visited);
			// If overlap between synsets then there's a match
			if (c.synsetMatch(hypernyms, word2Synsets)) {
				System.out.println ("hypernym synset match");
				printDirection(r.getPath(), "hyper");
				return true;
			}
			
			List<ISynsetID> hyponyms = c.getHyponyms(r.getSynsets());
			hyponyms.removeAll(visited);
			// If overlap between synsets then there's a match
			if (c.synsetMatch(hyponyms, word2Synsets)) {
				System.out.println ("hyponym synset match");
				printDirection(r.getPath(), "hypo");
				return true;
			}
			
			// Add all used synsets to the visited list
			visited.addAll(r.getSynsets());
			
			// Create two new relations
			List<String> direction = new ArrayList<String>();
			List<String> newDirection = new ArrayList<String>();
			
			direction.clear();
			direction.addAll(r.getPath());
			direction.add("hyper");
			
			newDirection.clear();
			newDirection.addAll(r.getPath());
			newDirection.add("hypo");

			
			if (hypernyms != null && hypernyms.size() != 0) {
				newRelations.add (new Relation(hypernyms, direction));
			}
			
			if (hyponyms != null && hyponyms.size() != 0) {
				newRelations.add (new Relation(hyponyms, newDirection));
			}
			
		}
			counter++;
			
			if (checkRelations(newRelations, visited, c, word2Synsets, counter)) 
				return true;
			return false;
	}

	private static void printDirection(List<String> direction, String move) {
		System.out.print("Direction is ");
		for (int i = 0; i < direction.size(); i++) {
			System.out.print(direction.get(i));
			System.out.print(", ");
		}
		System.out.print(move + "\n");
	}



	private static String[] getWords() {
		String[] words = {"figurehead", "front_man"};
		return words;
	}

}
