package search;

import java.util.ArrayList;
import java.util.List;

import edu.mit.jwi.item.ISynsetID;

public class Relation {

	private List<ISynsetID> synsets;
	private List<String> path;
	
	public Relation(List<ISynsetID> synsets, String direction) {
		this.setSynsets(synsets);
		this.setPath(direction);
	}
	
	public Relation(List<ISynsetID> synsets, List<String> direction) {
		this.setSynsets(synsets);
		this.setPath(direction);
	}

	public List<ISynsetID> getSynsets() {
		return synsets;
	}

	public void setSynsets(List<ISynsetID> synsets) {
		this.synsets = synsets;
	}

	public List<String> getPath() {
		return path;
	}

	public void setPath(List<String> direction) {
		this.path = direction;
	}
	
	public void setPath(String direction) {
		List<String> temp = new ArrayList<String>();
		temp.add(direction);
		path = temp;
	}
}
