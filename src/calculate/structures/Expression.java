package calculate.structures;

import java.util.Arrays;

import util.Text;
import _library.LinkedList;
import _library.Queue;
import _library.Stack;
import calculate.Calculator;
import calculate.ParseException;
import calculate.SyntaxException;
import calculate.misc.CloseParenthesis;
import calculate.misc.Comma;
import calculate.misc.OpenParenthesis;
import calculate.operators.AddOperator;
import calculate.operators.DivideOperator;
import calculate.operators.ExponentiateOperator;
import calculate.operators.MultiplyOperator;
import calculate.operators.NegateOperator;
import calculate.operators.SubtractOperator;
import calculate.structures.functional.Function;
import calculate.structures.polynomial.Fraction;
import calculate.structures.polynomial.Numerical;
import calculate.structures.polynomial.Variable;

public class Expression {

	/**
	 * the expression the user wishes to evaluate
	 */
	private String m_inputExpression;
	
	/**
	 * the LaTeX representation of the expression the user wishes to evaluate
	 */
	private String m_inputLatex = "";
	
	/**
	 * calculations environment in which this expression evaluates.
	 * the environment determines the variables, functions, etc.
	 * that this expression contains
	 */
	private Calculator m_environment;
	
	/**
	 * simplified portion of the inputed expression 
	 */
	private Combinable m_simplified = null;
	
	/**
	 * tokens that have yet to be combined into the final simplified result
	 */
	private LinkedList<Token> m_unsimplified = new LinkedList< Token >();
	
	/**
	 * used in shunting yard algorithm for converting to postfix
	 */
	private Stack< Token > m_operatorStack = new Stack< Token >();
	
	/**
	 * stores the unsimplified expression in postfix form
	 */
	private Queue< Token > m_unsimplifiedPostfix = new Queue< Token >();
	
	private Stack< Combinable > m_evaluationStack = new Stack< Combinable >();
	
	public Expression( String inputToEvaluate, Calculator environment ) {
		this.m_inputExpression = inputToEvaluate;
		this.m_environment = environment;
	}
	
	
	final public static String SPACE = " ";
	
