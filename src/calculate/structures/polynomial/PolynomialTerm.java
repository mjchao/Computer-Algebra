package calculate.structures.polynomial;

import _library.LinkedList;
import calculate.Calculator;
import calculate.misc.CloseParenthesis;
import calculate.misc.OpenParenthesis;
import calculate.operators.AddOperator;
import calculate.operators.DivideOperator;
import calculate.structures.Combinable;

/**
 * a term in a <code>Polynomial</code>. the terms in a <code>Polynomial</code> are
 * all added together. for example, the <code>PolynomialTerms</code> in 
 * <p>
 * <pre>     5x^3+(x^2+x)/(s) + y</pre>
 * <p>
 * are 5x^3, (x^2+x)/(s), and y
 * <p>
 * <code>PolynomialTerm</code> objects are defined by a numerator and denominator that
 * are sums of <code>Monomial</code> objects
 */
public class PolynomialTerm extends Polynomial {

	private LinkedList < Monomial > m_numerator = new LinkedList < Monomial > ();
	private LinkedList < Monomial > m_denominator = new LinkedList < Monomial > ();
	private Combinable m_exponent = Numerical.ONE;
	
	/**
	 * <code>PolynomialTerm</code> constructor for a <code>Monomial</code>
	 * 
	 * @param environment
	 * @param representation
	 */
	public PolynomialTerm( Calculator environment ) {
		super( environment );
		insertAddedTerm( this.m_numerator , ( Monomial ) this );
		insertAddedTerm( this.m_denominator , Numerical.ONE );
		this.m_exponent = Numerical.ONE;
	}
	
	public void setDenominatorAndExponentAsOne() {
		this.m_denominator = new LinkedList < Monomial > ();
		insertAddedTerm( this.m_denominator , Numerical.ONE );
		this.m_exponent = Numerical.ONE;
	}
	
	/**
	 * creates a <code>PolynomialTerm</code> with one term
	 * 
	 * @param environment			environment in which calculations occur
	 * @param initialTerm			the initial term in this <code>PolynomialTerm</code>
	 */
	public PolynomialTerm( Calculator environment , Monomial initialTerm ) {
		super( environment );
		insertAddedTerm( this.m_numerator , initialTerm );
		insertAddedTerm( this.m_denominator , Numerical.ONE );
		
		this.m_exponent = Numerical.ONE;
		
		//update the representation
		determineRepresentation();
	}
	
	/**
	 * creates a <code>PolynomialTerm</code> that is the sum of two <code>Monomial</code>
	 * objects
	 * 
	 * @param environment			environment in which calculations occur
	 * @param addedTerm1			the left operand
	 * @param addedTerm2			the right operand
	 */	
	public PolynomialTerm( Calculator environment , Monomial addedTerm1 , Monomial addedTerm2 ) {
		super( environment );
		insertAddedTerm( this.m_numerator , addedTerm1 );
		insertAddedTerm( this.m_numerator , addedTerm2 );
		
		//remove any zeroes from numerator
		removeZeroes( this.m_numerator );
		
		insertAddedTerm( this.m_denominator , Numerical.ONE );
		
		this.m_exponent = Numerical.ONE;
		//update the representation
		determineRepresentation();
	}
	
	public PolynomialTerm( Calculator environment , LinkedList < Monomial > numerator , Combinable exponent ) {
		super( environment );
		this.m_numerator = numerator;
		removeZeroes( this.m_numerator );
		insertAddedTerm( this.m_denominator , Numerical.ONE );
		this.m_exponent = exponent;
		determineRepresentation();
	}
	
	public PolynomialTerm( Calculator environment , LinkedList < Monomial > numerator , LinkedList < Monomial > denominator , Combinable exponent ) {
		super( environment );
		this.m_numerator = numerator;
		this.m_denominator = denominator;
		this.m_exponent = exponent;
		
		//remove zeroes from numerator
		removeZeroes( numerator);
		
		//remove zeroes from denominator
		removeZeroes( denominator );
		
		//update representation
		determineRepresentation();
	}
	
	private static void removeZeroes( LinkedList < Monomial > terms ) {
		
		//go through all terms
		terms.moveToStart();
		while ( terms.hasCurrent() ) {
			
			//remove any zeroes
			if ( terms.get() == null || terms.get().equals( Numerical.ZERO ) ) {
				terms.remove();
			
			//if the current term is not a zero
			} else {
				
				//advance
				if ( terms.hasNext() ) {
					terms.advance();
					
				//but stop if there are no more terms left to check
				} else {
					break;
				}
			}
		}
	}
	
