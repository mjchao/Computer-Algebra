package calculate.structures.polynomial;

import java.math.BigDecimal;
import java.math.BigInteger;

import calculate.Calculator;
import calculate.operators.DivideOperator;
import calculate.operators.SubtractOperator;
import calculate.structures.Combinable;

/**
 * immutable <code>Numerical</code> that represents any decimal exactly
 */
public class Fraction extends Numerical {

	private BigInteger m_numerator;
	private BigInteger m_denominator;
	
	public Fraction( Calculator environment , BigInteger numerator , BigInteger denominator ) {
		super( environment , new BigDecimal( numerator.doubleValue() / denominator.doubleValue() ));
		this.m_numerator = numerator;
		this.m_denominator = denominator;
		simplify();
		determineRepresentation();
	}
	
	public Fraction( Calculator environment , BigDecimal numerator , BigDecimal denominator ) {
		super ( environment , new BigDecimal( numerator.doubleValue() / denominator.doubleValue() ) );
		BigDecimal integerNumerator = numerator;
		BigDecimal integerDenominator = denominator;
		while ( integerNumerator.toString().contains( "." ) || integerDenominator.toString().contains( ".") ) {
			integerNumerator = integerNumerator.multiply( BigDecimal.TEN ).stripTrailingZeros();
			integerDenominator = integerDenominator.multiply( BigDecimal.TEN ).stripTrailingZeros();
		}
		this.m_numerator = integerNumerator.toBigInteger();
		this.m_denominator = integerDenominator.toBigInteger();
		simplify();
		determineRepresentation();
	}
	
	
	public Fraction( Calculator environment , String value ) {
		this ( environment , new BigDecimal( value ) , BigDecimal.ONE );
	}
	
	public Fraction( Calculator environment , BigDecimal value ) {
		this( environment , value.toString() );
	}
	
	public Fraction( Calculator environment , Numerical value ) {
		this ( environment , value.getNumericalValue() , BigDecimal.ONE );
	}

	/**
	 * @param representation			textual representation of a token to be created
	 * @return							if the textual representation of the token is that of a fraction
	 */
	final public static boolean isFraction( String representation ) {
		//all terminating numericals can be represented as fractions
		return isNumerical( representation );
	}
	
	/**
	 * @param arg1				a <code>Fraction</code>
	 * @param arg2				a <code>Fraction</code>
	 * @return					the greatest common divisor of arg1 and arg2,
	 * 							given that they are both integers
	 */
	final public static Fraction gcd( Fraction arg1 , Fraction arg2 ) {
		
		//make sure we are dealing with integers, because
		//we cannot get a gcd if one of the arguments is a fraction
		BigInteger a = arg1.getFractionNumerator();
		BigInteger b = arg2.getFractionNumerator();
		if ( arg1.isInteger() && arg2.isInteger() ) {
			return new Fraction( arg1.getEnvironment() , gcd( a , b ) , BigInteger.ONE );
		} else {
			return Numerical.ONE;
		}
	}
	
	/**
	 * finds the greatest common divisor of two numbers a and b
	 * 
	 * @param a			an integer
	 * @param b			another integer
	 * @return			the greatest common divisor of a and b
	 */
	final private static BigInteger gcd( BigInteger a , BigInteger b )
	{
		if ( b.compareTo( BigInteger.ZERO ) == 0 ) {
			return a;
		} else {
			return gcd( b , a.mod( b.abs() ) );
		}
	}
	
	/**
	 * finds the lowest common multiple of two numbers a and b
	 * 
	 * @param a			an integer
	 * @param b			another integer
	 * @return			the lowest common multiple of a and b
	 */
	final private static BigInteger lcm( BigInteger a , BigInteger b )
	{
		BigInteger gcd = gcd( a , b);
		return a.multiply( b ).divide( gcd );
	}
	
	private void determineRepresentation() {
		if ( this.m_denominator.equals( BigInteger.ONE ) ) {
			setRepresentation( this.m_numerator.toString() );
		} else {
			setRepresentation( this.m_numerator.toString() + DivideOperator.REPRESENTATION + this.m_denominator.toString() );
		}
	}
	
	private BigInteger getFractionNumerator() {
		return this.m_numerator;
	}
	
	public Fraction getDenominatorAsFraction() {
		return new Fraction( getEnvironment() , this.m_denominator , BigInteger.ONE );
	}
	
	private BigInteger getFractionDenominator() {
		return this.m_denominator;
	}
	
	@Override
	public boolean isInteger() {
		return this.m_denominator.equals( BigInteger.ONE );
	}
	
	final public void simplify()
	{
		BigInteger gcd = gcd(this.m_numerator, this.m_denominator);
		this.m_numerator = this.m_numerator.divide(gcd);
		this.m_denominator = this.m_denominator.divide(gcd);
		determineRepresentation();
	}
	
	@Override
	public Combinable add( Combinable augend ) {
		
		//only deal with adding numericals
		if ( augend instanceof Numerical ) {
			Fraction fractionAugend;
			if ( augend instanceof Fraction ) {
				fractionAugend = ( Fraction ) augend;
			} else {
				Numerical numericalAugend = ( Numerical ) augend;
				fractionAugend = new Fraction( getEnvironment() , numericalAugend );
			}
			
			//calculate a new denominator
			BigInteger newDenominator = lcm( this.getFractionDenominator() , fractionAugend.getFractionDenominator() );
			
			//calculate a new numerator
			BigInteger thisMultiplier = newDenominator.divide( this.getFractionDenominator() );
			BigInteger thisNewNumerator = this.getFractionNumerator().multiply( thisMultiplier );
			BigInteger augendMultiplier = newDenominator.divide( fractionAugend.getFractionDenominator() );
			BigInteger augendNewNumerator = fractionAugend.getFractionNumerator().multiply( augendMultiplier );
			BigInteger newNumerator = thisNewNumerator.add( augendNewNumerator );
			
			return new Fraction( getEnvironment() , newNumerator , newDenominator );
			
		} else {
			return super.add( augend );
		}
	}
	
