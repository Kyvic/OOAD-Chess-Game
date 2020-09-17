public class Controller{
	private View theView;
    private Model theModel; 
	
	public Controller(View theView, Model theModel){
		this.theView = theView;
		this.theModel = theModel;
	}
}