	/**
	 * inserts a given term into a list of added terms applying proper mathematical
	 * ordering
	 * 
	 * @param terms					the list of added terms
	 * @param termToInsert			the term to insert
	 */
	private static void insertAddedTerm( LinkedList < Monomial > terms , Monomial termToInsert ) {
		terms.moveToStart();
		boolean inserted = false;
		
		//in a sum of monomials, the higher order terms go first
		
		//first check if the term to insert should be inserted at the start
		if ( terms.hasCurrent() ) {
			Monomial firstTerm = terms.get();
			
			//first check degree
			int degreeComparison = termToInsert.getDegree().compareTo( firstTerm.getDegree() );
			if ( degreeComparison > 0 ) {
				terms.insertHead( termToInsert );
				inserted = true;
				
			//if degrees are same, go by alphabetical order
			} else if ( degreeComparison == 0 ){
				
				int baseComparison = termToInsert.compareBaseRepresentation( firstTerm );
				if ( baseComparison < 0 ) {
					terms.insertHead( termToInsert );
					inserted = true;
				}
			}
		}
		
		//if the term was not inserted at the head
		if ( inserted == false ) {
			
			//go through the rest of the list
			while ( terms.hasCurrent() && inserted == false ) {
				
				//check if the term to insert should be placed
				//between the current term and the next term
				if ( terms.hasNext() ) {
					Monomial nextTerm = terms.peek();
					
					//first compare degrees
					int degreeComparison = termToInsert.getDegree().compareTo( nextTerm.getDegree() );
					
					//if degree is greater, then
					if ( degreeComparison > 0 ) {
						
						//insert the term
						terms.insert( termToInsert );
						inserted = true;
						
					//if degrees are equal, then 
					} else if ( degreeComparison == 0 ) {
						
						//go by alphabetical order
						int baseComparison = termToInsert.compareBaseRepresentation( nextTerm );
						if ( baseComparison < 0 ) {
							terms.insert ( termToInsert );
							inserted = true;
						}
					}
					
					//move forward in the terms list
					terms.advance();
					
				//if we've reached the end of the list, i.e. there are no more terms
				//in the list, then we should stop
				} else {
					break;
				}
			}
		}
		
		//if the term was no inserted while moving through the list, then
		//append it to the end of the list
		if ( inserted == false ) {
			terms.add( termToInsert );
			inserted = true;
		}
	}
	
	private void determineRepresentation() {
		
		//determine the numerator
		String numerator = "";
		int numeratorTerms = 0;
		for ( Monomial aTerm : this.m_numerator ) {
			
			//if the numerator already has some terms in it, then 
			//we need a plus sign before we add another term
			if ( numerator.length() > 0 ) {
				numerator += AddOperator.REPRESENTATION;
			}
			numerator += aTerm.getRepresentation();
			numeratorTerms++;
		}
		
		//determine the denominator
		String denominator = "";
		int denominatorTerms = 0;
		for ( Monomial aTerm : this.m_denominator ) {
			
			//if the denominator already has some terms in it, then
			//we need a plus sign before we add another term
			if ( denominator.length() > 0 ) {
				denominator += AddOperator.REPRESENTATION;
			}
			denominator += aTerm.getRepresentation();
			denominatorTerms++;
		}
		
		//add parenthesis around the denominator if necessary
		if ( denominatorTerms > 1 ) {
			denominator = OpenParenthesis.REPRESENTATION + denominator + CloseParenthesis.REPRESENTATION;
		}
		
		String representation;
		
		//if there is no denominator, then
		if ( denominatorTerms == 0 ) {
			
			//the representation of this polynomial term is just the numerator
			representation = numerator;
			
		//if there is a denominator then
		} else {
			
			//determine if the numerator needs parentheses
			if ( numeratorTerms > 1 ) {
				numerator = OpenParenthesis.REPRESENTATION + numerator + CloseParenthesis.REPRESENTATION;
			}
			
			//the representation is the numerator divided by the denominator
			representation = numerator + DivideOperator.REPRESENTATION + denominator;	
		}
		
		//if every term was zero and removed from the numerator and denominator,
		//then this PolynomialTerm is also 0
		if ( representation.equals( "" ) ) {
			representation = "0";
		}
		
		//update the representation
		setRepresentation( representation );
	}
	
	/**
	 * @return			a deep copy of the numerator of this <code>PolynomialTerm</code>
	 */
	public LinkedList < Monomial > getNumerator() {
		return this.m_numerator.clone();
	}
	
	/**
	 * @return			if the numerator is equal to 1
	 */
	public boolean hasNumerator() {
		this.m_numerator.moveToStart();
		if ( this.m_numerator.hasCurrent() ) {
			return this.m_numerator.get().equals( Numerical.ONE );
		} else {
			return true;
		}
	}
	
	/**
	 * @return			a deep copy of the denominator of this <code>PolynomialTerm</code>
	 */
	public LinkedList < Monomial > getDenominator() {
		return this.m_denominator.clone();
	}
	
	/**
	 * @return			if the denominator is equal to 1
	 */
	public boolean hasDenominator() {
		this.m_denominator.moveToStart();
		if ( this.m_denominator.hasCurrent() ) {
			return this.m_denominator.get().equals( Numerical.ONE );
		} else {
			return true;
		}
	}
	