	/**
	 * converts input into separate tokens so the computer algebra system
	 * can deal with it more easily
	 * 
	 * @throws ParseException			if some part of the input could not be recognized as
	 * 									a variable, function, etc.
	 */
	@SuppressWarnings("null")
	public void tokenize() throws ParseException {
		
		//remove all spaces in the input
		this.m_inputExpression = this.m_inputExpression.replace( SPACE , "" );
		
		//get all keywords we should look at in the input
		Operator[] operators = this.m_environment.getAllOperators();
		Function[] functions = this.m_environment.getAllFunctions();
		Variable[] variables = this.m_environment.getAllVariables();
		
		//sort the variables so that if we have a variable "s", and a variable
		//"sa" they do not get mixed up in tokenization.
		//when sorted, "sa" will be checked after "s", so there will be no mixup
		Arrays.sort( variables );
		Comma comma = new Comma();
		OpenParenthesis openParenthesis = new OpenParenthesis();
		CloseParenthesis closeParenthesis = new CloseParenthesis();
		
		int startIndexOfNumerical = 0;
		//go through the whole input
		for ( int idx = 0; idx < this.m_inputExpression.length(); /*do not increment*/ ) {
			
			//we need to remember if we have already created a token
			//starting at this index. that way, we do not create
			//two tokens starting at this index. for example
			//you may have a variable x and a variable xy. if the input
			//is xy, we want to avoid creating both an "x" token and an "xy" token
			Token tokenToAdd = null;
			
			if ( tokenToAdd == null ) {
				
				//check if we are at the start of an operator
				for ( int operatorID = 0 ; operatorID < operators.length ; operatorID++ ) {
					
					//compare the operator's representation with
					//the piece of user input starting at the current index
					//and ending at the current index + operator's representation's length
					String anOperator = operators[ operatorID ].getRepresentation();
					
					//make sure we do not go out of bounds
					if ( idx + anOperator.length() <= this.m_inputExpression.length() ) {
						String pieceOfInputToCheck = this.m_inputExpression.substring( idx , idx + anOperator.length() );
						if ( pieceOfInputToCheck.equals( anOperator ) ) {
							
							//if they are the same, then we've read an operator,
							//so add that to the unsimplified expression
							tokenToAdd = operators[ operatorID ].copy( idx );
						}
					}
				}
			}
			
			if ( tokenToAdd == null ) {
				
				//check if we are at the start of a function
				for ( int functionID = 0 ; functionID < functions.length ; functionID ++ ) {
					
					//pick a function to compare to the input
					//furthermore, the function must have a parenthesis immediately after
					String aFunction = functions[ functionID ].getRepresentation() + OpenParenthesis.REPRESENTATION;
					
					//make sure we do not go out of bounds
					if ( idx + aFunction.length() <= this.m_inputExpression.length() - OpenParenthesis.REPRESENTATION.length() ) {
						String pieceOfInputToCheck = this.m_inputExpression.substring( idx , idx + aFunction.length() );
						
						//compare input to function
						if ( pieceOfInputToCheck.equals( aFunction ) ) {
							tokenToAdd = functions[ functionID ].copy( idx );
						}
					}
				}
			}
			
			if ( tokenToAdd == null ) {
				
				//check if we are at the start of a variable
				for ( int variableID = 0 ; variableID < variables.length ; variableID ++ ) {
					
					//pick a variable to compare to the input
					String aVariable = variables[ variableID ].getRepresentation();
					
					//make sure we do not go out of bounds
					if ( idx + aVariable.length() <= this.m_inputExpression.length() ) {
						String pieceOfInputToCheck = this.m_inputExpression.substring( idx , idx + aVariable.length() );
						
						//compare input to variable
						if ( pieceOfInputToCheck.equals( aVariable ) ) {
							tokenToAdd = variables[ variableID ].copy( idx );
						}
					}
				}
			}
			
			//check for commas
			if ( tokenToAdd == null ) {
				String pieceOfInputToCheck = this.m_inputExpression.substring( idx , idx + comma.getRepresentation().length() );
				if ( pieceOfInputToCheck.equals( comma.getRepresentation() ) ) {
					tokenToAdd = comma.copy( idx );
				}
			}
			
			//check for opening parenthesis
			if ( tokenToAdd == null ) {
				String pieceOfInputToCheck = this.m_inputExpression.substring( idx , idx + openParenthesis.getRepresentation().length() );
				if ( pieceOfInputToCheck.equals( openParenthesis.getRepresentation() ) ) {
					tokenToAdd = openParenthesis.copy( idx );
				}
			}
			
			//check for closing parenthesis
			if ( tokenToAdd == null ) {
				String pieceOfInputToCheck = this.m_inputExpression.substring( idx, idx + closeParenthesis.getRepresentation().length() );
				if ( pieceOfInputToCheck.equals( closeParenthesis.getRepresentation() ) ) {
					tokenToAdd = closeParenthesis.copy( idx );
				}
			}
			
			if ( tokenToAdd == null ) {
				
				//if no token was created and we aren't at a space, then we probably
				//have a number
				//keep moving on
				idx++;
			} else {
				
				//there may have been a number before this token
				int endIndexOfNumerical = idx;
				if ( startIndexOfNumerical < endIndexOfNumerical ) {
					
					//create and add numerical token
					String numericalRepresentation = this.m_inputExpression.substring( startIndexOfNumerical , endIndexOfNumerical );
					addNumericalToken( numericalRepresentation );
				}
				startIndexOfNumerical = idx + tokenToAdd.getRepresentation().length();
				
				//add the token
				this.m_unsimplified.add( tokenToAdd );

				//move index forward by length of token just read in
				idx += tokenToAdd.getRepresentation().length();
			}
		}
		
		//there could be a number at the very end
		int endIndexOfNumerical = this.m_inputExpression.length();
		if ( endIndexOfNumerical > startIndexOfNumerical ) {
			
			//create and add numerical token
			String numericalRepresentation = this.m_inputExpression.substring( startIndexOfNumerical , endIndexOfNumerical );
			addNumericalToken( numericalRepresentation );
		}
		this.m_inputLatex = convertToLatex( this.m_unsimplified );
	}
	
