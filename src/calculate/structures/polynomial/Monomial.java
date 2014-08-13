package calculate.structures.polynomial;

import _library.LinkedList;
import calculate.Calculator;
import calculate.operators.MultiplyOperator;
import calculate.operators.SubtractOperator;
import calculate.structures.Combinable;

public class Monomial extends PolynomialTerm {

	/**
	 * the terms that are multiplied together to form this <code>Monomial</code>
	 */
	private LinkedList < MonomialTerm > m_terms = new LinkedList < MonomialTerm > ();
	
	/**
	 * the numerical coefficient of this <code>Monomial</code>
	 */
	private Numerical m_coefficient;
	
	/**
	 * <code>Monomial</code> constructor for a <code>Numerical</code>
	 * 
	 * @param environment
	 * @param representation
	 */
	public Monomial( Calculator environment ) {
		super( environment );
		this.m_coefficient = ( Numerical ) this;
	}
	
	/**
	 * <code>Monomial</code> constructor for a <code>MonomialTerm</code> or 
	 * <code>Variable</code>
	 * 
	 * @param environment
	 * @param representation
	 * @param coefficient
	 */
	public Monomial( Calculator environment , Numerical coefficient ) {
		super( environment );
		this.m_terms.add( ( MonomialTerm ) this );
		this.m_coefficient = coefficient;
	}
	
	/**
	 * <code>Monomial</code> constructor for a <code>Monomial</code>
	 * @param environment
	 * @param terms
	 * @param coefficient
	 */
	public Monomial( Calculator environment , LinkedList < MonomialTerm > terms , Numerical coefficient ) {
		super( environment );
		this.m_terms = terms;
		
		//remove any terms that are equal to one
		terms.moveToStart();
		while ( terms.hasCurrent() ) {
			
			//check for zero exponents
			if ( terms.get().getMonomialTermExponent().equals( Numerical.ZERO ) ) {
				terms.remove();
			
			//if nothing was removed, advance
			} else {
				if ( terms.hasNext() ) {
					terms.advance();
				} else {
					break;
				}
			}
		}
		this.m_coefficient = coefficient;
		determineRepresentation();
	}
	
	/**
	 * @return			a deep copy of the terms in this variable that are 
	 * 					multiplied together to obtain an overall <code>Monomial</code>
	 */
	public LinkedList < MonomialTerm > getMultipliedTerms() {
		return this.m_terms.clone();
	}
	
	/**
	 * @return			the coefficient of this <code>Monomial</code>
	 */
	public Numerical getCoefficient() {
		return this.m_coefficient;
	}
	
	/**
	 * sets the coefficient of this <code>Monomial</code>
	 * 
	 * @param newCoefficient			the new coefficient for this <code>Monomial</code>
	 */
	public void setCoefficient( Numerical newCoefficient ) {
		this.m_coefficient = newCoefficient;
		
		//update the representation
		determineRepresentation();
	}
	
	/**
	 * determines the representation of this monomial and updates it
	 */
	private void determineRepresentation() {
		
		//start building the representation from scratch
		String representation = "";
		
		//apply the coefficient
		
		//if the coefficient is zero, then this whole Monomial evaluates to 0
		if ( this.m_coefficient.equals( Numerical.ZERO ) ) {
			setRepresentation("0");
			return;
			
		//if coefficient is one, there is no need to display it
		} else if ( this.m_coefficient.equals( Numerical.ONE ) ) {
			
			//continue
			
		//if the coefficient is negative one, just a negative sign is needed
		} else if ( this.m_coefficient.equals( Numerical.NEGATIVE_ONE ) ) {
			representation += SubtractOperator.REPRESENTATION;
		//if the coefficient is not one, then add it to the beginning of the representation
		} else {
			representation += this.m_coefficient.getRepresentation();
		}
		
		//apply the terms in this Monomial
		
		//go through each term in the variable part of the monomial and each
		//term in the exponent part
		for ( MonomialTerm term : this.m_terms ) {
			representation += term.getRepresentation();
		}
		
		//if everything was one, then this Monomial's representation should be 1
		if ( representation.equals( "" ) ) {
			representation = "1";
		}
		//update the representation
		setRepresentation( representation );
	}
	
