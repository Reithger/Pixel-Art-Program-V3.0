package manager.pen.changes;

import java.util.HashMap;

public class VersionHistory {

//---  Constants   ----------------------------------------------------------------------------
	
	private final static int AUTO_SAVE_CUT_OFF = 250;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	private HashMap<String, ChangeStep> changes;
	private String active;
	private long lastEdit;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public VersionHistory() {
		changes = new HashMap<String, ChangeStep>();
		active = "";
		lastEdit = System.currentTimeMillis();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void addChange(String ref, int layer, int duration, Change undo, Change redo) {
		ensureChangeStep(ref, layer);
		if((durationSurpassed() && ref.equals(active)) || changes.get(ref).getLayer() != layer) {
			commitChangeStep(active, changes.get(ref).getLayer());
		}
		active = ref;
		ChangeStep cS = changes.get(ref);
		cS.addChanges(redo, undo);
		lastEdit = System.currentTimeMillis();
	}
	
	private void ensureChangeStep(String ref, int layer) {
		if(changes.get(ref) == null) {
			changes.put(ref, new ChangeStep(ref, layer));
			ChangeStep curr = changes.get(ref);
			ChangeStep actual = new ChangeStep(ref, layer);
			curr.setNext(actual);
			actual.setPrevious(curr);
			changes.put(ref, actual);
			lastEdit = System.currentTimeMillis();
		}
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Change getUndo(String ref) {
		if(changes.get(ref) == null) {
			return null;
		}
		ChangeStep cS = changes.get(ref);
		if(cS.getPrevious() != null) {
			changes.put(ref, cS.getPrevious());
		}
		return cS.getUndo();
	}
	
	public Change getRedo(String ref) {
		if(changes.get(ref) == null) {
			return null;
		}
		ChangeStep cS = changes.get(ref);
		if(cS.getNext() == null) {
			return null;
		}
		changes.put(ref, cS.getNext());
		return cS.getNext().getRedo();
	}

//---  Support Methods   ----------------------------------------------------------------------
	
	private void commitChangeStep(String ref, int layer) {
		ChangeStep curr = changes.get(ref);
		if(curr.hasContent()) {
			ChangeStep next = new ChangeStep(ref, curr.getLayer());
			curr.setNext(next);
			next.setPrevious(curr);
			changes.put(ref, next);
		}
		lastEdit = System.currentTimeMillis();
	}

	private boolean durationSurpassed() {
		return (System.currentTimeMillis() - lastEdit) > AUTO_SAVE_CUT_OFF;
	}
	
}