	/**
	 * converts the tokenized input into a LaTeX formula
	 * 
	 * @param input 			the input expression to convert to a LaTeX forula
	 * @return 					the LaTeX formula for the input expression 
	 */
	public String convertToLatex( LinkedList < Token > input ) {
		String rtn = "";
		input.moveToStart();
		while ( input.hasCurrent() ) {
			Token aToken = input.get();
			
			//if the token is a combinable, just add it to the LaTeX output
			if ( aToken instanceof Combinable ) {
				Combinable combinableToken = ( Combinable ) aToken;
				rtn += combinableToken.toLatexString();
				
			//if the token is an operator
			} else if ( aToken instanceof Operator ) {
				
				// and the token is an exponentiate operator  
				if ( aToken instanceof ExponentiateOperator ) {
					
					//add the exponentiate operator
					rtn += ExponentiateOperator.LATEX_REPRESENTATION;
					
					if ( input.hasNext() ) {
						int parenthesisDepth = 0;
						LinkedList < Token > exponentTokens = new LinkedList < Token > ();
						
						//check for negative signs
						if ( input.peek() instanceof SubtractOperator ) {
							input.advance();
							exponentTokens.add( input.get() );
						}
						if ( input.hasNext() ) {
							do {
								input.advance();
								Token nextToken = input.get();
								if ( nextToken instanceof OpenParenthesis ) {
									parenthesisDepth ++;
								} else if ( nextToken instanceof CloseParenthesis ) {
									parenthesisDepth --;
								}
								exponentTokens.add( nextToken );
							} while ( input.hasNext() && parenthesisDepth > 0 );
						}
						
						rtn += "{" + convertToLatex( exponentTokens ) + "}";
					}
				
				//otherwise, just add the operator's LaTeX representation
				} else {
					rtn += ( ( Operator ) aToken ).getLatexRepresentation();
				}
			//otherwise, just add the token's representation
			} else {
				rtn += aToken.toLatexString();
			}
			input.advance();
		}
		return rtn;
	}
	
	/**
	 * adds a number to <code>m_unsimplified</code>
	 * 
	 * @param numericalRepresentation			the textual representation of the number
	 * @throws ParseException					if the textual representation is not one of a number
	 */
	public void addNumericalToken( String numericalRepresentation ) throws ParseException {
		
		//decimals are used in approximate mode
		if ( this.m_environment.getOutputMode() == Calculator.OUTPUT_APPROXIMATE ) {
			
			//check to make sure we're given a valid number
			if ( Numerical.isNumerical( numericalRepresentation ) ) {
				
				//create and add numerical
				Numerical numericalToAdd = new Fraction( this.m_environment , numericalRepresentation );
				this.m_unsimplified.add( numericalToAdd );
			} else {
				throw new ParseException( "\"" + numericalRepresentation + "\"" );
			}
			
		//fractions are used in exact mode
		} else if ( this.m_environment.getOutputMode() == Calculator.OUTPUT_EXACT ) {
			
			//make sure we're given a valid fraction or number that can be converted
			//to fractional form
			if ( Fraction.isFraction( numericalRepresentation) ) {
				
				//create and add fraction
				Fraction fractionToAdd = new Fraction( this.m_environment , numericalRepresentation);
				this.m_unsimplified.add( fractionToAdd );
			} else {
				throw new ParseException( numericalRepresentation );
			}
		}
	}
	
