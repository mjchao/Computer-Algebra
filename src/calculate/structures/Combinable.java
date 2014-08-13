package calculate.structures;

import calculate.Calculator;
import calculate.operators.AddOperator;
import calculate.structures.functional.FunctionExpression;
import calculate.structures.polynomial.Polynomial;

public class Combinable extends Token implements Comparable < Combinable > {

	/**
	 * the polynomials associated with this combinable. for example, in the expression
	 * 5x^2+4x+sin(2x)+cos(2x), the polynomial tokens are 5x^2 and 4x
	 */
	private Polynomial m_polynomialPart = null;
	
	/**
	 * the functions associated with this combinable. for example, in the expression
	 * 5x^2+4x+sin(2x)+cos(2x), the function tokens are sin(2x) and cos(2x)
	 */
	private FunctionExpression m_functionPart = null;
	
	private Calculator m_environment;
	
	public Combinable( Calculator environment ) {
		super( "" );
		this.m_environment = environment;
	}
	
	public Combinable( String representation , Calculator environment ) {
		super( representation );
		this.m_environment = environment;
	}
	
	public Combinable( String representation , Polynomial polynomialPart , FunctionExpression functionPart ) {
		super( representation );
		this.m_polynomialPart = polynomialPart;
		this.m_functionPart = functionPart;
	}
	
	/**
	 * @return			the environment, containing important data, in which 
	 * 					calculations are executed.
	 */
	public Calculator getEnvironment() {
		return this.m_environment;
	}
	
	public Polynomial getPolynomialPart() {
		return this.m_polynomialPart;
	}
	
	public FunctionExpression getFunctionPart() {
		return this.m_functionPart;
	}

	public Combinable add( Combinable augend ) {
		
		//add the polynomial parts together
		Polynomial thisPolynomialPart = this.m_polynomialPart;
		Polynomial augendPolynomialPart = augend.getPolynomialPart();
		Polynomial addedPolynomialPart;
		
		if ( thisPolynomialPart == null && augendPolynomialPart == null ) {
			addedPolynomialPart = null;
		} else if ( thisPolynomialPart == null ) {
			addedPolynomialPart = augendPolynomialPart;
		} else if ( augendPolynomialPart == null ) {
			addedPolynomialPart = thisPolynomialPart;
		} else {
			addedPolynomialPart = ( Polynomial ) thisPolynomialPart.add( augendPolynomialPart );
		}
		
		//add the function parts together
		FunctionExpression thisFunctionPart = this.m_functionPart;
		FunctionExpression augendFunctionPart = augend.getFunctionPart();
		FunctionExpression addedFunctionPart;
		
		if ( thisFunctionPart == null && augendFunctionPart == null ) {
			addedFunctionPart = null;
		} else if ( thisFunctionPart == null ) {
			addedFunctionPart = augendFunctionPart;
		} else if ( augendFunctionPart == null ) {
			addedFunctionPart = thisFunctionPart;
		} else {
			addedFunctionPart = thisFunctionPart.add( augendFunctionPart );
		}
		
		//express the result in the form polynomial+function
		String newRepresentation = null;
		if ( addedPolynomialPart == null && addedFunctionPart == null ) {
			newRepresentation = "";
		} else if ( addedPolynomialPart == null && addedFunctionPart != null ) {
			newRepresentation = addedFunctionPart.getRepresentation();
		} else if ( addedFunctionPart == null && addedPolynomialPart != null ) {
			newRepresentation = addedPolynomialPart.getRepresentation();
		} else if ( addedPolynomialPart != null && addedFunctionPart != null ){
			newRepresentation = addedPolynomialPart.getRepresentation() + AddOperator.REPRESENTATION + addedFunctionPart.getRepresentation();
		}
		
		return new Combinable( newRepresentation , addedPolynomialPart , addedFunctionPart );
	}
	
