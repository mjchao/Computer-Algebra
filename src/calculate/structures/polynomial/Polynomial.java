package calculate.structures.polynomial;

import _library.LinkedList;
import calculate.Calculator;
import calculate.misc.CloseParenthesis;
import calculate.misc.OpenParenthesis;
import calculate.operators.MultiplyOperator;
import calculate.operators.SubtractOperator;
import calculate.structures.Combinable;

/**
 * A <code>Polynomial</code> contains a series of <code>PolynomialTerm</code> objects
 * multiplied together. It also has a coefficient.
 */
public class Polynomial extends Combinable {
	
	/**
	 * the terms in this <code>Polynomial</code> that are multiplied together
	 */
	private LinkedList< PolynomialTerm > m_terms = new LinkedList< PolynomialTerm > ();
	
	/**
	 * the numerical coefficient of this <code>Polynomial</code>
	 */
	private Numerical m_coefficient;
	
	/**
	 * a default <code>Polynomial</code> constructor for a <code>PolynomialTerm</code>
	 * @param environment
	 */
	public Polynomial( Calculator environment ) {
		super( environment );
		this.m_terms.add( ( PolynomialTerm ) this );
		
	}
	
	/**
	 * <code>Polynomial</code> constructor for a <code>Monomial</code>
	 * 
	 * @param environment
	 * @param representation
	 */
	public Polynomial( Calculator environment , String representation ) {
		super( representation , environment );
		this.m_terms.add( ( PolynomialTerm ) this );
	}
	
	public Polynomial( Calculator environment , LinkedList < PolynomialTerm > multipliedTerms , Numerical coefficient) {
		super( environment );
		this.m_terms = multipliedTerms;
		this.m_coefficient = coefficient;
	}
	
	public Polynomial( Calculator environment , PolynomialTerm term1 , PolynomialTerm term2 ) {
		super( environment );
		if ( term1.getNumerator().equals( term2.getNumerator() ) && term1.getDenominator().equals( term2.getDenominator() ) ) {
			PolynomialTerm termToAdd = term1.clone();
			termToAdd.setPolynomialTermExponent( termToAdd.getPolynomialTermExponent().add( term2.getPolynomialTermExponent() ) );
			insertMultipliedTerm( this.m_terms , termToAdd );
			this.m_coefficient = Numerical.ONE;
		} else {
			insertMultipliedTerm( this.m_terms , term1 );
			insertMultipliedTerm( this.m_terms , term2 );
			this.m_coefficient = Numerical.ONE;
		}
	}
	
	private void determineRepresentation() {
		//TODO
	}
	
	private static void insertMultipliedTerm( LinkedList < PolynomialTerm > terms , PolynomialTerm termToInsert ) {
		terms.add( termToInsert );
	}
	
	public LinkedList< PolynomialTerm > getPolynomialMultipliedTerms() {
		return this.m_terms;
	}
	
	public Numerical getCoefficient() {
		return this.m_coefficient.clone();
	}
	
	public void setCoefficient( Numerical newCoefficient ) {
		this.m_coefficient = newCoefficient;
	}
	
	/**
	 * @return			the degree of the <code>Polynomial</code>. For example,
	 * 					the degree of 5x^2+4x-10 is 2.
	 */
	public Combinable getDegree() {
		Combinable rtn = Numerical.ZERO;
		for ( PolynomialTerm aTerm : this.m_terms ) {
			rtn = rtn.add( aTerm.getDegree() );
		}
		return rtn;
	}
	
	@Override
	public Combinable multiply( Combinable multiplicand ) {
		
		if ( multiplicand instanceof Polynomial ) {
			
			Polynomial polynomialMultiplicand = ( Polynomial ) multiplicand;

			LinkedList < PolynomialTerm > thisMultipliedTerms = this.getPolynomialMultipliedTerms();
			LinkedList < PolynomialTerm > multiplicandMultipliedTerms = polynomialMultiplicand.getPolynomialMultipliedTerms();
			LinkedList < PolynomialTerm > resultingMultipliedTerms = new LinkedList < PolynomialTerm > ();
			
			//go through all the terms in the multiplicand
			for ( PolynomialTerm multiplicandTerm : multiplicandMultipliedTerms ) {
				boolean multiplied = false;
				
				//go through all the terms in this Polynomial
				for ( PolynomialTerm thisTerm : thisMultipliedTerms ) {
					
					//if a term in this Polynomial and a term in the multiplicand
					//have the same numerator and denominator
					//we can just multiply them together by adding exponents
					if ( multiplicandTerm.getNumerator().equals( thisTerm.getNumerator() ) &&
							multiplicandTerm.getDenominator().equals( thisTerm.getDenominator() ) ) {
						
						//modify the exponent
						PolynomialTerm product = thisTerm.clone();
						product.setPolynomialTermExponent( product.getPolynomialTermExponent().add( multiplicandTerm.getPolynomialTermExponent() ) );
						
						//add the term to the list of multiplied terms
						insertMultipliedTerm( resultingMultipliedTerms , product );
						
						//make sure no terms are multipled more than once
						thisMultipliedTerms.remove();
						multiplied = true;
						break;
					}
				}
				
				//if the term was not multiplied, then just add it to the list of multiplied terms
				if ( !multiplied ) {
					insertMultipliedTerm( resultingMultipliedTerms , multiplicandTerm );
				}
			}
			
			//add any of the terms in this Polynomial that were not multiplied
			//to the multiplied terms list
			for ( PolynomialTerm thisTerm : thisMultipliedTerms ) {
				insertMultipliedTerm( resultingMultipliedTerms , thisTerm );
			}
			
			//return the result
			return new Polynomial( getEnvironment() , resultingMultipliedTerms , this.getCoefficient() );
		} else {
			return super.multiply( multiplicand );
		}
	}

