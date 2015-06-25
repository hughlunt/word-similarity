package search;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

public class Comparer {

	private URL url;
	private IDictionary dict;
	private final String PATH = "/usr/local/WordNet-3.0" + File.separator + "dict";
	private final String HYPERNYM = "HYPER";
	private final String HYPONYM = "HYPO";
	
	public Comparer() throws IOException {
		
		// construct the URL to the Wordnet dictionary directory
		this.url = new URL ("file", null, PATH);
		
		// construct the dictionary object and open it
		this.dict = new Dictionary (url);
		dict.open();
	}

	public List<ISynsetID> getSynsetIDs(String word) {
		List<ISynsetID> synsets = new ArrayList<ISynsetID>();
		IIndexWord idxWord = dict.getIndexWord(word, POS.NOUN);
		
		if (idxWord == null) {
			return null;
		}

		for(IWordID wordID : idxWord.getWordIDs()) {
			IWord iWord = dict.getWord(wordID);
			synsets.add(iWord.getSynset().getID());
		}
		return synsets;
	}

	public boolean synsetMatch(List<ISynsetID> word1Synsets,
			List<ISynsetID> word2Synsets) {
		List<ISynsetID> common = new ArrayList<ISynsetID>(word1Synsets);
		common.retainAll(word2Synsets);
		if (common.size() != 0) 
			return true; 
		return false; 
	}

	public List<ISynsetID> getHypernyms(List<ISynsetID> synsets) {
		return getRelatedSynsets(synsets, HYPERNYM);
	}


	public List<ISynsetID> getHyponyms(List<ISynsetID> synsets) {
		return getRelatedSynsets(synsets, HYPONYM);
	}

	private List<ISynsetID> getRelatedSynsets(List<ISynsetID> synsets,
			String type) {
		List<ISynsetID> relations = new ArrayList<ISynsetID>();
		if (type == HYPERNYM) {
			for (ISynsetID sid : synsets) {
				relations.addAll(dict.getSynset(sid).getRelatedSynsets(Pointer.HYPERNYM));
				relations.addAll(dict.getSynset(sid).getRelatedSynsets(Pointer.HYPERNYM_INSTANCE));
			}
		}
		else if (type == HYPONYM) {
			for (ISynsetID sid : synsets) {
				relations.addAll(dict.getSynset(sid).getRelatedSynsets(Pointer.HYPONYM));
				relations.addAll(dict.getSynset(sid).getRelatedSynsets(Pointer.HYPONYM_INSTANCE));
			}
		}

		return relations;
	}

}