	@Override
	public Combinable subtract( Combinable subtrahend ) {
		
		//only deal with subtracting numericals
		if ( subtrahend instanceof Numerical ) {
			Fraction fractionSubtrahend;
			if ( subtrahend instanceof Fraction ) {
				fractionSubtrahend = ( Fraction ) subtrahend;
			} else {
				Numerical numericalSubtrahend = ( Numerical ) subtrahend;
				fractionSubtrahend = new Fraction( getEnvironment() , numericalSubtrahend );
			}
			
			//calculate a new denominator
			BigInteger newDenominator = lcm( this.getFractionDenominator() , fractionSubtrahend.getFractionDenominator() );
			
			//calculate a new numerator
			BigInteger thisMultiplier = newDenominator.divide( this.getFractionDenominator() );
			BigInteger thisNewNumerator = this.getFractionNumerator().multiply( thisMultiplier );
			BigInteger subtrahendMultiplier = newDenominator.divide( fractionSubtrahend.getFractionDenominator() );
			BigInteger subtrahendNewNumerator = fractionSubtrahend.getFractionNumerator().multiply( subtrahendMultiplier );
			BigInteger newNumerator = thisNewNumerator.subtract( subtrahendNewNumerator );
			
			return new Fraction( getEnvironment() , newNumerator , newDenominator );
		} else {
			return super.subtract( subtrahend );
		}
	}
	
	@Override
	public Combinable multiply( Combinable multiplicand ) {
		
		//only deal with multiplying numericals
		if ( multiplicand instanceof Numerical ) {
			Fraction fractionMultiplicand;
			if ( multiplicand instanceof Fraction ) {
				fractionMultiplicand = ( Fraction ) multiplicand;
			} else {
				Numerical numericalMultiplicand = ( Numerical ) multiplicand;
				fractionMultiplicand = new Fraction( getEnvironment() , numericalMultiplicand );
			}
			
			//calculate a new denominator
			BigInteger newDenominator = this.getFractionDenominator().multiply( fractionMultiplicand.getFractionDenominator() );
			
			//calculate a new numerator
			BigInteger newNumerator = this.getFractionNumerator().multiply( fractionMultiplicand.getFractionNumerator() );
			
			return new Fraction( getEnvironment() , newNumerator , newDenominator );
		} else {
			return super.multiply( multiplicand );
		}
	}
	
	@Override
	public Fraction multiplyByNegativeOne() {
		return ( Fraction ) this.multiply( Numerical.NEGATIVE_ONE );
	}
	
	@Override
	public Combinable divide( Combinable dividend ) {
		
		//only deal with dividing numericals
		if ( dividend instanceof Numerical ) {
			Fraction fractionDividend;
			if ( dividend instanceof Fraction ) {
				fractionDividend = ( Fraction ) dividend;
			} else {
				Numerical numericalDividend = ( Numerical ) dividend;
				fractionDividend = new Fraction( getEnvironment() , numericalDividend );
			}
			
			//calculate a new denominator
			BigInteger newDenominator = this.getFractionDenominator().multiply( fractionDividend.getFractionNumerator() );
			
			//calculate a new numerator
			BigInteger newNumerator = this.getFractionNumerator().multiply( fractionDividend.getFractionDenominator() );
			
			return new Fraction( getEnvironment() , newNumerator , newDenominator );
		} else {
			return super.divide( dividend );
		}
	}
	
	@Override
	public Combinable exponentiate( Combinable exponent ) {
		
		//only deal with exponentiating numericals
		if ( exponent instanceof Numerical ) {
			Numerical numericalExponent = ( Numerical ) exponent;
			double newNumerator = Math.pow( this.m_numerator.intValue() , numericalExponent.getNumericalValue().doubleValue() );
			double newDenominator = Math.pow( this.m_denominator.intValue() , numericalExponent.getNumericalValue().doubleValue() );
			return new Fraction( getEnvironment() , new BigDecimal( newNumerator ) , new BigDecimal( newDenominator ) );
		} else {
			return super.exponentiate( exponent );
		}
	}
	
	@Override
	public String toLatexString() {
		if ( this.m_denominator.equals( BigInteger.ONE ) ) {
			return this.m_numerator.toString(); 
		} else {
			
			//add the negative sign in front if this fraction is negative
			if ( this.compareTo( Numerical.ZERO ) < 0 ) {
				return SubtractOperator.LATEX_REPRESENTATION + "\\frac{" + this.m_numerator.abs() + "}{" + this.m_denominator.abs() + "}";
			} else {
				return "\\frac{" + this.m_numerator.toString() + "}{" + this.m_denominator.toString() + "}";
			}
		}
	}
	
	@Override
	public Fraction copy( int indexInInput ) {
		Fraction rtn = clone();
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
	
	@Override
	public Fraction clone() {
		return new Fraction( getEnvironment() , this.m_numerator , this.m_denominator );
	}
}