	public Combinable subtract( Combinable subtrahend ) {
		
		//subtract the polynomial parts
		Polynomial thisPolynomialPart = this.m_polynomialPart;
		Polynomial subtrahendPolynomialPart = subtrahend.getPolynomialPart();
		Polynomial subtractedPolynomialPart;
		
		if ( thisPolynomialPart == null && subtrahendPolynomialPart == null ) {
			subtractedPolynomialPart = null;
		} else if ( thisPolynomialPart == null ) {
			subtractedPolynomialPart = subtrahendPolynomialPart.multiplyByNegativeOne();
		} else if ( subtrahendPolynomialPart == null ) {
			subtractedPolynomialPart = thisPolynomialPart;
		} else {
			subtractedPolynomialPart = ( Polynomial ) thisPolynomialPart.subtract( subtrahendPolynomialPart );
		}
		
		//subtract the function parts
		FunctionExpression thisFunctionPart = this.m_functionPart;
		FunctionExpression subtrahendFunctionPart = subtrahend.getFunctionPart();
		FunctionExpression subtractedFunctionPart;
		
		if ( thisFunctionPart == null && subtrahendFunctionPart == null ) {
			subtractedFunctionPart = null;
		} else if ( thisFunctionPart == null ) {
			subtractedFunctionPart = subtrahendFunctionPart.multiplyByNegativeOne();
		} else if ( subtrahendFunctionPart == null ) {
			subtractedFunctionPart = thisFunctionPart;
		} else {
			subtractedFunctionPart = thisFunctionPart.subtract( subtrahendFunctionPart );
		}
		
		//express the result in the form polynomials + functions
		String newRepresentation = null;
		if ( subtractedPolynomialPart == null && subtractedFunctionPart == null ) {

			//should never happen
			assert false;
			newRepresentation = "";
		} else if ( subtractedPolynomialPart == null && subtractedFunctionPart != null ) {
			newRepresentation = subtractedFunctionPart.getRepresentation();
		} else if ( subtractedFunctionPart == null && subtractedPolynomialPart != null ) {
			newRepresentation = subtractedPolynomialPart.getRepresentation();
		} else if ( subtractedPolynomialPart != null && subtractedFunctionPart != null ){
			newRepresentation = subtractedPolynomialPart.getRepresentation() + AddOperator.REPRESENTATION + subtractedPolynomialPart.getRepresentation();
		}
		
		return new Combinable( newRepresentation , subtractedPolynomialPart, subtractedFunctionPart );
	}
	
	public Combinable multiply( Combinable multiplicand ) {
		
		//distributive property:
		
		//multiply this polynomial part by the multiplicand
		Polynomial thisPolynomialPart = this.m_polynomialPart;
		Polynomial multiplicandPolynomialPart = multiplicand.getPolynomialPart();
		FunctionExpression multiplicandFunctionPart = multiplicand.getFunctionPart();
		
		Combinable polynomialPartResult = null;
		if ( thisPolynomialPart != null ) {
			
			//multiply polynomial by multiplicand polynomial
			Polynomial polynomialTimesPolynomial = null;
			if ( multiplicandPolynomialPart != null ) {
				polynomialTimesPolynomial = ( Polynomial ) thisPolynomialPart.multiply( multiplicandPolynomialPart );
			}
			
			//multiply polynomial by multiplicand functions
			Combinable polynomialTimesFunction = null;
			if ( multiplicandFunctionPart != null ) {
				polynomialTimesFunction = thisPolynomialPart.multiply( multiplicandFunctionPart );
			}
			
			//add the results together
			if ( polynomialTimesPolynomial == null && polynomialTimesFunction == null ) {
				//polynomialPartResult = null;
			} else if ( polynomialTimesPolynomial == null ) {
				polynomialPartResult = polynomialTimesFunction;
			} else if ( polynomialTimesFunction == null ) {
				polynomialPartResult = polynomialTimesPolynomial;
			} else {
				polynomialPartResult = polynomialTimesPolynomial.add( polynomialTimesFunction );
			}
		}
		
		//multiply multiplicand polynomial part by this function part
		//and multiply this function part by the multiplicand function part
		FunctionExpression thisFunctionPart = this.m_functionPart;
		
		//use commutivity of multiplication to use the polynomial's multiply() function
		//in multiplying this function part by the multiplicand function part
		Combinable functionTimesPolynomial = null;
		if ( thisFunctionPart != null && multiplicandPolynomialPart != null ) {
			functionTimesPolynomial = multiplicandPolynomialPart.multiply( thisFunctionPart );
		}
		
		//then multiply this function part by the multiplicand function part
		Combinable functionTimesFunction;
		if ( thisFunctionPart == null || multiplicandFunctionPart == null ) {
			functionTimesFunction = null;
		} else {
			functionTimesFunction = thisFunctionPart.multiply( multiplicandFunctionPart );
		}
		
		//finally, add the results together
		Combinable functionPartResult;
		if ( functionTimesPolynomial == null && functionTimesFunction == null ) {
			functionPartResult = null;
		} else if ( functionTimesPolynomial == null ) {
			functionPartResult = functionTimesFunction;
		} else if ( functionTimesFunction == null ) {
			functionPartResult = functionTimesPolynomial;
		} else {
			functionPartResult = functionTimesPolynomial.add( functionTimesPolynomial );
		}
		
		//combine the function and polynomial parts
		Combinable multiplyResult;
		if ( polynomialPartResult == null && functionPartResult == null ) {
			//should never happen
			multiplyResult = null;
		} else if ( polynomialPartResult == null ) {
			multiplyResult = functionPartResult;
		} else if ( functionPartResult == null ) {
			multiplyResult = polynomialPartResult;
		} else {
			multiplyResult = functionPartResult.add( polynomialPartResult );
		}
		return multiplyResult;
	}
	
