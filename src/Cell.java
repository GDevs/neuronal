
public class Cell extends Entity implements
java.io.Serializable{
	public static final byte NUMBER_OF_FUEHLER = 16;
	public static final byte NUMBER_OF_FUEHLER_INPUT = 3;
	public static final byte NUMBER_OF_FEEDBACK_NEURONS = 5;
	public static final byte NUMBER_OF_INPUT = 1+ NUMBER_OF_FUEHLER + NUMBER_OF_FEEDBACK_NEURONS;
	public static final byte NUMBER_OF_HIDDEN_1 = 16;
	public static final byte NUMBER_OF_HIDDEN_2 = 16;
	public static final byte NUMBER_OF_OUTPUT = 2+NUMBER_OF_FEEDBACK_NEURONS;
	public static final double RADIUS = 2;
	public static final int MAX_ENERGY = 10000;
	private boolean alive = true;
	private int energy = MAX_ENERGY/2; 
	private int angle = 0;
	private int generation= 0;
	
	private double[][] fuehlerInput;
	private double[] inputNeurons;
	private double[] hiddenNeurons1;
	private double[] hiddenNeurons2;
	private double[] outputNeurons;
	private double[][] fuehlerToInput;
	private double[][] inputToHidden1;
	private double[][] hidden1ToHidden2;
	private double[][] hidden2ToOutput;
/* Erklaerung fuer die Vektoren (Neuronenschichten):
 * 
 * fuehlerInput: {Zelle, Starke Zelle, Essen}
 * Input: {FuehlerLinks, Fuehler Rechts, 1- Energie/300, Starke}
 * Output: {Drehen, Bewegen, Staerke »freisetzen«}
 */
	
	public Cell(double aX,double aY){
		fuehlerInput = new double [NUMBER_OF_FUEHLER][NUMBER_OF_FUEHLER_INPUT];
		inputNeurons = new double[NUMBER_OF_INPUT];
		hiddenNeurons1 = new double[NUMBER_OF_HIDDEN_1];
		hiddenNeurons2 = new double[NUMBER_OF_HIDDEN_2];
		outputNeurons = new double[NUMBER_OF_OUTPUT];

		fuehlerToInput = new double[1][NUMBER_OF_FUEHLER_INPUT];
		inputToHidden1 = new double[NUMBER_OF_HIDDEN_1][NUMBER_OF_INPUT];
		hidden1ToHidden2 = new double[NUMBER_OF_HIDDEN_2][NUMBER_OF_HIDDEN_1];
		hidden2ToOutput = new double[NUMBER_OF_OUTPUT][NUMBER_OF_HIDDEN_2];
		x = aX;
		y = aY;
	}
	
	public Cell(double aX,double aY,int aGeneration){
		this(aX,aY);
		generation = aGeneration;
	}
	
	public void compute_neurons(){
		for(int i = 0; i< NUMBER_OF_FUEHLER; i++){
			inputNeurons[i] = Matrix.multiply(fuehlerToInput, fuehlerInput[i])[0];
		}
		inputNeurons[NUMBER_OF_FUEHLER+0] = (1-(energy/MAX_ENERGY));
		for(int i = 0; i< NUMBER_OF_FEEDBACK_NEURONS; i++){
			inputNeurons[NUMBER_OF_INPUT - NUMBER_OF_FEEDBACK_NEURONS + i] = outputNeurons[NUMBER_OF_OUTPUT - NUMBER_OF_FEEDBACK_NEURONS + i];
		}
		hiddenNeurons1 = Matrix.multiply(inputToHidden1, inputNeurons);
		hiddenNeurons2 = Matrix.multiply(hidden1ToHidden2, hiddenNeurons1);
		outputNeurons = Matrix.multiply(hidden2ToOutput, hiddenNeurons2);
	}
	
	public void move(){
		//angle++;
		int energyConsumption = 1;
		
		for(int i = 0; i < outputNeurons.length; i++){
			if(Math.abs(outputNeurons[i])>1){
				outputNeurons[i]=1*(outputNeurons[i]/Math.abs(outputNeurons[i]));
			}
		}
		
		//winkelaenderung:
		angle = (int) (angle + Math.round(outputNeurons[0]*25));
		if(angle > 360){
			angle = angle - 360;
		}else if(angle < 0){
			angle = angle + 360;
		}
		energyConsumption = (int) (energyConsumption + Math.abs(outputNeurons[0]*5));
		
		//Bewegen
		setX(getX() + Math.cos(angle*Math.PI/180)*outputNeurons[1]);
		setY(getY() + Math.sin(angle*Math.PI/180)*outputNeurons[1]);
		energyConsumption = (int) (energyConsumption + Math.abs(outputNeurons[1]*2));
		
		if(energy-energyConsumption>0){
			energy = energy - energyConsumption;
		}else{
			energy = 0;
			setAlive(false);
		}
	}
	
	public void eat(int aEnergy){
		if(energy + aEnergy <=MAX_ENERGY){
			energy = energy + aEnergy;
		}else{
			energy = MAX_ENERGY;
		}
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public double[] getFuehlerInput(byte fuehlerNumber) {
		return fuehlerInput[fuehlerNumber];
	}

	public void setFuehlerInput(double[] fuehlerInput, byte fuehlerNumber) {
		this.fuehlerInput[fuehlerNumber] = fuehlerInput;
	}
	
	public double[][] getFuehlerInput() {
		return fuehlerInput;
	}

	public void setFuehlerInput(double[][] fuehlerInput) {
		this.fuehlerInput = fuehlerInput;
	}

	public double[][] getInputToHidden1() {
		return inputToHidden1;
	}

	public void setInputToHidden1(double[][] inputToHidden1) {
		this.inputToHidden1 = inputToHidden1;
	}

	public double[][] getHidden2ToOutput() {
		return hidden2ToOutput;
	}

	public double[][] getHidden1ToHidden2() {
		return hidden1ToHidden2;
	}

	public void setHidden1ToHidden2(double[][] hidden1ToHidden2) {
		this.hidden1ToHidden2 = hidden1ToHidden2;
	}

	public void setHidden2ToOutput(double[][] hidden2ToOutput) {
		this.hidden2ToOutput = hidden2ToOutput;
	}

	public double[][] getFuehlerToInput() {
		return fuehlerToInput;
	}

	public void setFuehlerToInput(double[][] fuehlerToInput) {
		this.fuehlerToInput = fuehlerToInput;
	}

}
