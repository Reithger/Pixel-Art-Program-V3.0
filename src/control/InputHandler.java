package control;

public interface InputHandler {

//---  Operations   ---------------------------------------------------------------------------
	
	public abstract void handleCodeInput(int code, String ref);
	
	public abstract void handleDrawInput(int x, int y, int duration, String ref);
	
	public abstract void handleKeyInput(char code);
	
}
