package search;

import java.util.ArrayList;
import java.util.List;

import edu.mit.jwi.item.ISynsetID;

public class DerivedSynset {

	private ISynsetID synsetID;
	private List<ISynsetID> synsetPath;
	private String type;
	private List<String> path;
	
	public DerivedSynset(List<ISynsetID> synsetPath, ISynsetID id, String type) {
		setSynsetPath(synsetPath);
		setSynsetID(id);
		setType(type);
		setPath(type);
	}

	public DerivedSynset(List<ISynsetID> synsetPath, ISynsetID id,
			String type, List<String> path) {
		setSynsetPath(synsetPath);
		setSynsetID(id);
		setType(type);
		setPath(path);
	}

	private void setPath(String type2) {
		List<String> temp = new ArrayList<String>();
		temp.add(type2);
		this.path = temp;
	}

	public List<ISynsetID> getSynsetPath() {
		return synsetPath;
	}

	public void setSynsetPath(List<ISynsetID> synsetPath) {
		this.synsetPath = synsetPath;
	}

	public ISynsetID getSynsetID() {
		return synsetID;
	}

	public void setSynsetID(ISynsetID synsetID) {
		this.synsetID = synsetID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getPath() {
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}
}