	public Combinable getPolynomialTermExponent() {
		if ( this.m_exponent == null ) {
			return Numerical.ONE;
		}
		return this.m_exponent.clone();
	}
	
	public void setPolynomialTermExponent( Combinable newExponent ) {
		this.m_exponent = newExponent;
	}
	
	@Override
	public Combinable getDegree() {
		this.m_numerator.moveToStart();
		this.m_denominator.moveToStart();
		
		//get numerator's degree
		Combinable numeratorDegree;
		if ( this.m_numerator.hasCurrent() ) {
			numeratorDegree = this.m_numerator.get().getDegree();
		} else {
			numeratorDegree = Numerical.ZERO;
		}
		
		//get denominator's degree
		Combinable denominatorDegree;
		if ( this.m_denominator.hasCurrent() ) {
			denominatorDegree = this.m_denominator.get().getDegree();
		} else {
			denominatorDegree = Numerical.ZERO;
		}
		
		//get the degree of this PolynomialTerm
		return numeratorDegree.subtract( denominatorDegree ).multiply( this.m_exponent );
	}
	
	@Override
	public int countNumTerms() {
		return this.m_numerator.size();
	}
	
	/**
	 * 
	 * @param augend		<code>PolynomialTerm</code> to be added to this
	 * @return				if the augend can be added with this <code>PolynomialTerm</code>
	 * 						without forming a new <code>Polynomial</code>
	 */
	public boolean isAddableWith( PolynomialTerm augend ) {
		
		//make sure the denominators are the same
		if ( this.getDenominator().equals( augend.getDenominator() ) ) {
			
			//continue
			
		//if denominators are not the same, then this cannot be added to the augend
		} else {
			
			return false;
		}
		
		if ( this.getPolynomialTermExponent().equals( Numerical.ONE ) && augend.getPolynomialTermExponent().equals( Numerical.ONE ) ) {
			
			//continue
			
		//if exponents are not 1, then they need to be distributed before they can
		//be added
		} else {
			
			return false;
			
		}
		
		//if all conditions satisfied, then this can be added to the augend
		return true;
	}

	@Override
	public Combinable add( Combinable augend ) {
		
		//only deal with PolynomialTerm addition
		if ( augend instanceof PolynomialTerm ) {
			PolynomialTerm polynomialAugend = ( PolynomialTerm ) augend;
			if ( this.isAddableWith( polynomialAugend ) ) {
				LinkedList < Monomial > thisNumeratorTerms = this.getNumerator();
				LinkedList < Monomial > augendNumeratorTerms = polynomialAugend.getNumerator();
				LinkedList < Monomial > resultNumeratorTerms = new LinkedList < Monomial > ();
				//attempt to add the augend terms to this PolynomialTerm's terms
				
				//go through the augend's terms
				for ( Monomial augendTerm : augendNumeratorTerms ) {
					
					boolean added = false;
					
					//go through the terms in this
					for ( Monomial thisTerm : thisNumeratorTerms ) {
						
						//if the two terms can be added, then
						if ( thisTerm.isAddableWith( augendTerm ) ) {
							
							//add the two terms
							Monomial sum = ( Monomial ) thisTerm.add( augendTerm );
							
							//put the sum in the result
							insertAddedTerm( resultNumeratorTerms , sum );
							
							//remove the term in this numerator that was just added
							thisNumeratorTerms.remove();
							
							//that way, after we have added all the augend terms, 
							//all the terms left in the list of terms in this
							//numerator will be the ones that were never added
							//so we can just insert those into the result terms list
							
							//don't add the augend term more than once
							added = true;
							break;
						}
					}
					
					//if we could not find a term in this numerator with which
					//to add the augend term, insert the augend term into the results
					//list
					if ( added == false ) {
						insertAddedTerm( resultNumeratorTerms , augendTerm );
					}
				}
				
				//lastly, add any of the terms in this numerator that were
				//not added with an augend
				for ( Monomial thisTerm : thisNumeratorTerms ) {
					insertAddedTerm( resultNumeratorTerms , thisTerm );
				}
				
				//the denominator stays the same
				LinkedList < Monomial > resultDenominator = this.getDenominator();
				
				//exponent stays the same
				Combinable resultExponent = this.getPolynomialTermExponent();

				//return the sum
				return new PolynomialTerm( getEnvironment() , resultNumeratorTerms , resultDenominator , resultExponent );
			} else {
				//TODO create a polynomial
				//System.out.println("exiting on add");
				//System.exit(0);
				return null;
			}
		} else {
			return super.add( augend );
		}
	}
	
	/**
	 * 
	 * @param subtrahend			value to be subtracted
	 * @return						if the subtrahend can be subtracted from this
	 * 								<code>PolynomialTerm</code>
	 */
	public boolean isSubtractableWith( PolynomialTerm subtrahend ) {
		
		//if the subtrahend can be added to this PolynomialTerm,
		//then the subtrahend can be subtracted from this PolynomialTerm
		return this.isAddableWith( subtrahend );
	}
	
