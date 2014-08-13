package calculate.structures.polynomial;

import calculate.Calculator;
import calculate.misc.CloseParenthesis;
import calculate.misc.OpenParenthesis;
import calculate.operators.ExponentiateOperator;
import calculate.structures.Combinable;

/**
 * a term in a <code>Monomial</code>. the terms in a <code>Monomial</code> are
 * multiplied together. each term has a base and an exponent.
 * <p>
 * for example, 5x^2y^3 could be a <code>Monomial</code> and its <code>MonomialTerms</code>
 * are x^2 and y^3
 * 
 */
public class MonomialTerm extends Monomial {

	
	private Variable m_base;
	private Combinable m_exponent;
	
	/**
     * <code>MonomialTerm</code> constructor for a <code>Numerical</code>
     * 
	 * @param environment
	 * @param representation
	 */
	public MonomialTerm( Calculator environment ) {
		super( environment );
		this.m_base = null;
		this.m_exponent = null;
	}
	
	/**
	 * <code>MonomialTerm</code> constructor for a <code>Variable</code> or
	 * <code>Constant</code>
	 * 
	 * @param environment
	 * @param representation
	 * @param exponent
	 */
	public MonomialTerm( Calculator environment , Combinable exponent ) {
		super( environment , Numerical.ONE );
		this.m_base = ( Variable ) this;
		this.m_exponent = exponent;
		determineRepresentation();
	}
	
	/**
	 * <code>MonomialTerm</code> constructor for a <code>MonomialTerm</code>
	 * 
	 * @param environment
	 * @param base						base term of the <code>MonomialTerm</code>
	 * @param exponent					the exponent of the <code>MonomialTerm</code>
	 */
	public MonomialTerm( Calculator environment , Variable base , Combinable exponent ) {
		super( environment , Numerical.ONE );
		this.m_base = base;
		this.m_exponent = exponent;
		determineRepresentation();
	}
	
	/**
	 * @return 			the base, or <code>Variable</code> part of 
	 * 					this <code>MonomialTerm</code>
	 */
	public Variable getBase() {
		if ( this.m_base == null ) {
			return null;
		}
		return this.m_base.clone();
	}
	
	/**
	 * sets the base to a new base
	 * 
	 * @param newBase	the new base for this <code>MonomialTerm</code>
	 */
	public void setBase( Variable newBase ) {
		this.m_base = newBase;
	}
	
	/**
	 * @return			the exponent part of this <code>MonomialTerm</code>
	 */
	public Combinable getMonomialTermExponent() {
		return this.m_exponent.clone();
	}
	
	/**
	 * sets the exponent to a new value
	 * 
	 * @param newExponent		the new exponent for this <code>MonomialTerm</code>
	 */
	public void setExponent( Combinable newExponent ) {
		this.m_exponent = newExponent;
	}
	
	/**
	 * adds the given amount to the exponent. this occurs during multiplication
	 * of two <code>Monomial</code> objects with the same base
	 * 
	 * @param augend			value to add to the exponent
	 * @return					a new <code>MonomialTerm</code> with the updated exponent
	 */
	public MonomialTerm addToExponent( Combinable augend ) {
		Combinable newExponent = this.m_exponent.add( augend );
		MonomialTerm rtn = new MonomialTerm( getEnvironment() , getBase() , newExponent );
		return rtn;
	}
	
	/**
	 * subtracts the given amount from the exponent. this occurs during division
	 * of two <code>Monomial</code> objects with the same base
	 * 
	 * @param subtrahend		value to subtract from exponent
	 * @return					a new <code>MonomialTerm</code> with the updated exponent
	 */
	public MonomialTerm subtractFromExponent( Combinable subtrahend ) {
		Combinable newExponent = this.m_exponent.subtract( subtrahend );
		MonomialTerm rtn = new MonomialTerm( getEnvironment() , getBase() , newExponent );
		return rtn;
	}
	