	/**
	 * @return		the representation of the base of this <code>Monomial</code>
	 */
	public int compareBaseRepresentation( Monomial toCompare ) {
		LinkedList < MonomialTerm > thisTerms = this.getMultipliedTerms();
		LinkedList < MonomialTerm > toCompareTerms = toCompare.getMultipliedTerms();
		
		thisTerms.moveToStart();
		toCompareTerms.moveToStart();
		
		//compare term by term
		while ( thisTerms.hasCurrent() && toCompareTerms.hasCurrent() ) {
			MonomialTerm thisTerm = thisTerms.get();
			MonomialTerm toCompareTerm = toCompareTerms.get();

			//first compare alphabetically by base
			int compareBase = thisTerm.getBase().compareTo( toCompareTerm.getBase() );
			if ( compareBase == 0 ) {
				
				//if bases are equal, continue
				
			} else {
				return compareBase;
			}
			
			//keep moving through list if there are more terms to compare
			if ( thisTerms.hasNext() && toCompareTerms.hasNext() ) {
				thisTerms.advance();
				toCompareTerms.advance();
			} else {
				
				//if run out of terms, whichever has more terms is bigger
				return new Integer( thisTerms.size() ).compareTo( new Integer( toCompareTerms.size() ) );
			}
		}
		return 0;
	}
	
	/**
	 * determines if one <code>Monomial</code> can be added to another <code>Monomial</code>
	 * without creating a <code>Polynomial</code>. for example, 5x and 4x are addable
	 * but 5x and 4pi are not.
	 * 
	 * @param augend			value to be added
	 * @return					if this <code>Monomial</code> can be added with the augend
	 * 							to obtain a <code>Monomial</code>
	 */
	public boolean isAddableWith( Monomial augend ) {
		
		//make sure the bases (the multiplied terms) are the same
		if ( this.getMultipliedTerms().equals( augend.getMultipliedTerms() ) ) {
			
			//continue
			
		//if the base of one Monomial is different from the base of the other,
		//then we cannot add them
		} else {
			return false;
		}
		
		//if all conditions satisfied, then this Monomial can be added to the
		//augend
		return true;
	}
	
	/**
	 * adds two <code>Monomial</code> objects together
	 * 
	 * @param augend 				any <code>Combinable</code> but this method
	 * 								will not deal with anything that is not a 
	 * 								<code>Monomial</code>
	 * @return						the sum of this <code>Monomial</code> and the augend
	 */
	@Override
	public Combinable add( Combinable augend ) {
		
		//only deal with Monomial addition
		if ( augend instanceof Monomial ) {
			Monomial monomialAugend = ( Monomial ) augend;
			
			//if the this monomial and the augend monomial can be added
			//together to product a monomial sum (i.e. they have the same bases
			//and exponents, just possibly different coefficients), then
			if ( this.isAddableWith( monomialAugend ) ) {
				
				//just combine the coefficients
				Numerical newCoefficient = ( Numerical ) this.m_coefficient.add( monomialAugend.getCoefficient() );
				return new Monomial( getEnvironment() , getMultipliedTerms() , newCoefficient );
			} else {
				
				//create a polynomial term
				return new PolynomialTerm( getEnvironment() , this , monomialAugend );
			}
		//if the augend is not a Monomial, don't deal with it
		} else {
			return super.add( augend );
		}
	}
	
	/**
	 * determines if one <code>Monomial</code> can be subtracted from another without
	 * creating a new <code>Polynomial</code>.
	 * 
	 * @param subtrahend			the <code>Monomial to be subtracted from this <code>Monomial</code>
	 * @return						if the subtrahend may be subtracted from this <code>Monomial</code>
	 * 								without creating a new <code>Polynomial</code>.
	 * @see							#isAddableWith(Monomial)
	 */
	public boolean isSubtractableWith( Monomial subtrahend ) {
		
		//if two Monomials can be added together without forming a
		//new Polynomial, then they can be subtracted from each other
		//without forming a new Polynomial
		return isAddableWith( subtrahend );
	}
	