	/**
	 * applies implicit multiplication to the
	 * infix expression stored in <code>m_unsimplified</code>
	 */
	public void applyImplicitMultiplication() {
		
		//operators:
		Operator multiply = this.m_environment.getOperator( MultiplyOperator.REPRESENTATION );
		
		Token currentToken;
		Token nextToken;
		
		//check for opposite signs (negative signs)
		
		//first, check if the first token is a negative sign
		this.m_unsimplified.moveToStart();
		
		//if first token is a subtraction sign, then replace it with -1 because
		//it is a negative sign
		if ( this.m_unsimplified.get() instanceof SubtractOperator ) {
			this.m_unsimplified.replace( Numerical.NEGATIVE_ONE );
			this.m_unsimplified.insert( new NegateOperator() );
		}
		
		//then go through the rest of the tokens
		while ( this.m_unsimplified.hasNext() ) {
			
			currentToken = this.m_unsimplified.get();
			nextToken = this.m_unsimplified.peek();
			
			//look for subtraction signs
			if ( nextToken instanceof SubtractOperator ) {
				
				//if the token before the subtraction sign cannot use the
				//subtract operation, then this is a negative sign
				//the only exception if the token before this subtraction sign
				//is a close parenthesis, e.g. (5x+5)-1, then the - is a subtraction sign
				if ( !( currentToken instanceof Combinable ) && !( currentToken instanceof CloseParenthesis) ) {
					
					//change the subtraction sign to a negative one
					this.m_unsimplified.replaceNext( Numerical.NEGATIVE_ONE );

					//add a special negate operator after that
					if ( this.m_unsimplified.hasNext() ) {
						this.m_unsimplified.advance();
						if ( this.m_unsimplified.hasNext() ) {
							this.m_unsimplified.insert( new NegateOperator() );
						}
					}
				}
			}
			
			//move forward
			this.m_unsimplified.advance();
		}
		
		//check for implicit multiplication
		this.m_unsimplified.moveToStart();
		while ( this.m_unsimplified.hasNext() ) {
			
			//determine the current and the next token
			currentToken = this.m_unsimplified.get();
			nextToken = this.m_unsimplified.peek();
			
			if ( currentToken != null && nextToken != null ) {
				
				//all functions are followed by opening parenthesis,
				//so there is no implicit multiplication if the current token
				//is a function
				if ( !( currentToken instanceof Function ) ) {
					
					//if the current token and the next token are parenthesis or
					//combinable values, then there is implicit multiplication
					if ( ( currentToken instanceof Combinable || currentToken instanceof CloseParenthesis) &&
						 ( nextToken instanceof Combinable || nextToken instanceof OpenParenthesis ) ) {
						
						//add the implicit multiplication sign
						this.m_unsimplified.insert( multiply );
						//System.out.println( currentToken.getRepresentation() + " " + nextToken.getRepresentation());
						this.m_unsimplified.advance();
						currentToken = this.m_unsimplified.get();
					}
				}
			}
			
			//move forward
			this.m_unsimplified.advance();
		}
	}
	
