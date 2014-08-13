package debug;

import _library.LinkedList;
import calculate.Calculator;
import calculate.structures.polynomial.Fraction;
import calculate.structures.polynomial.Monomial;
import calculate.structures.polynomial.MonomialTerm;
import calculate.structures.polynomial.Numerical;
import calculate.structures.polynomial.PolynomialTerm;
import calculate.structures.polynomial.Variable;

public class CalculateTester {

	private static Calculator test;
	
	final public static void main( String[] args ) {
		test = new Calculator();
		test.defineVariable("x", "5");
		test.defineVariable("s", "6");
		test.defineVariable("sint", "10");
		test.defineConstant( "pi" , new Numerical( test , "3.14159" ) );
		//test.evaluate("xspisint");
		//testMonomial();
		testFraction();
	}
	
	public static void testFraction() {
		Fraction testFraction = new Fraction( test , new Numerical( test , "5" ));
		Numerical dividend = new Numerical( test , "1" );
		System.out.println( testFraction.divide( dividend ) );
	}
	
	public static void testMonomial() {
		Variable x = test.getVariable("x");
		Variable s = test.getVariable("s");
		Variable sint = test.getVariable("sint");
		Variable pi = test.getVariable("pi");
		
		LinkedList < MonomialTerm > monomialTerms = new LinkedList< MonomialTerm > ();
		monomialTerms.add( new MonomialTerm( test , x , new Numerical( test , "1" ) ) );
		
		LinkedList < MonomialTerm > monomialTerms2 = new LinkedList < MonomialTerm > ();
		//monomialTerms2.add( new MonomialTerm( test , s , new Numerical("-10" , test ) ) );
		monomialTerms2.add( new MonomialTerm( test , x, new Numerical( test ,  "1" ) ) );
		
		Numerical monomialCoefficient = new Numerical( test , "1" );
		Numerical monomialCoefficient2 = new Numerical( test , "1" );
		Monomial testMonomial = new Monomial( test , monomialTerms , monomialCoefficient );
		Monomial testMonomial2 = new Monomial( test , monomialTerms2 , monomialCoefficient2 );
		
		System.out.println( testMonomial.add( testMonomial2 ) );
	}
	
	public static void testPolynomialTerm() {
		Variable x = test.getVariable("x");
		Variable s = test.getVariable("s");
		Variable sint = test.getVariable("sint");
		Variable pi = test.getVariable("pi");
		
		PolynomialTerm one = new PolynomialTerm( test , Numerical.ONE );
		PolynomialTerm testPolynomial = new PolynomialTerm( test , x , s );
		PolynomialTerm testPolynomial2 = new PolynomialTerm( test , sint , x );
		System.out.println( testPolynomial );
		System.out.println( testPolynomial2 );
		System.out.println( one.add( one.clone() ).add( one.clone() ));
	}
	
	//implicit multiplication tests:
	/*
	 xpispispispispispis         x*pi*s*pi*s*pi*s*pi*s*pi*s*pi*s
	 sin(x)sin(x)				sin(x)*sin(x)
	 (1)(2)(3)(sin(x))			(1)*(2)*(3)*(sin(x))
	 (((((1)))))                (((((1)))))
	 4(5)						4*(5)
	 xssin(x)					x*s*sin(x)
	 xsintssssin(x)				x*sint*s*s*s*sin(x)
	 5x+4x+3x+2x+5sin(x)		5*x+4*x+3*x+2*x+5*sin(x)
	 
	 */
	
	//implicit negative sign integrated with implicit multiplication tests:
	/*
	5-x*-5							5-x*-1*5
	sin(x)*-sin(x)					sin(x)*-1*sin(x)
	pi*-pipipi						pi*-1*pi*pi*pi
	-sint+cos(x)					-1*sint+cos(x)
	0-1-2-3-4--sin(x)				0-1-2-3-4--1*sin(x)
	
	 */
	