	public Polynomial multiplyByNegativeOne() {
		Polynomial rtn = this.clone();
		rtn.setCoefficient( ( Numerical ) this.getCoefficient().multiplyByNegativeOne() );
		return rtn;
	}
	
	@Override
	public Combinable divide( Combinable dividend ) {
		throw new RuntimeException();
	}
	
	@Override
	public Combinable exponentiate( Combinable exponent ) {
		
		if ( exponent instanceof Polynomial ) {
			Polynomial polynomialExponent = ( Polynomial ) exponent;
			LinkedList < PolynomialTerm > thisTerms = this.m_terms.clone();
			LinkedList < PolynomialTerm > resultingMultipliedTerms = this.m_terms.clone();
			
			//exponentiate the terms
			for ( PolynomialTerm aTerm : thisTerms ) {
				aTerm.setPolynomialTermExponent( aTerm.getPolynomialTermExponent().multiply( polynomialExponent ) );
				thisTerms.replace( aTerm );
			}
		
			//exponentiate the coefficient
			Polynomial newCoefficient;
			if ( this.m_coefficient == null ) {
				newCoefficient = Numerical.ONE;
			} else {
				newCoefficient = ( Polynomial ) this.m_coefficient.exponentiate( polynomialExponent );
			}
			
			if ( newCoefficient instanceof Numerical ) {
				return new Polynomial( getEnvironment() , resultingMultipliedTerms , ( Numerical ) newCoefficient );
			} else if ( newCoefficient instanceof PolynomialTerm ) {
				insertMultipliedTerm( resultingMultipliedTerms , ( PolynomialTerm ) newCoefficient );
				return new Polynomial( getEnvironment() , resultingMultipliedTerms , Numerical.ONE );
			} else {
				System.out.println( "correct");
				LinkedList < PolynomialTerm > coefficientTerms = newCoefficient.getPolynomialMultipliedTerms();
				for ( PolynomialTerm aTerm : coefficientTerms ) {
					insertMultipliedTerm( resultingMultipliedTerms , aTerm );
				}
				return new Polynomial( getEnvironment() , resultingMultipliedTerms , Numerical.ONE );
			}
		} else {
			return super.exponentiate( exponent );
		}
	}
	
	@Override
	public boolean equals( Object toCompare ) {
		
		if ( toCompare instanceof Polynomial ) {
			
			Polynomial polynomialToCompare = ( Polynomial ) toCompare;
			//if the terms are the same, then the polynomials are equal
			return this.m_terms.equals( polynomialToCompare.getPolynomialMultipliedTerms() );
		} else {
			return super.equals( toCompare );
		}
	}
	
	@Override
	public int compareTo( Combinable toCompare ) {
		
		//only deal with polynomial comparisons
		if ( toCompare instanceof Polynomial ) {
			
			//TODO
			return 1;
			
		//if the given Combinable to compare is not a polynomial, don't deal with it
		} else {
			return super.compareTo( toCompare );
		}
	}
	
	@Override
	public int countNumTerms() {
		return this.m_terms.size();
	}
	
	@Override
	public Polynomial clone() {
		return new Polynomial( getEnvironment() , this.getPolynomialMultipliedTerms() , this.getCoefficient() );
	}
	
	@Override
	public String toLatexString() {
		System.out.println("poylnomial latex");
		String rtn = "";
		if ( this.m_coefficient.equals( Numerical.ONE ) ) {
			
			//don't display the coefficient if it is 1
			
		} else if ( this.m_coefficient.equals( Numerical.NEGATIVE_ONE ) ) {
			
			//just add a negative sign if the coefficient is -1
			rtn += SubtractOperator.LATEX_REPRESENTATION;
			
		} else if ( this.m_coefficient.equals( Numerical.ZERO ) ) {
			
			//if the coefficient is zero, return 0
			return "0";
			
		} else {
			
			//add the coefficient if it is not -1, 0 or 1
			rtn += this.m_coefficient.toLatexString();
		}
		
		int numNonOneTerms = 0;
		for ( PolynomialTerm aTerm : this.m_terms ) {
			if ( !aTerm.equals( Numerical.ONE ) ) {
				numNonOneTerms++;
			}
		}
		
		//add each term to the representation
		if ( numNonOneTerms == 1 ) {
			this.m_terms.moveToStart();
			rtn += this.m_terms.get().toLatexString();
		} else {
			for ( PolynomialTerm aTerm : this.m_terms ) {
				boolean addedATerm = false;
				
				//if a term is zero, then the whole Polynomial is zero
				if ( aTerm.equals( Numerical.ZERO ) ) {
					return "0";
				}
				
				if ( !aTerm.equals( Numerical.ONE ) ) {
					if ( addedATerm ) {
						rtn += MultiplyOperator.LATEX_REPRESENTATION + OpenParenthesis.LATEX_REPRESENTATION + 
								aTerm.toLatexString() + CloseParenthesis.LATEX_REPRESENTATION;
					} else {
						rtn += OpenParenthesis.LATEX_REPRESENTATION + aTerm.toLatexString() + CloseParenthesis.LATEX_REPRESENTATION;
						addedATerm = true;
					}
				}
				
			}
		}
		
		return rtn;
	}
}