	/**
	 * subtracts one <code>Monomial</code> from another
	 * 
	 * @param subtrahend 			any <code>Combinable</code> but this method
	 * 								will not deal with anything that is not a 
	 * 								<code>Monomial</code>
	 * @return						the difference of this <code>Monomial</code> and the subtrahend
	 */
	@Override
	public Combinable subtract( Combinable subtrahend ) {
		
		//only deal with Monomial subtraction
		if ( subtrahend instanceof Monomial ) {
			Monomial monomialSubtrahend = ( Monomial ) subtrahend;
			
			//check if we need to express the result as a polynomial or
			//if we can just express it as a new monomial
			
			//if we do not need to create a new polynomial, i.e. this Monomial
			//and the subtrahend Monomial have the same bases and exponents
			if ( this.isSubtractableWith( monomialSubtrahend ) ) {
				
				//just subtract the coefficients
				Numerical newCoefficient = ( Numerical ) this.m_coefficient.subtract( ((Monomial) subtrahend).getCoefficient() );
				return new Monomial( getEnvironment() , getMultipliedTerms() , newCoefficient );
			} else {
				
				// create a polynomial term
				return new PolynomialTerm( getEnvironment() , this , monomialSubtrahend.multiplyByNegativeOne() );
			}
		//if the subtrahend is not a Monomial, don't deal with it
		} else {
			return super.subtract( subtrahend );
		}
	}
	
	
	/**
	 * multiplies two <code>Monomial</code> objects together
	 * 
	 * @param multiplicand					the <code>Combinable</code> by which to multiply this <code>Monomial</code>,
	 * 										but this method will not execute if the multiplicand is not a <code>Monomial</code>
	 * @return 								the product of multiplying this by the multiplicand
	 */
	@Override
	public Combinable multiply( Combinable multiplicand ) {
		
		//only deal with Monomial multiplication
		if ( multiplicand instanceof Monomial ) {
			Monomial monomialMultiplicand = ( Monomial ) multiplicand;
			
			
			LinkedList < MonomialTerm > thisTerms = this.getMultipliedTerms();
			//initially, start with empty result
			LinkedList < MonomialTerm > resultTerms = new LinkedList < MonomialTerm > ();
			
			//go through each term in the multiplicand
			LinkedList < MonomialTerm > multiplicandTerms = monomialMultiplicand.getMultipliedTerms();
			for ( MonomialTerm multiplicandMultiplier : multiplicandTerms ) {
				
				//see if any term in this Monomial has the same base
				boolean multiplied = false;
				for ( MonomialTerm thisMultiplier : thisTerms ) {
					
					//if there exists a term in this Monomial with the same
					//base as the multiplier
					if ( thisMultiplier.getBase().equals( multiplicandMultiplier.getBase() ) ) {
						
						//just add their exponents and update the result
						MonomialTerm newTerm = thisMultiplier.addToExponent( multiplicandMultiplier.getMonomialTermExponent() );
						insertMultipliedTerm( resultTerms , newTerm );
						multiplied = true;
						thisTerms.remove();
						break;
					}
				}
				
				//if there does not exist a term in this Monomial with the same base
				//as the multiplier, then add the term to the list of multiplied terms
				if ( multiplied == false ) {
					insertMultipliedTerm( resultTerms , multiplicandMultiplier );
				}
			}
			
			//finally, insert any of the original terms in this Monomial that
			//were not used in multiplying with the multiplicand terms
			for ( MonomialTerm aTerm : thisTerms ) {
				insertMultipliedTerm( resultTerms , aTerm );
			}
			
			//multiply the coefficients
			Numerical newCoefficient = ( Numerical ) this.m_coefficient.multiply( monomialMultiplicand.getCoefficient() );
			
			//return the result
			return new Monomial( getEnvironment() , resultTerms , newCoefficient );
		} else {
			return super.multiply( multiplicand );
		}
	}
	
