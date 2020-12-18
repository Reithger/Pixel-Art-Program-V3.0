package manager.pen.changes;

public class ChangeStep {

//---  Instance Variables   -------------------------------------------------------------------
	
	private volatile Change redo;
	private volatile Change undo;
	
	private ChangeStep next;
	private ChangeStep previous;
	
	private boolean hasChange;
	
	private String ref;
	private int layer;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public ChangeStep(String inRef, int inLayer) {
		ref = inRef;
		layer = inLayer;
		redo = new Change();
		undo = new Change();
		undo.setOverwrite(false);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void addChanges(Change inRedo, Change inUndo) {
		hasChange = true;
		redo.addChange(inRedo);
		undo.addChange(inUndo);
	}
	
	public boolean hasContent() {
		return hasChange;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setNext(ChangeStep in) {
		next = in;
	}
	
	public void setPrevious(ChangeStep in) {
		previous = in;
	}

	public void export(String path) {
		int size = getSize();
		redo.save(path + "redo_" + size);
		undo.save(path + "undo_" + size);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Change getRedo() {
		return redo;
	}
	
	public Change getUndo() {
		return undo;
	}
	
	public ChangeStep getNext() {
		return next;
	}
	
	public ChangeStep getPrevious() {
		return previous;
	}
	
	public String getReference() {
		return ref;
	}
	
	public int getLayer() {
		return layer;
	}

	public int getSize() {
		return 1 + (previous != null ? previous.getSize() : 0);
	}
	
//---  Mechanics   ----------------------------------------------------------------------------

}