	@Override
	public Combinable subtract( Combinable subtrahend ) {
		
		//only deal with PolynomialTerm subtraction
		if ( subtrahend instanceof PolynomialTerm ) {
			PolynomialTerm polynomialSubtrahend = ( PolynomialTerm ) subtrahend;
			if ( this.isSubtractableWith( polynomialSubtrahend ) ) {
				LinkedList < Monomial > thisNumeratorTerms = this.getNumerator();
				LinkedList < Monomial > subtrahendNumeratorTerms = polynomialSubtrahend.getNumerator();
				LinkedList < Monomial > resultNumeratorTerms = new LinkedList < Monomial > ();
				
				//go through the subtrahend's terms
				for ( Monomial subtrahendTerm : subtrahendNumeratorTerms ) {
					
					//see if there is a term in this PolynomialTerm
					//from which the subtrahend term can be subtracted
					boolean subtracted = false;
					for ( Monomial thisTerm : thisNumeratorTerms ) {
						
						//if there is such a term, then
						if ( thisTerm.isSubtractableWith( subtrahendTerm ) ) {
							
							//subtract them
							Monomial difference = ( Monomial ) thisTerm.subtract( subtrahendTerm );
							
							//add the difference to the result
							insertAddedTerm( resultNumeratorTerms , difference );
							
							//make sure we do not subtract from the term in this
							//PolynomialTerm again
							thisNumeratorTerms.remove();
							
							//don't subtract the subtrahend term more than twice
							subtracted = true;
						}
					}
					
					//if we could not find a term from which to subtract
					//the subtrahend term, add the opposite of that term
					//to the result terms
					if ( subtracted == false ) {
						insertAddedTerm( resultNumeratorTerms , subtrahendTerm.multiplyByNegativeOne() );
					}
				}
				
				//lastly, insert any of the terms in this polynomial from which
				//we did not subtract anything
				for ( Monomial aTerm : thisNumeratorTerms ) {
					insertAddedTerm( resultNumeratorTerms , aTerm );
				}
				
				//the denominator does not change
				LinkedList < Monomial > resultDenominatorTerms = this.getDenominator();
				
				//the exponent does not change
				Combinable resultExponent = this.getPolynomialTermExponent();
				
				//return the difference
				return new PolynomialTerm( getEnvironment() , resultNumeratorTerms , resultDenominatorTerms , resultExponent );
			} else {
				
				//TODO create a polynomial
				//System.out.println("exiting on subtract: " + this + " , " + subtrahend);
				//System.exit(0);
				return null;
			}
		//if the subtrahend is not a PolynomialTerm, don't deal with it
		} else {
			return super.subtract( subtrahend );
		}
	}
	
	/**
	 * @param multiplicand			value by which to multiply
	 * @return						if this can be multiplied by the multiplicand
	 * 								without creating a <code>Polynomial</code> object
	 */
	public boolean isMultipliableWith( PolynomialTerm multiplicand ) {
		
		//if this PolynomialTerm and the multiplicand have 1 for their exponent,
		//then they can be added together
		Combinable thisExponent;
		Combinable multiplicandExponent;
		if ( multiplicand instanceof Monomial ) {
			return true;
		}
		if ( this.m_exponent == null ) {
			thisExponent = Numerical.ONE;
		} else {
			thisExponent = this.getPolynomialTermExponent();
		}
		
		if ( multiplicand.getPolynomialTermExponent() == null ) {
			multiplicandExponent = Numerical.ONE;
		} else {
			multiplicandExponent = multiplicand.getPolynomialTermExponent();
		}
		
		if ( thisExponent.equals( Numerical.ONE ) && multiplicandExponent.equals( Numerical.ONE ) ) {
			return true;
		}
		
		//otherwise, they can't unless the term(s) with exponents are expanded
		return false;
	}
	