	@Override
	public Monomial multiplyByNegativeOne() {
		return ( Monomial ) this.multiply( Numerical.NEGATIVE_ONE );
	}
	
	/**
	 * inserts a given term into a list of terms at the proper location.
	 * 
	 * @param terms						a list of terms
	 * @param termToInsert				the term to insert
	 */
	private static void insertMultipliedTerm( LinkedList < MonomialTerm > terms , MonomialTerm termToInsert ) {
		
		boolean inserted = false;
		//go through the list of terms
		terms.moveToStart();
		
		//first check if the term to insert should be inserted at the very front
		if ( terms.hasCurrent() ) {
			if ( termToInsert.compareTo( terms.get() ) < 0 ) {
				terms.insertHead( termToInsert );
				inserted = true;
			}
		}
		
		if ( inserted == false ) {
			
			//then go through the rest of the list
			while ( terms.hasCurrent() && inserted == false ) {
	
				//if there is a term after the current one,
				//see if the term to insert should be placed between
				//the current term and the next term
				if ( terms.hasNext() ) {
					MonomialTerm nextTerm = terms.peek();
					
					//if the term to insert should come before the next term then
					if ( termToInsert.compareTo( nextTerm ) < 0 ) {
						
						//insert the term
						terms.insert( termToInsert );
						inserted = true;
					}
					
					//move forward in the terms list
					terms.advance();
				} else {
					break;
				}
			}
		}
		
		//if after going through the whole list,
		//the term has not been inserted, then
		if ( inserted == false ) {
		
			//add the term to the end of the list
			terms.add( termToInsert );
			inserted = true;
		}
	}
	
	/**
	 * divides this <code>Monomial</code> by the given dividend
	 * 
	 * @param dividend				any <code>Combinable</code>, except this method
	 * 								will execute only if it is given a <code>Monomial</code>
	 * 								dividend
	 * @return 						the quotient of the division
	 */
	@Override
	public Combinable divide( Combinable dividend ) {
		
		//only deal with Monomial division
		if ( dividend instanceof Monomial ) {
			Monomial monomialDividend = ( Monomial ) dividend;
			
			LinkedList < MonomialTerm > termsInDividend = monomialDividend.getMultipliedTerms();
			
			//replace every term in the dividend by its reciprocol
			for ( MonomialTerm dividendTerm : termsInDividend ) {
				MonomialTerm reciprocolTerm = dividendTerm.multiplyExponentBy( Numerical.NEGATIVE_ONE );
				termsInDividend.replace( reciprocolTerm );
			}
		
			//determine the quotient's coefficient
			Numerical quotientCoefficient = ( Numerical ) this.getCoefficient().divide( monomialDividend.getCoefficient() );
			
			//then multiply just the terms
			Monomial multiplicand = new Monomial( dividend.getEnvironment() , termsInDividend , Numerical.ONE );
			Monomial rtn = ( Monomial ) this.multiply( multiplicand );
			
			//finally update the quotient's coefficient
			rtn.setCoefficient( quotientCoefficient );
			
			//return the result
			return rtn;
			
		//if the dividend is not a Monomial, don't deal with it
		} else {
			return super.divide( dividend );
		}
	}
	
