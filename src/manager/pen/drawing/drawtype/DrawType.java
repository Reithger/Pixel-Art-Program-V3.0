package manager.pen.drawing.drawtype;

public interface DrawType {

//---  Operations   ---------------------------------------------------------------------------

	public abstract Integer[][] draw(Integer col, int penSize);
	
	public abstract void tellAge(int in);
}