	/**
	 * multiplies the exponent by the given amount. this occurs during exponentiation
	 * of a <code>Monomial</code>
	 * 
	 * @param multiplicand		value by which to multiply the exponent
	 * @return					a new <code>MonomialTerm</code> with the updated exponent
	 */
	public MonomialTerm multiplyExponentBy( Combinable multiplicand ) {
		Combinable newExponent = this.m_exponent.multiply( multiplicand );
		MonomialTerm rtn = new MonomialTerm( getEnvironment() , getBase() , newExponent );
		return rtn;
	}
	
	/**
	 * determines the representation of this <code>MonomialTerm</code> and
	 * updates it
	 */
	private void determineRepresentation() {
		String representation = "";
		
		//if the exponent is zero, then there is no need to add this term at all
		if ( this.m_exponent.equals( Numerical.ZERO ) ) {
			
			//continue
			
		//if the exponent is one, then just add the base
		} else if ( this.m_exponent.equals( Numerical.ONE ) ) {
			representation += this.m_base.getRepresentation();
			
		//if the exponent is not zero or one, then add the base and the exponent
		} else {
			
			representation += this.m_base.getRepresentation();
			//if the exponent has only one term, then no parentheses are required
			if ( this.m_exponent.countNumTerms() == 1 ) {
				representation += ExponentiateOperator.REPRESENTATION + this.m_exponent;
				
			//if the exponent has more than 1 term, then parentheses are needed
			} else {
				representation += ExponentiateOperator.REPRESENTATION + OpenParenthesis.REPRESENTATION +
						this.m_exponent.getRepresentation() + CloseParenthesis.REPRESENTATION;
			}
		}
		setRepresentation( representation );
	}
	
	@Override
	public boolean equals( Object obj ) {
		
		//only deal with comparing MonomialTerms with MonomialTerms
		if ( obj instanceof MonomialTerm ) {
			
			MonomialTerm termToCompare = ( MonomialTerm ) obj;
			//make sure the bases are equal
			if ( this.getBase() == null ^ termToCompare.getBase() == null ) {
				return false;
			}
			if ( this.getBase().equals( termToCompare.getBase() ) ) {
				
				//make sure the exponents are equal
				if ( this.getMonomialTermExponent().equals( termToCompare.getMonomialTermExponent() ) ) {
					
					//if all conditions are satisfied, then this term equals
					//the term to compare
					return true;
				}
			}
			return false;
		} else {
			return super.equals( obj );
		}
	}
	
	/**
	 * determines which <code>Monomial</code> term should be displayed first
	 * in a <code>Monomial</code>. For example, in the <code>Monomial</code>
	 * 5y^2x, x should come before y, so the <code>Monomial</code> should be rewritten
	 * as 5xy^2
	 * 
	 * @param toCompare					any <code>Combinable</code>, except this method
	 * 									will not execute unless the given object to compare is
	 * 									a <code>MonomialTerm</code>
	 * @return							which term, this or the term to compare, should
	 * 									be displayed first
	 */
	@Override
	public int compareTo( Combinable toCompare ) {
		
		//only deal with comparing with other MonomialTerm objects
		if ( toCompare instanceof MonomialTerm ) {
			MonomialTerm termToCompare = ( MonomialTerm ) toCompare;
			
			//compare their textual representations
			return this.getBase().compareTo( termToCompare.getBase() );
		} else {
			return super.compareTo( toCompare );
		}
	}
	
	@Override
	public MonomialTerm clone() {
		return new MonomialTerm( getEnvironment() , getBase() , getMonomialTermExponent() );
	}
	
	@Override
	public String toLatexString() {
		String rtn = getBase().toLatexString();
		if ( !getMonomialTermExponent().equals( Numerical.ONE ) ) {
			rtn += "^{" + getMonomialTermExponent().toLatexString() + "}";
		}
		return rtn;
	}
}
