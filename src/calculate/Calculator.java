package calculate;

import java.util.Collection;
import java.util.HashMap;

import calculate.constants.Pi;
import calculate.functions.CosineFunction;
import calculate.functions.SineFunction;
import calculate.operators.AddOperator;
import calculate.operators.DivideOperator;
import calculate.operators.ExponentiateOperator;
import calculate.operators.MultiplyOperator;
import calculate.operators.SubtractOperator;
import calculate.structures.Combinable;
import calculate.structures.Expression;
import calculate.structures.Operator;
import calculate.structures.Token;
import calculate.structures.functional.Function;
import calculate.structures.polynomial.Constant;
import calculate.structures.polynomial.Numerical;
import calculate.structures.polynomial.Variable;

/**
 * environment in which calculations may occur
 */
public class Calculator {

	/**
	 * map by name to all defined operators
	 */
	private HashMap< String, Operator > m_operators = new HashMap< String, Operator >();
	
	/**
	 * map by name to all defined variables
	 */
	private HashMap< String, Variable > m_variables = new HashMap< String, Variable >();
	
	/**
	 * map by name to all defined functions
	 */
	private HashMap< String, Function > m_functions = new HashMap< String, Function >();
	
	/**
	 * specifies outputs to be approximate (in decimal form)
	 */
	public static int OUTPUT_APPROXIMATE = 0;
	
	/**
	 * specifies outputs to be exact (in fraction form)
	 */
	public static int OUTPUT_EXACT = 1;
	private int m_outputMode = OUTPUT_APPROXIMATE;
	
	public static int DEFAULT_SCALE = 12;
	private int m_scale = DEFAULT_SCALE;
	
	public static int DEFAULT_DIVISION_ITERATIONS = 25;
	private int m_maxDivisionIterations = DEFAULT_DIVISION_ITERATIONS;
	
	public Calculator() {
		
		//add the operators +, -, *, /, ^
		this.m_operators.put( AddOperator.REPRESENTATION , new AddOperator() );
		this.m_operators.put( SubtractOperator.REPRESENTATION , new SubtractOperator() );
		this.m_operators.put( MultiplyOperator.REPRESENTATION , new MultiplyOperator() );
		this.m_operators.put( DivideOperator.REPRESENTATION , new DivideOperator() );
		this.m_operators.put( ExponentiateOperator.REPRESENTATION , new ExponentiateOperator() );
		
		//add all pre-defined functions
		this.m_functions.put( SineFunction.REPRESENTATION , new SineFunction( this ) );
		this.m_functions.put( CosineFunction.REPRESENTATION , new CosineFunction( this ) );
		
		//add all pre-defined constants
		this.m_variables.put( Pi.REPRESENTATION , new Pi( this ) );
	}
	
	public Expression evaluate( String userInput ) {
		Expression input = new Expression( userInput, this );
		
		//tokenize input
		//System.out.println("tokenize");
		input.tokenize();
		//input.printTokens();
		
		//apply implicit operations to input
		//System.out.println("implicit");
		input.applyImplicitMultiplication();
		
		//convert input to postfix
		//System.out.println("postfix");
		input.convertToPostfix();
		
		//System.out.println("simplifying");
		//simplify the input
		input.simplify();
		
		//get the result
		//Combinable result = input.getResult();
		//System.out.println( result.getRepresentation() );
		
		return input;
	}
	
	/**
	 * 
	 * @param aRepresentation			textual representation of a token
	 * @return							if the specified token is a defined operator
	 */
	public boolean containsOperator( String aRepresentation ) {
		return this.m_operators.containsKey( aRepresentation );
	}
	
	/**
	 * 
	 * @param representation	textual representation of an operator		
	 * @return					the operator with the given name
	 */
	public Operator getOperator( String representation ) {
		return this.m_operators.get( representation );
	}
	
	/**
	 * @return		an array of all defined operators in this environment
	 */
	public Operator[] getAllOperators() {
		Collection<Operator> listOfOperators = this.m_operators.values();
		return listOfOperators.toArray( new Operator[ this.m_operators.size() ] );
	}
	
	/**
	 * 
	 * @param aRepresentation		textual representation of a token
	 * @return						if the specified token is a defined constant
	 */
	public boolean containsConstant( String aRepresentation ) {
		return this.m_variables.containsKey( aRepresentation ) && ( this.m_variables.get( aRepresentation ) instanceof Constant );
	}
	