	public Combinable divide( Combinable dividend ) {
		
		//express the result in the form (this)/(dividend)
		return null;
	}
	
	public Combinable exponentiate( Combinable exponent ) {
		//TODO
		return null;
	}
	
	public Combinable mod( Combinable toMod ) {
		//TODO
		return null;
	}
	
	/**
	 * @return			the number of terms in this <code>Combinable</code>
	 */
	public int countNumTerms() {
		int numTerms = 0;
		
		//count terms in polynomial part
		if ( this.m_polynomialPart != null ) {
			numTerms += this.m_polynomialPart.countNumTerms();
		}
		
		//count terms in function part
		if ( this.m_functionPart != null ) {
			numTerms += this.m_functionPart.countNumTerms();
		}
		
		return numTerms;
	}

	/**
	 * @param combinableToCompare			<code>Combinable</code> to compare
	 * @return								if this <code>Combinable</code> has the same value
	 * 										as the given <code>Combinable</code>
	 */
	@Override
	public boolean equals( Object toCompare ) {
		if ( toCompare instanceof Combinable ) {
			Combinable combinableToCompare = ( Combinable ) toCompare;
			
			//compare polynomial parts
			if ( this.m_polynomialPart == null ) {
				
				//in order for the polynomial part of this combinable to be equal
				//to the polynomial part of the other combinable 
				
				//either both polynomial parts do not exist
				if ( combinableToCompare.getPolynomialPart() != null ) {
					return false;
				}
			} else {
				
				//or both polynomial parts are equal
				if ( ! this.m_polynomialPart.equals( combinableToCompare.getPolynomialPart() ) ) {
					return false;
				}
			}
			
			//compare function parts
			if ( this.m_functionPart == null ) {
				
				//in order for the function part of this combinable to be equal
				//to the function part of the other combinable
				
				//either both function parts do not exist
				if ( combinableToCompare.getFunctionPart() != null ) {
					return false;
				}
			} else {
	
				//or both function parts are equal
				if ( ! this.m_functionPart.equals( combinableToCompare.getFunctionPart() ) ) {
					return false;
				}
			}
			
			
			//if all conditions are met, then this Combinable is equal to
			//the given Combinable
			return true;
		} else {
			return super.equals( toCompare );
		}
	}
	
	/**
	 * compares this <code>Combinable</code> to another <code>Combinable</code>.
	 * <p>
	 * ALL <code>compareTo(...)</code> METHODS MUST CHECK FOR COMPARE TO ZERO.
	 * 
	 * @param toCompare				a <code>Combinable</code> to compare
	 * @return						positive if this <code>Combinable</code> is less than the given one.
	 * 								0 if this <code>Combinable</code> equals the given one.
	 * 								negative if this <code>Combinable</code> is greater than the given <code>Combinable</code>
	 */
	@Override
	public int compareTo( Combinable toCompare ) {
		return 1;
	}
	
	@Override
	public Combinable copy(int indexInInput) {
		Combinable rtn = new Combinable( getRepresentation() , this.m_environment );
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
	
	@Override
	public Combinable clone() {
		return copy( getIndexInInput() );
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		return getRepresentation();
	}
	
	/**
	 * determines the LaTeX rendering of this <code>Combinable</code>
	 * @return
	 */
	@Override
	public String toLatexString() {
		//TODO
		return "";
	}
}