	//convert to postfix tests:
	/*
	 5+7-8*9^5^7-3						5 7 + 8 9 5 7 ^ ^ * - 3 -
	 sin(4x+7x^2)^(3-2)					4 x * 7 x 2 ^ * + sin 3 2 - ^
	 (((sin(pi))/cos(pi))*7+5)/8/9		pi sin pi cos / 7 * 5 + 8 / 9 /
	 -((((((((((1))))))))))				-1 1 *
	 (1+2(3+4))							1 2 3 4 + * +
	 x+x+x+x+x							x x + x + x + x +
	 
	 //Bad inputs
	 (((1)								Mismatched Parentheses
	 (((1+)))							1 +
	 ()()()()()							* * * *
	 (///)(//+*-)						/ / / / / -1 * + *
	 */
	
	//simplify tests:
	/*
		Monomial mult/div tests:		shell display output
			x/(x/(x/sint))				x/sint
			xxxxx						x^5
			xsintsintxx					sint^2x^3
			xxxxx/(xxxxx)				1
			xxxxx/xxxxx					x^8
			xsintpis					pissintx
			pi/pipi/pipi/pi				1
			0xxxxsint					0
			0sintsintxx/sint/sint/x/x	0
			
		Monomial mult/div/add/sub tests:
			5xxx+5xxx					10x^3
			10x+10x+10x+10x+5x			45x
			5x/sint+5x/sint+5x/sint		(15x)/sint
			14xx/sint					(14x^2)/sint
			sint/x+sint/x				(2sint)/x
			9xsint+10sintx				19sintx
			10+10+9x/x					29
			1/x+1/x						2x
			2/x+2/x						4/x
			0.5x/x+0.5x/x				1.0
			1x/x+1x/x					2
			(45x*x)/(x*x)				45
			(1/3*x)/(x*x)				0.333333333333/x
			(1/3*sint)/(1/3*x)			sint/x
			
		Monomial mult/div/add/sub/exp tests:
			5x^4						5x^4
			5x^4s^3						5s^3x^4
			5x^4s^4/(5x^4s^4)			1
			5x^4s^3/(4s^3x^4)			1.25
			
			8^xx^x+8^xx^x				2*8^xx^x
			8^2^x						8^(2^x)
			(8^2)^x						64^x
			8^x*8^x						8^(2x)
			
		Polynomial term add/sub
		and proper mathematical ordering:
			x+s							s+x
			x+s+pi+sint+a				a+s+sint+x+pi
			a+s+sint+x+pi+e				a+s+sint+x+e+pi
			5a+2pi						5a+2pi
			2pi+5a						5a+2pi
			a^3+3a^2x+3ax^2+x^3			a^3+3a^2x+3ax^2+x^3
			x^3+3x^2a+3xa^2+a^3			a^3+3a^2x+3ax^2+x^3
			1--x						x+1
			apisint+pisinte				piasint+episint
			pisint+episint				episint+pisint
			5pisint+episint				episint+5pisint
			
		PolynomialTerm multiply
			(1+x)(1+x)						x^2+2x+1
			(1+x)(1+x)(1+x)					x^3+3x^2+3x+1
			(1+x)(1-x)						-x^2+1
			(1-1)(x^2+1)					0
			(2+2)(x^2+1)					4x^2+4
			(2+2)(x^2)(x^2+1)				4x^4+4x^2
			(2+2)(x^2)(x^2+1)(x^-2)			4x^2+4
			(2+2)(x^2)(x^2+1)(x^-2)(x^-2)	(4x^2+4)/x^2
			(x+x)(s+s)						4sx
			
		PolynomialTerm divide
			(x+1)(x+1)(x+1)/(x+1)			x^2+2x+1
			(x+sint-x-sint)/(x+sint-x-sint) 0
			((x+5)/(2x+6))*(x+3)			(x+5)/2
			
	//Bad inputs
	 */
}