	@Override
	public Combinable multiply( Combinable multiplicand ) {

		//only deal with PolynomialTerm multiplication
		if ( multiplicand instanceof PolynomialTerm ) {
			PolynomialTerm polynomialMultiplicand = ( PolynomialTerm ) multiplicand;
		
			if ( this.isMultipliableWith( polynomialMultiplicand ) ) {
				
				//distribute numerator
				LinkedList < Monomial > thisNumeratorTerms = this.getNumerator();
				LinkedList < Monomial > multiplicandNumeratorTerms = polynomialMultiplicand.getNumerator();
				PolynomialTerm resultNumerator = Numerical.ZERO;
				
				//go through each term in this PolynomialTerm's numerator
				for ( Monomial thisTerm : thisNumeratorTerms ) {
					
					//and go through each term in the multiplicand's numerator 
					for ( Monomial multiplicandTerm : multiplicandNumeratorTerms ) {
						
						//and multiply them together and add it to the product numerator
						Monomial termProduct = ( Monomial ) thisTerm.multiply( multiplicandTerm );
						resultNumerator = ( PolynomialTerm ) resultNumerator.add( termProduct );
					}
				}
				
				//distribute denominator
				LinkedList < Monomial > thisDenominatorTerms = this.getDenominator();
				LinkedList < Monomial > multiplicandDenominatorTerms = polynomialMultiplicand.getDenominator();
				PolynomialTerm resultDenominator = Numerical.ZERO;
				
				//go through each term in this PolynomialTerm's denominator
				for ( Monomial thisTerm : thisDenominatorTerms ) {
					
					//and go through each term in the multiplicand's denominator
					for ( Monomial multiplicandTerm : multiplicandDenominatorTerms ) {
						
						//and multiply them together and add it to the product denominator terms
						Monomial termProduct;
						if ( thisTerm == null ) {
							termProduct = ( Monomial ) Numerical.ONE.multiply( multiplicandTerm );
						} else {
							termProduct = ( Monomial ) thisTerm.multiply( multiplicandTerm );
						}
						resultDenominator = ( PolynomialTerm ) resultDenominator.add( termProduct );
					}
				}
				
				//exponent stays the same
				Combinable resultExponent = this.getPolynomialTermExponent();
				
				//return the product
				PolynomialTerm product = new PolynomialTerm( getEnvironment() , resultNumerator.getNumerator() , resultDenominator.getNumerator() , resultExponent );
				return product;
			} else {
				
				return new Polynomial( getEnvironment() , this.clone() , polynomialMultiplicand.clone() );
			}
		} else {
			return super.multiply( multiplicand );
		}
	}
	
	public boolean isDividableBy( PolynomialTerm dividend ) {
		return this.getPolynomialTermExponent().equals( Numerical.ONE ) && dividend.getPolynomialTermExponent().equals( Numerical.ONE );
	}
	
	@Override
	public Combinable divide( Combinable dividend ) {
		
		//only deal with PolynomialTerm division
		if ( dividend instanceof PolynomialTerm ) {
			PolynomialTerm polynomialDividend = ( PolynomialTerm ) dividend;
			if ( this.equals( Numerical.ZERO ) ) {
				return Numerical.ZERO;
			}
			if ( this.isDividableBy( polynomialDividend ) ) {
				
				//divide this numerator by the dividend numerator
				PolynomialTerm thisNumerator = new PolynomialTerm( getEnvironment() , this.getNumerator() , this.getPolynomialTermExponent() );
				PolynomialTerm dividendNumerator = new PolynomialTerm( polynomialDividend.getEnvironment() , polynomialDividend.getNumerator() , polynomialDividend.getPolynomialTermExponent() );
				PolynomialTerm numeratorGcd = thisNumerator.gcd( dividendNumerator );
				
				//keep dividing out the greatest common divisor until it is one
				while ( !numeratorGcd.equals( Numerical.ONE ) ) {
					thisNumerator = thisNumerator.divideAndTruncate( numeratorGcd );
					dividendNumerator = dividendNumerator.divideAndTruncate( numeratorGcd );
					numeratorGcd = thisNumerator.gcd( dividendNumerator );
				}

				//divide the dividend denominator by this denominator
				PolynomialTerm dividendDenominator = new PolynomialTerm( polynomialDividend.getEnvironment() , polynomialDividend.getDenominator() , polynomialDividend.getPolynomialTermExponent() );
				PolynomialTerm thisDenominator = new PolynomialTerm( getEnvironment() , this.getDenominator() , this.getPolynomialTermExponent() );
				PolynomialTerm denominatorGcd = dividendDenominator.gcd( thisDenominator );
				
				//keep dividing out the greatest common divisor until it is one
				while ( !denominatorGcd.equals( Numerical.ONE ) ) {
					dividendDenominator = dividendDenominator.divideAndTruncate( denominatorGcd );
					thisDenominator = thisDenominator.divideAndTruncate( denominatorGcd );
					denominatorGcd = dividendDenominator.gcd( thisDenominator );
				}
				
				//calculate a new numerator and denominator
				PolynomialTerm newNumerator = ( PolynomialTerm ) thisNumerator.multiply( dividendDenominator );
				PolynomialTerm newDenominator = ( PolynomialTerm ) thisDenominator.multiply( dividendNumerator );
				
				//the exponent does not change
				Combinable newExponent = this.getPolynomialTermExponent();
				
				//return the new result
				return new PolynomialTerm( getEnvironment() , newNumerator.getNumerator() , newDenominator.getNumerator() , newExponent );
			
			} else {
				
				//create a polynomial that is this/dividend
				LinkedList < Monomial > inverseDividendNumerator = polynomialDividend.getDenominator();
				LinkedList < Monomial > inverseDividendDenominator = polynomialDividend.getNumerator();
				PolynomialTerm inverseDividend = new PolynomialTerm( getEnvironment() , inverseDividendNumerator , inverseDividendDenominator , this.getPolynomialTermExponent() );
				return new Polynomial( getEnvironment() , this.clone() , inverseDividend );
			}//*/
			
			/*//DEBUG
			PolynomialTerm gcd = this.gcd( polynomialDividend );
			System.out.println( gcd );
			System.exit(0);
			return null;
			//*/
		//if the dividend is not a PolynomialTerm, don't deal with it
		} else {
			return super.divide( dividend );
		}
	}
	