	/**
	 * 
	 * @param representation		textual representation of a constant
	 * @return						the constant with the given textual representation
	 */
	public Constant getConstant( String representation ) {
		return (Constant) this.m_variables.get( representation );
	}
	
	public void defineConstant( String name, Numerical value ) {
		
		//create the constant
		Constant constantToAdd = new Constant( this, name , value );
		
		//store the constant
		this.m_variables.put( name , constantToAdd );
	}
	
	/**
	 * 
	 * @param aRepresentation			textual representation of a token
	 * @return							if the specified token is a defined variable
	 */
	public boolean containsVariable( String aRepresentation ) {
		return this.m_variables.containsKey( aRepresentation ); 
	}
	
	/**
	 * 
	 * @param name		name of a variable
	 * @return			the variable with the given name
	 */
	public Variable getVariable( String name ) {
		return this.m_variables.get( name );
	}
	
	/**
	 * @return			an array of all defined variables in this environment
	 */
	public Variable[] getAllVariables() {
		Collection<Variable> listOfVariables = this.m_variables.values();
		Variable[] allVariables = listOfVariables.toArray( new Variable[ this.m_variables.size() ] );
		return allVariables;
	}
	
	
	/**
	 * defines a variable with a given name and unknown value
	 * 
	 * @param name			name used to refer to the variable
	 */
	public void defineVariable( String name ) {
		
		//create the variable
		Variable variableToCreate = new Variable( this , name );
		
		//store the variable
		this.m_variables.put( name , variableToCreate );
	}
	
	/**
	 * defines a variable with a given name and value
	 * 
	 * @param name					name used to refer to the variable
	 * @param value					value assigned to the variable
	 * @throws TypeException		if the value cannot be assigned to a variable.
	 * 								for example, you cannot assign "+" to a variable
	 */
	public void defineVariable( String name , String value ) throws TypeException {
		
		//determine what kind of value this variable has
		//check if it is a token first
		Token variableValue = null;
		try {
			variableValue = Token.createToken( value, this );
		} catch ( ParseException notAToken ) {
			//TODO implement Expression class and then use evaluate on "value" parameter
			variableValue = evaluate( value ).getResult();
		}
		
		//create the variable
		if ( variableValue instanceof Combinable ) {
			
			//if a value was calculated for the variable,
			//assign that value to the variable
			Variable variableToCreate = new Variable( this , name , (Combinable) variableValue );
			this.m_variables.put( name , variableToCreate );
		} else if (variableValue == null ) {
			
			//if no value was calculated for the variable,
			//treat the variable as if it has an unknown value
			Variable variableToCreate = new Variable( this , name );
			this.m_variables.put( name, variableToCreate );
		} else {
			
			//if an invalid value, such as an operator,
			//was assigned to the variable, notify the user
			throw new TypeException( name, Combinable.class.getSimpleName() , variableValue.getClass().getSimpleName() );
		}
	}
	
	/**
	 * 
	 * @param aRepresentation			textual representation of a token
	 * @return							if the specified token is a defined function
	 */
	public boolean containsFunction( String aRepresentation ) {
		return this.m_functions.containsKey( aRepresentation );
	}
	
	/**
	 * 
	 * @param representation	textual representation of a function
	 * @return					function with the given representation
	 */
	public Function getFunction( String representation ) {
		return this.m_functions.get( representation );
	}
	
	/**
	 * @return				an array of all defined functions in this environment
	 */
	public Function[] getAllFunctions() {
		Collection<Function> listOfFunctions = this.m_functions.values();
		return listOfFunctions.toArray( new Function[ this.m_functions.size() ] );
	}
	
	/**
	 * @return			the type of output to be produced in this calculations environment
	 */
	public int getOutputMode() {
		return this.m_outputMode;
	}
	
	/**
	 * @return			the number of decimal places to which decimal answers should be displayed
	 */
	public int getScale() {
		return this.m_scale;
	}
	
	/**
	 * @return			maximum number of times to attempt multivariate polynomial division
	 */
	public int getMaxDivisionIterations() {
		return this.m_maxDivisionIterations;
	}
}