	/**
	 * exponentiates this <code>Monomial</code>
	 * 
	 * @param exponent				any <code>Combinable</code>, except
	 * 								this method will only execute if the exponent is
	 * 								a <code>Monomial</code>
	 * @return						the result of exponentiating this <code>Monomial</code> by the exponent
	 */
	@Override
	public Combinable exponentiate( Combinable exponent ) {
		
		//only deal with Monomial exponentiation
		if ( exponent instanceof Monomial ) {
			Monomial monomialExponent = ( Monomial ) exponent;
			
			//go through every term and exponentiate it ( multiply its exponent by the exponent given )
			LinkedList < MonomialTerm > resultTerms = new LinkedList < MonomialTerm > ();
			for ( MonomialTerm aTerm : this.m_terms ) {
				MonomialTerm exponentiatedTerm = aTerm.multiplyExponentBy( monomialExponent );
				insertMultipliedTerm( resultTerms , exponentiatedTerm );
			}
			
			//exponentiate the coefficient
			
			//if the exponent is a number, we can just modify the coefficient
			if ( exponent instanceof Numerical ) {
				Numerical numericalExponent = ( Numerical ) exponent;
				Numerical newCoefficient = ( Numerical ) this.m_coefficient.exponentiate( numericalExponent );
				return new Monomial( getEnvironment() , resultTerms , newCoefficient );
				
			//if the exponent is not a number, we have to add it to the list of
			//multiplied terms
			} else {
				
				//if the current coefficient is not 1, then we need to exponentiate it
				//because if the coefficient is 1, 1 to the any power is 1
				if ( !this.m_coefficient.equals( Numerical.ONE ) ) {
					MonomialTerm additionalTerm = new MonomialTerm( getEnvironment() , this.getCoefficient() , exponent );
					insertMultipliedTerm( resultTerms , additionalTerm );
				}
				//since the current coefficient was exponentiated, the new coefficient
				//should be 1
				Numerical newCoefficient = Numerical.ONE;
				
				//return the result
				return new Monomial( getEnvironment() , resultTerms , newCoefficient );
			}
			
		//if the exponent is not a Monomial, don't deal with it
		} else {
			return super.exponentiate( exponent );
		}
	}
	
	@Override
	public Combinable getDegree() {

		//the degree of a Monomial is the sum of all the exponents of
		//its terms
		Combinable degree = Numerical.ZERO;
		for ( MonomialTerm aTerm : this.m_terms ) {

			if ( !( aTerm.getBase() instanceof Constant ) ) {
				degree = degree.add( aTerm.getMonomialTermExponent() );
			}
		}
		return degree;
	}
	
	@Override
	public boolean equals( Object toCompare ) {
		
		//only deal with checking if a Monomial equals another Monomial
		if ( toCompare instanceof Monomial ) {
			Monomial monomialToCompare = ( Monomial ) toCompare;
			
			//special cases for comparison with zero
			if ( monomialToCompare instanceof Numerical ) {
				
				//check if the value with which to compare is zero 
				Numerical numericalToCompare = ( Numerical ) monomialToCompare;
				if ( numericalToCompare.equals( Numerical.ZERO ) ) {
					for ( MonomialTerm aTerm : this.m_terms ) {
						if ( aTerm.equals( Numerical.ZERO ) ) {
							return true;
						}
					}
					return this.m_coefficient.equals( Numerical.ZERO );
				}
			}
			
			if ( this.equals( Numerical.ZERO ) ) {
				return toCompare.equals( Numerical.ZERO );
			}
			
			//if this is not comparison with zero then
			return 
					/* check that their variables terms are the same */
					this.getMultipliedTerms().equals( monomialToCompare.getMultipliedTerms() ) &&
					
					/* check that their coefficients are the same */
					this.getCoefficient().equals( monomialToCompare.getCoefficient() );
		
		//if we are not given a Monomial, then do not deal with the comparison
		} else {
			return super.equals( toCompare );
		}
	}
	
	@Override
	public int compareTo( Combinable toCompare ) {
		
		//only deal with Monomial comparisons
		if ( toCompare instanceof Monomial ) {
			
			Monomial monomialToCompare = ( Monomial ) toCompare;

			//first compare degree
			Combinable thisDegree = this.getDegree();
			if ( this instanceof Constant ) {
				thisDegree = Numerical.ZERO;
			}
			
			Combinable toCompareDegree = monomialToCompare.getDegree();
			if ( monomialToCompare instanceof Constant ) {
				toCompareDegree = Numerical.ZERO;
			}
			
			int degreeComparison = thisDegree.compareTo( toCompareDegree );

			//if degree comparison is zero (two monomials equal in degree), then
			if ( degreeComparison == 0 ) {
				
				//compare representations alphabetically
				//return this.compareBaseRepresentation( monomialToCompare );
				return this.getRepresentation().compareTo( monomialToCompare.getRepresentation() );
				
			//if degree comparison is not zero (two monomials not equal in degree), then
			//the result is the degree comparison
			} else {
				return degreeComparison;
			}
			
		//if the given Combinable to compare is not a monomial, don't deal with it
		} else {
			return super.compareTo( toCompare );
		}
	}
	