	/**
	 * @param dividend			value by which to divide
	 * @return					the greatest common divisor of this <code>PolynomialTerm</code>
	 * 							and the dividend
	 */
	private PolynomialTerm gcd( PolynomialTerm dividend ) {

		//use the euclidean algorithm
		
		//determine the larger and smaller degree PolynomialTerm
		PolynomialTerm largerDegree = this;
		PolynomialTerm smallerDegree = dividend;
		if ( this.getDegree().compareTo( dividend.getDegree() ) < 0 ) {
			largerDegree = dividend;
			smallerDegree = this;
		}
		
		//create deep copies
		largerDegree = largerDegree.clone();
		smallerDegree = smallerDegree.clone();

		//start with the gcd as whichever is smaller: the divisor or dividend
		//in case this division leaves no remainder
		PolynomialTerm lastNonzeroRemainder = smallerDegree;
		
		PolynomialTerm gcdDivisor = largerDegree;
		PolynomialTerm gcdDividend = smallerDegree;
		PolynomialTerm remainder = largerDegree;

		while ( true ) {
			
			PolynomialTerm[] divisionResults = gcdDivisor.divideAndRemainder( gcdDividend );
			if ( divisionResults[ 1 ].equals( this ) ) {
				return Numerical.ONE;
			}
			
			remainder = divisionResults[ 1 ];
			
			//once the remainder is zero, we can stop
			if ( remainder.equals( Numerical.ZERO ) ) {
				
				break;
				
			//if remainder is not zero, the divisor is set as the dividend
			//and the dividend set as the remainder
			} else {
				
				//update the last non-zero remainder
				lastNonzeroRemainder = remainder;
				gcdDivisor = gcdDividend;
				gcdDividend = remainder;
			}
		} 
		
		//the last non-zero remainder is the gcd
		return lastNonzeroRemainder.toMonic();
	}
	
	/**
	 * @return			this <code>PolynomialTerm</code> except in its monic form,
	 * 					i.e. no more numerical values can be factored out
	 */
	private PolynomialTerm toMonic() {

		if ( this.countNumTerms() == 0 ) {
			return this.clone();
		} else if ( this.countNumTerms() == 1) {
			if ( this.getLeadingTerm() instanceof Variable ) {
				return this.getLeadingTerm().clone();
				//return Numerical.ONE;
			} else {
				Monomial rtn = this.getLeadingTerm().clone();
				rtn.setCoefficient( Numerical.ONE );
				return rtn;
			}
		} else {
			Fraction gcd = ( Fraction ) this.getLeadingTerm().getCoefficient();
			for ( Monomial aTerm : this.m_numerator ) {
				gcd = Fraction.gcd( gcd , ( Fraction ) aTerm.getCoefficient() );
			}
			
			//if the leading term is negative, then the sign of the gcd
			//should be reversed
			if ( this.getLeadingTerm().getCoefficient().compareTo(Numerical.ZERO ) < 0 ) {
				gcd = gcd.multiplyByNegativeOne();
			}
			LinkedList < Monomial > monicTerms = new LinkedList < Monomial > ();
			for ( Monomial aTerm : this.m_numerator ) {
				monicTerms.add( ( Monomial ) aTerm.divide( gcd ) );
			}
			return new PolynomialTerm( getEnvironment() , monicTerms , getDenominator() , getPolynomialTermExponent() );
		}
	}
	
	
	/**
	 * @param dividend		value by which to divide
	 * @return				the quotient of the division without a remainder
	 */
	public PolynomialTerm divideAndTruncate( PolynomialTerm dividend ) {
		return divideAndRemainder( dividend ) [ 0 ];
	}
	
	/**
	 * @param dividend			value by which to divide that is not zero
	 * @return					an array of <code>PolynomialTerm</code> objects with
	 * 							the quotient in index 0 and the remainder in index 1
	 */
	private PolynomialTerm[] divideAndRemainder( PolynomialTerm dividend ) {
		
		//euclidean algorithm:
		
		//start with no quotient and remainder as the divisor
		PolynomialTerm quotient = Numerical.ZERO;
		PolynomialTerm remainder = this;
		
		//keep dividing until either the remainder is zero
		//or the remainder has gotten smaller than the dividend
		int iterations = 0;
		while ( !remainder.equals( Numerical.ZERO ) && remainder.getDegree().compareTo( dividend.getDegree() ) >= 0 ) {
			iterations++;
			
			//divide leading terms
			Monomial partialQuotient = ( Monomial ) remainder.getLeadingTerm().divide( dividend.getLeadingTerm() );
			/*if ( !partialQuotient.getCoefficient().isInteger() ) {
				break;
			}*/
			
			//update quotient and remainder
			quotient = ( PolynomialTerm ) quotient.add( partialQuotient );
			remainder = ( PolynomialTerm ) remainder.subtract( partialQuotient.multiply( dividend ) );
			
			if ( iterations > Calculator.DEFAULT_DIVISION_ITERATIONS ) {
				PolynomialTerm[] rtn = { Numerical.ZERO , this };
				return rtn;
			}
		}
		
		PolynomialTerm[] rtn = { quotient , remainder };
		
		return rtn;
	}
	
