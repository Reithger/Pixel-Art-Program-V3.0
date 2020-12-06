package control;

public interface InputHandler {

	public abstract void handleCodeInput(int code, String ref);
	
	public abstract void handleDrawInput(int x, int y, String ref);
	
}