	/**
	 * @return 				the number of terms in this <code>Monomial</code>
	 */
	@Override
	public int countNumTerms() {
		return this.m_terms.size();
	}
	
	@Override
	public Monomial copy( int indexInInput ) {
		Monomial rtn = new Monomial( getEnvironment() , getMultipliedTerms() , getCoefficient() );
		rtn.setIndexInInput( indexInInput ) ;
		return rtn;
	}
	
	@Override
	public Monomial clone() {
		return copy( getIndexInInput() );
	}
	
	@Override
	public String toLatexString() {

		String rtn = "";
		
		String coefficient = "";
		//if the coefficient is zero, then the Monomial is zero
		if ( this.m_coefficient.equals( Numerical.ZERO ) ) {
			return "0";
			
		//if the coefficient is one, then no coefficient needs to be displayed 
		} else if ( this.m_coefficient.equals( Numerical.ONE ) ) {
			
			//continue
			
		//if the coefficient is negative one, then only a negative sign is needed
		} else if ( this.m_coefficient.equals( Numerical.NEGATIVE_ONE ) ) {
			//coefficient = SubtractOperator.LATEX_REPRESENTATION;
			rtn = SubtractOperator.LATEX_REPRESENTATION + rtn;
			
		//if the coefficient is not one, then display the coefficient
		//however, if it is negative, then a negative sign is required
		//before the whole Monomial
		} else if ( this.m_coefficient.compareTo( Numerical.ZERO ) < 0 ) {
			rtn = SubtractOperator.LATEX_REPRESENTATION + rtn;
			coefficient = this.m_coefficient.multiplyByNegativeOne().toLatexString();
		} else {
			coefficient = this.m_coefficient.toLatexString();
		}
		
		//determine if the first term in the numerator is a numerical or not
		//because if it is a numerical, we will need a * between the coefficient
		//and the monomial representation
		this.m_terms.moveToStart();
		if ( this.m_terms.hasCurrent() ) {
			if ( this.m_terms.get().getBase() instanceof Numerical ) {
				if ( !coefficient.equals("") ) {
					coefficient += MultiplyOperator.LATEX_REPRESENTATION;
				}
			}
		}
		
		//add each term's LaTeX representation
		
		//first get the numerator in LaTeX
		
		String numerator = "";
		for ( MonomialTerm aTerm : this.m_terms ) {
			
			//create the numerator of the Monomial by adding all the terms
			//that have exponent greater than zero
			if ( aTerm.getMonomialTermExponent().compareTo( Numerical.ZERO ) >= 0 ) {
				numerator += aTerm.toLatexString();
			}
		}
		
		//next determine the denominator in LaTeX
		String denominator = "";
		for ( MonomialTerm aTerm : this.m_terms ) {
			
			//create the denominator by adding all the terms
			//that have exponent less than zero
			if ( aTerm.getMonomialTermExponent().compareTo( Numerical.ZERO ) < 0 ) {
				
				//take the same term, except with positive exponent
				MonomialTerm denominatorTerm = aTerm.multiplyExponentBy( Numerical.NEGATIVE_ONE );
				
				//and add the positive exponent term to the denominator
				denominator += denominatorTerm.toLatexString();
			}
		}
		
		//if the denominator does not exist, i.e. it is 1, then
		//just add the numerator
		if ( denominator.equals( "" ) ) {
			
				rtn += coefficient + numerator;
				
		//otherwise, use \frac{}{}
		} else {
			
			//update the numerator
			numerator = coefficient + numerator;
			
			//if the numerator is still blank, then it needs to be replaced with a 1
			if ( numerator.equals( "" ) ) { 
				numerator = "1";
			}
			rtn += "\\frac{" + numerator + "}{" + denominator + "}";
		}
		
		//if no terms were added, then the LaTeX representation is 1
		if ( rtn.equals("") ) {
			return "1";
		}
		return rtn;
	}
}