	@Override
	public Combinable exponentiate( Combinable exponent ) {
		
		//only deal with PolynomialTerm exponentiation
		if ( exponent instanceof PolynomialTerm ) {
			PolynomialTerm polynomialExponent = ( PolynomialTerm ) exponent;
			if ( polynomialExponent instanceof Numerical ) {
				Numerical numericalExponent = ( Numerical ) exponent;
				
				//if the exponent is an integer and this PolynomialTerm
				//can be multiplied by itself
				if ( numericalExponent.isInteger() && this.isMultipliableWith( this.clone() )) {
					
					//then we can exponentiate by repeatedly multiplying
					Numerical timesMultiplied = Numerical.ONE;
					PolynomialTerm result = this;
					
					//first,
					while ( timesMultiplied.multiply( Numerical.TWO ).compareTo( numericalExponent ) <= 0 ) {
						//keep squaring the result
						result = ( PolynomialTerm ) result.multiply( result.clone() );
						
						//update number of times multiplied
						timesMultiplied = ( Numerical ) timesMultiplied.multiply( Numerical.TWO );
					}
					
					//once squaring will go over the limit, keep multiplying by this
					while ( timesMultiplied.add( Numerical.ONE ).compareTo( numericalExponent ) <= 0 ) {
						result = ( PolynomialTerm ) result.multiply( this.clone() );
						
						//update number of times multipled
						timesMultiplied = ( Numerical ) timesMultiplied.add( Numerical.ONE );
					}
					
					//return the result
					return result;
				}
			}
			
			//if the exponent is not an integer, we can only display the exponent
			PolynomialTerm rtn;
			if ( this.getPolynomialTermExponent() instanceof Numerical ) {
				rtn = new PolynomialTerm( getEnvironment() , this.getNumerator() , this.getDenominator() , polynomialExponent.multiply( this.getPolynomialTermExponent() ) );
			} else {
				rtn = new PolynomialTerm( getEnvironment() , this.getNumerator() , this.getDenominator() , this.getPolynomialTermExponent().multiply( polynomialExponent ) );
			}
			return rtn;
		} else {
			return super.exponentiate( exponent );
		}
	}
	
	/**
	 * @return			the first term in the numerator of this <code>PolynomialTerm</code>
	 */
	public Monomial getLeadingTerm() {
		this.m_numerator.moveToStart();
		if ( this.m_numerator.hasCurrent() ) {
			return this.m_numerator.get();
		} else {
			return Numerical.ZERO;
		}
	}
	
	@Override
	public boolean equals( Object toCompare ) {
		
		//only deal with PolynomialTerm comparison
		if ( toCompare instanceof PolynomialTerm ) {
			
			//check for equaling to zero
			if ( toCompare instanceof Numerical ) {
				if ( toCompare.equals( Numerical.ZERO ) ) {
					for ( Monomial aTerm : this.m_numerator ) {
						if ( aTerm.equals( Numerical.ZERO ) ) {
							return true;
						}
					}
					return this.m_numerator.size() == 0;
				}
				if ( toCompare.equals( Numerical.ONE ) ) {
					return this.m_numerator.equals( Numerical.ONE.getNumerator() );
				}
			}
			
			PolynomialTerm polynomialToCompare = ( PolynomialTerm ) toCompare;
			
			//make sure numerators are the same
			if ( this.getNumerator().equals( polynomialToCompare.getNumerator() ) ) {
				
				//continue
				
			} else {
				return false;
			}
			
			//make sure denominators are the same
			if ( this.getDenominator().equals( polynomialToCompare.getDenominator() ) ) {
				
				//continue
				
			} else {
				return false;
			}
			
			//make sure exponents are the same
			if ( this.getPolynomialTermExponent().equals( polynomialToCompare.getPolynomialTermExponent() ) ) {
				
				//continue
				
			} else {
				return false;
			}
			
			//if all conditions satisfied, then the two PolynomialTerms are equal
			return true;
		} else {
			return super.equals( toCompare );
		}
	}
	
	@Override
	public int compareTo( Combinable toCompare ) {
		
		//only deal with PolynomialTerm comparison
		if ( toCompare instanceof PolynomialTerm ) {
			
			PolynomialTerm polynomialToCompare = ( PolynomialTerm ) toCompare;
			//first compare degree
			int degreeComparison = this.getDegree().compareTo( polynomialToCompare.getDegree() );
			
			//if degrees are same, then compare representation
			if ( degreeComparison == 0 ) {
				return this.getRepresentation().compareTo( polynomialToCompare.getRepresentation() );
				
			//if degrees are not the same, then return the degree comparison result
			} else {
				return degreeComparison;
			}
		} else {
			return super.compareTo( toCompare );
		}
	}
	
