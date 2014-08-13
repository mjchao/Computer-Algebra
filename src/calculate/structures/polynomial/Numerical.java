package calculate.structures.polynomial;

import java.math.BigDecimal;
import java.math.RoundingMode;

import calculate.Calculator;
import calculate.structures.Combinable;

public class Numerical extends Constant {

	final public static Fraction NEGATIVE_ONE = new Fraction( null , "-1" );
	final public static Fraction ZERO = new Fraction( null , "0" );
	final public static Fraction ONE = new Fraction( null , "1" );
	final public static Fraction TWO = new Fraction( null , "2" );
	
	protected BigDecimal m_value;
	
	public Numerical( Calculator environment , BigDecimal value ) {
		super ( environment , value.toString() );
		this.m_value = value;
		this.setDenominatorAndExponentAsOne();
	}
	
	public static Fraction ONE( Calculator environment ) {
		return new Fraction( environment , "1" );
	}
	
	public static Fraction NEGATIVE_ONE( Calculator environment ) {
		return new Fraction( environment , "-1" );
	}
	
	public static Fraction ZERO( Calculator environment ) {
		return new Fraction( environment , "0" );
	}
	
	public static Fraction TWO( Calculator environment ) {
		return new Fraction( environment , "2" );
	}
	
	/**
	 * creates a <code>Numerical</code> with the given value
	 * 
	 * @param environment
	 * @param value					a value to be converted to a decimal
	 */
	public Numerical ( Calculator environment , String value ) {
		this ( environment , new BigDecimal( value ) );
	}
	
	public BigDecimal getNumericalValue() {
		return this.m_value;
	}

	@Override
	public Combinable add( Combinable augend ) {
		
		//only deal with adding numerical augends
		if ( augend instanceof Numerical ) {
			
			//add the numerical value
			Numerical numericalAugend = ( Numerical ) augend;
			BigDecimal newValue = this.m_value.add( numericalAugend.getNumericalValue() );
			return new Numerical( getEnvironment() , newValue );
		} else {
			return super.add( augend );
		}
	}
	
	@Override
	public Combinable subtract( Combinable subtrahend ) {
		
		//only deal with subtracting numerical augends
		if ( subtrahend instanceof Numerical ) {
			
			//subtract the numerical value
			Numerical numericalSubtrahend = ( Numerical ) subtrahend;
			BigDecimal newValue = this.m_value.subtract( numericalSubtrahend.getNumericalValue() );
			return new Numerical( getEnvironment() , newValue );
		} else { 
			return super.subtract( subtrahend );
		}
	}
	
	@Override
	public Combinable multiply( Combinable multiplicand ) {
		
		//only deal with multiplying numerical multiplicands
		if ( multiplicand instanceof Numerical ) {
			
			//multiply the numerical values
			Numerical numericalMultiplicand = ( Numerical ) multiplicand;
			BigDecimal newValue = this.m_value.multiply( numericalMultiplicand.getNumericalValue() );
			return new Numerical( getEnvironment() , newValue );
		} else {
			return super.multiply( multiplicand );
		}
	}
	
	@Override
	public Combinable divide( Combinable dividend ) {
		
		//only deal with dividing by numerical dividends
		if ( dividend instanceof Numerical ) {
			
			//divide the numerical values
			Numerical numericalDividend = ( Numerical ) dividend;
			BigDecimal newValue;
			try {
				newValue = this.m_value.divide( numericalDividend.getNumericalValue() );
			} catch ( ArithmeticException nonTerminating ) {
				Calculator divisionEnvironment = getEnvironment();
				if ( divisionEnvironment == null ) {
					divisionEnvironment = dividend.getEnvironment();
				}
				int scale = divisionEnvironment.getScale();
				newValue = this.m_value.divide( numericalDividend.getNumericalValue(), scale , RoundingMode.HALF_UP );
			}
			return new Numerical( getEnvironment() , newValue );
		} else {
			return super.divide( dividend );
		}
	}
	
	/**
	 * divides and truncates
	 * 
	 * @param dividend				number by which to divide that is not zero
	 * @return						the quotient truncated, e.g. 5/4 yields 1 and 3/4 yields 0
	 */
	public Numerical truncationDivide( Numerical dividend ) {
		BigDecimal truncatedQuotient = this.m_value.divideAndRemainder( dividend.getNumericalValue() )[ 0 ];
		return new Numerical( getEnvironment() , truncatedQuotient );
	}
	
	@Override
	public Combinable exponentiate( Combinable exponent ) {
		
		//only deal with numerical exponentiation
		if ( exponent instanceof Numerical ) {
			Numerical numericalExponent = ( Numerical ) exponent;
			double thisDoubleValue = this.m_value.doubleValue();
			double exponentDoubleValue = numericalExponent.getNumericalValue().doubleValue();
			double exponentiateResult = Math.pow( thisDoubleValue , exponentDoubleValue );
			return new Numerical( getEnvironment() , new BigDecimal( exponentiateResult ) );
		} else {
			return super.exponentiate( exponent );
		}
	}
	
	/**
	 * @param representation			textual representation of a token to be created
	 * @return							if the textual representation of the token is that of a number
	 */
	@SuppressWarnings("unused")
	final public static boolean isNumerical( String representation ) {
		
		//check if the representation of the token can be converted to a decimal
		try {
			new BigDecimal( representation );
			return true;
		} catch ( NumberFormatException e ) {
			return false;
		}
	}
	
	/**
	 * @return				if this <code>Numerical</code> is an integer
	 */
	public boolean isInteger() {
		return this.m_value.intValue() == this.m_value.doubleValue();
	}
	
	@Override
	public boolean equals( Object toCompare ) {
		if ( toCompare == null ) {
			return this.getValue().equals( BigDecimal.ZERO );
		}
		if ( toCompare instanceof Numerical ) {
			return ( ( Numerical ) toCompare ).getNumericalValue().equals( this.m_value );
		} else {
			return super.equals( toCompare );
		}
	}
	
	@Override
	public int compareTo( Combinable toCompare ) {
		if ( toCompare instanceof Numerical ) {
			return this.m_value.compareTo( ( ( Numerical ) toCompare).getNumericalValue() );
		} else {
			return super.compareTo( toCompare );
		}
	}
	
	@Override
	public Numerical copy( int indexInInput ) {
		Numerical rtn = new Numerical( getEnvironment() , this.m_value );
		rtn.setIndexInInput( indexInInput );
		return rtn;
	}
	
	@Override
	public Numerical clone() {
		return copy( getIndexInInput() );
	}
	
	@Override
	public String toLatexString() {
		return getRepresentation();
	}
}