	/**
	 * converts the infix expression stored in <code>m_unsimplified</code>
	 * into a postfix expression stored in <code>m_unsimplifiedPostifx</code>
	 */
	public void convertToPostfix() {
		
		//go through all the tokens in the tokenized expression
		this.m_unsimplified.moveToStart();
		while ( this.m_unsimplified.hasCurrent() ) {
			
			//read a token
			Token currentToken = this.m_unsimplified.get();
			
			//put functions onto the operators stack
			if ( currentToken instanceof Function ) {
				this.m_operatorStack.push( currentToken );
				
			//all combinables that are not functions are of type polynomial
			//which work like numbers.
			} else {
				
				//push combinables that are not functions onto the postfix queue
				if ( currentToken instanceof Combinable ) {
					this.m_unsimplifiedPostfix.push( currentToken );
				}
			}
			
			//if a comma is read
			if ( currentToken instanceof Comma ) {

				//pop from the operator stack until a left parenthesis is encountered
				while ( this.m_operatorStack.peek() instanceof OpenParenthesis ) {
					this.m_unsimplifiedPostfix.push( this.m_operatorStack.pop() );
				}
		
				//if all tokens have been popped and no open parenthesis encountered
				//then there's a syntax error
				if ( this.m_operatorStack.peek() == null ) {
					throw new SyntaxException( Text.Calculate.ErrorMessages.MISSING_OPEN_PARENTHESIS , 0 );
				}
			}
			
			//if an operator is read
			if ( currentToken instanceof Operator ) {
				
				//as long as the operator stack has an operator at the top
				while ( this.m_operatorStack.peek() instanceof Operator ) {
					Operator currentOperator = ( Operator ) currentToken;
					Operator operatorAtTop = ( Operator ) this.m_operatorStack.peek();
					
					//if the operator just read is left associative and precendence
					//equal to the operator at the top, pop operator at top off and 
					//put onto postfix queue
					if ( currentOperator.getAssociativity() == Operator.LEFT_ASSOCIATIVE && 
							currentOperator.getPrecedence() == operatorAtTop.getPrecedence() ) {
							this.m_unsimplifiedPostfix.push( this.m_operatorStack.pop() );
						
					//if the operator just read has lower precedence than the
					//operator at the top, pop it off and put onto postfix queue
					} else if ( currentOperator.getPrecedence() < operatorAtTop.getPrecedence() ) {
						this.m_unsimplifiedPostfix.push( this.m_operatorStack.pop() );
						
					//otherwise, stop
					} else {
						break;
					}
				}
				
				//finally, put the operator just read onto the stack
				this.m_operatorStack.push( currentToken );
			}
			
			//if open parenthesis is read, put it onto the operator stack
			if ( currentToken instanceof OpenParenthesis ) {
				this.m_operatorStack.push( currentToken );
			}
			
			//if close parenthesis read
			if ( currentToken instanceof CloseParenthesis ) {
				
				//keep popping off operator stack onto postfix queue
				//until an open parenthesis is at the top
				while ( this.m_operatorStack.peek() != null && 
						!( this.m_operatorStack.peek() instanceof OpenParenthesis ) ) {
					this.m_unsimplifiedPostfix.push( this.m_operatorStack.pop() );
				}
				
				//if no open parenthesis found, then there are mismatched parentheses
				if ( this.m_operatorStack.peek() == null ) {
					throw new SyntaxException( Text.Calculate.ErrorMessages.MISSING_OPEN_PARENTHESIS , 0 );
				}
				
				//then remove the open parenthesis from the stack
				this.m_operatorStack.pop();
				
				//if a function is left at the top of the operator stack, pop that
				//and put into postfix queue
				if ( this.m_operatorStack.peek() instanceof Function ) {
					this.m_unsimplifiedPostfix.push( this.m_operatorStack.pop() );
				}
			}
			this.m_unsimplified.advance();
		}
		
		//after all tokens have been read, 
		//pop all operators off operator stack and push onto postfix queue
		while ( this.m_operatorStack.peek() != null ) {
			
			//if a parenthesis is encountered, there are mismatched parentheses
			if ( this.m_operatorStack.peek() instanceof OpenParenthesis ||
					this.m_operatorStack.peek() instanceof CloseParenthesis ) {
				throw new SyntaxException ( Text.Calculate.ErrorMessages.EXTRA_PRENTHESIS , this.m_operatorStack.peek().getIndexInInput() );
			}
			this.m_unsimplifiedPostfix.push( this.m_operatorStack.pop() );
		}
		
		//postcondition: postfix queue contains the original infix expression
		//converted into postfix
	}
	