	/**
	 * reduces the numerator and denominator of this fraction as much as possible
	 * before displaying a result
	 */
	public void reduce() {
		
		//divide out common terms in the numerator and denominator
		try {
			PolynomialTerm numerator = new PolynomialTerm( getEnvironment() , this.getNumerator() , this.getPolynomialTermExponent() );
			PolynomialTerm denominator = new PolynomialTerm( getEnvironment() , this.getDenominator() , this.getPolynomialTermExponent() );
			PolynomialTerm reducedPolynomial = ( PolynomialTerm ) numerator.divide( denominator );
			this.m_numerator = reducedPolynomial.getNumerator();
			this.m_denominator = reducedPolynomial.getDenominator();
		} catch ( ClassCastException unsimplifiable ) {
			//can't simplify
		}
		
		//get rid of all fractional coefficients
		Fraction fractionalCoefficient;
		do {
			fractionalCoefficient = Numerical.ONE;
			for ( Monomial aTerm : this.m_numerator ) {
				if ( aTerm != null ) {
					if ( !aTerm.getCoefficient().isInteger() ) {
						fractionalCoefficient = ( Fraction ) aTerm.getCoefficient();
					}
				}
			}
			for ( Monomial aTerm : this.m_denominator ) {
				if ( aTerm != null ) {
					if ( !aTerm.getCoefficient().isInteger() ) {
						fractionalCoefficient = ( Fraction ) aTerm.getCoefficient();
					}
				}
			}
			PolynomialTerm numerator = new PolynomialTerm( getEnvironment() , this.getNumerator() , this.getPolynomialTermExponent() );
			PolynomialTerm denominator = new PolynomialTerm( getEnvironment() , this.getDenominator() , this.getPolynomialTermExponent() );
			numerator = ( PolynomialTerm ) numerator.multiply( fractionalCoefficient.getDenominatorAsFraction() );
			denominator = ( PolynomialTerm ) denominator.multiply( fractionalCoefficient.getDenominatorAsFraction() );
			this.m_numerator = numerator.getNumerator();
			this.m_denominator = denominator.getNumerator();
		} while ( !fractionalCoefficient.equals( Numerical.ONE ) );
	}
	
	
	@Override
	public String toLatexString() {

		this.reduce();
		
		//determine the numerator
		String numerator = "";
		for ( Monomial numeratorTerm : this.m_numerator ) {
			
			//if terms have been added to the numerator, 
			if ( numerator.length() > 0 ) {
				
				//a plus sign is required before adding another term
				if ( numeratorTerm.getCoefficient().compareTo( Numerical.ZERO ) > 0 ) {
					numerator += AddOperator.LATEX_REPRESENTATION;
				}
			}
			numerator += numeratorTerm.toLatexString();
		}
		
		if ( numerator.equals( "" ) ) {
			numerator = "0";	
		}
		
		String denominator = "";
		for ( Monomial denominatorTerm : this.m_denominator ) {
			
			//if terms have been added to the denominator
			if ( denominator.length() > 0 ) {
				
				//a plus sign is required before adding another term
				if ( denominatorTerm.getCoefficient().compareTo( Numerical.ZERO ) > 0 ) {
					denominator += AddOperator.LATEX_REPRESENTATION;
				}
			}
			denominator += denominatorTerm.toLatexString();
		}
		
		if ( denominator.equals( "" ) ) {
			denominator = "0";
		}
		
		String rtn;
		
		//if no terms were added to the denominator, then
		if ( denominator.equals( Numerical.ONE.getRepresentation() ) ) {
			
			//the LaTeX formula is just the LaTeX formula for the numerator
			if ( numerator.equals("0") ) {
				rtn = "0 \\text{ or indeterminate }";
			} else {
				rtn = numerator;
			}
		} else {
			
			//the LaTeX formula is the fractional equivalent of numerator/denominator
			if ( numerator.equals("0") ) {
				rtn = "0 \\text{ or indeterminate } ";
			} else if ( denominator.equals("0") ) {
				rtn = "\\infty \\text{ or indeterminate } ";
			} else {
				rtn = "\\frac{" + numerator + "}{" + denominator + "}";
			}
		}
		
		String exponent = this.m_exponent.toLatexString();
		if ( exponent.length() > 0 ) {
			if ( !this.m_exponent.equals( Numerical.ONE ) ) {
				rtn = OpenParenthesis.LATEX_REPRESENTATION + rtn + CloseParenthesis.LATEX_REPRESENTATION + "^{" + exponent + "}";
			}
		}
		return rtn;
	}
	
	@Override
	public PolynomialTerm clone() {
		return new PolynomialTerm( getEnvironment() , getNumerator() , getDenominator() , getPolynomialTermExponent() );
	}
	
}
