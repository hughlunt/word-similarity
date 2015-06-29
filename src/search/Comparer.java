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

	public ISynsetID synsetMatch(List<ISynsetID> word1Synsets,
			List<ISynsetID> word2Synsets) {
		List<ISynsetID> common = new ArrayList<ISynsetID>(word1Synsets);
		common.retainAll(word2Synsets);
		if (common.size() != 0) 
			return common.get(0); 
		return null; 
	}

	public List<ISynsetID> getHypernyms(ISynsetID synset) {
		List<ISynsetID> hypernyms = new ArrayList<ISynsetID>();
		hypernyms.addAll(dict.getSynset(synset).getRelatedSynsets(Pointer.HYPERNYM));
		hypernyms.addAll(dict.getSynset(synset).getRelatedSynsets(Pointer.HYPERNYM_INSTANCE));
		return hypernyms;
	}

	public List<ISynsetID> getHyponyms(ISynsetID synset) {
		List<ISynsetID> hyponyms = new ArrayList<ISynsetID>();
		hyponyms.addAll(dict.getSynset(synset).getRelatedSynsets(Pointer.HYPONYM));
		hyponyms.addAll(dict.getSynset(synset).getRelatedSynsets(Pointer.HYPONYM_INSTANCE));
		return hyponyms;
	}

}