	/**
	 * simplifies the postfix expression stored in <code>m_unsimplifiedPostfix</code>
	 */
	public void simplify() {
		
		//keep reading in tokens while there are tokens left in the queue
		while ( this.m_unsimplifiedPostfix.hasNext() ) {
			Token nextToken = this.m_unsimplifiedPostfix.pop();
			
			//if the token is a combinable, i.e. a number or expression to which
			//operations may be applied
			if ( nextToken instanceof Combinable ) {
			
				//put it on the evaluation stack
				this.m_evaluationStack.push( ( Combinable ) nextToken );
				
			//if the token is an operator
			} else if ( nextToken instanceof Operator ) {
				Operator nextOperator = ( Operator ) nextToken;
				
				//take two combinables off the stack and apply the operation to them
				Combinable rightOperand = this.m_evaluationStack.pop();
				Combinable leftOperand = this.m_evaluationStack.pop();
				
				//System.out.println( leftOperand.getRepresentation() + " " + nextOperator.getRepresentation() + " " +  rightOperand.getRepresentation());
				//System.out.println( leftOperand.getClass().getSimpleName() );
				//System.out.println( rightOperand.getClass().getSimpleName() );
				
				//if insufficient values, then it is a syntax error
				if ( rightOperand == null || leftOperand == null ) {
					throw new SyntaxException( Text.Calculate.ErrorMessages.getInsufficientOperandsMessage( nextOperator.getRepresentation() ) , nextOperator.getIndexInInput() );
				} else {
					
					Combinable operationResult = null;
					
					//determine the operation and apply it
					if ( nextOperator instanceof AddOperator ) {
						operationResult = leftOperand.add( rightOperand );
					} else if ( nextOperator instanceof SubtractOperator ) {
						operationResult = leftOperand.subtract( rightOperand );
					} else if ( nextOperator instanceof MultiplyOperator ) {
						operationResult = leftOperand.multiply( rightOperand );
					} else if ( nextOperator instanceof DivideOperator ) {
						operationResult = leftOperand.divide( rightOperand );
					} else if ( nextOperator instanceof ExponentiateOperator ) {
						operationResult = leftOperand.exponentiate( rightOperand );
					} else if ( nextOperator instanceof NegateOperator ) {
						operationResult = leftOperand.multiply( rightOperand );
					} else {
						System.out.println( "FUCK: in Expression.simplify() " + nextOperator.getRepresentation() );
					}
					
					if ( operationResult == null ) {
						System.out.println( "Fuck! Operator: " + nextOperator.getRepresentation() );
						System.exit(0);
					}
					//push the result of the operation back onto the stack
					this.m_evaluationStack.push( operationResult );
				}
				
			//if the token is a function
			} else if ( nextToken instanceof Function ) {
				Function nextFunction = ( Function ) nextToken;
				//determine how many values the function requires
				int numArguments = nextFunction.getNumArguments();
				
				//take as many arguments as needed off the stack
				Combinable[] arguments = new Combinable[ numArguments ];
				for ( int argIdx = 0 ; argIdx < numArguments; argIdx ++ ) {
					arguments[ argIdx ] = this.m_evaluationStack.pop();
					
					//if insufficient arguments, then it is a syntax error
					if ( arguments[ argIdx ] == null ) {
						throw new SyntaxException( Text.Calculate.ErrorMessages.getInsufficientArgumentsMessage( nextFunction.getRepresentation() ) , nextFunction.getIndexInInput() );
					}
				}
				
				//apply the arguments to the function
				//so that it can update its textual representation
				nextFunction.applyArguments( arguments );
			}
		}
		
		this.m_simplified = this.m_evaluationStack.pop();

		//if there are more tokens left on the evaluation stack,
		//then the input is invalid and there are too many operands/arguments
		if ( this.m_evaluationStack.hasNext() ) {
			Token extraInput = this.m_evaluationStack.pop();
			throw new SyntaxException( Text.Calculate.ErrorMessages.getTooManyOperandsMessage( extraInput.getRepresentation() ) , extraInput.getIndexInInput() );
		}
	}
	
	public Combinable getResult() {
		return this.m_simplified;
	}
	
	public String getLatexInput() {
		return this.m_inputLatex;
	}
	
	//DEBUG:
	public void printTokens() {
		this.m_unsimplified.moveToStart();
		while ( this.m_unsimplified.hasCurrent() ) {
			Token current = this.m_unsimplified.get();
			System.out.println( current.getClass().getSimpleName() + ": " + current.getRepresentation() );
			this.m_unsimplified.advance();
		}
	}//*/
	
	
	//DEBUG:
	public void printPostfixQueue() {
		while ( this.m_unsimplifiedPostfix.hasNext() ) {
			System.out.print( " " + this.m_unsimplifiedPostfix.pop().getRepresentation() + " ");
		}
	}//*/
}
