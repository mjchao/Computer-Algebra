Computer-Algebra
================

Performs basic, middle-school (or possibly early high-school) algebraic manipulations. For example, (x+y)^2 is evaluated as x^2 + 2xy + y^2. This program was written during June 2013. After the first week of July, I stopped development on this computer algebra program.

System Requirements
-----

This was programmed in Java 6. You will need a Java 6 Runtime Environment or later version. Java can be downloaded at 

http://www.oracle.com

Once you have Java, to run the program, download the **raw** file cas.jar and double click on it to run. You can also run it from command line by typing "java -jar cas.jar"

Additional Credits
-----

The LaTeX rendering in this program comes from the jlatexmath library. The library's webpage can be found here:

http://forge.scilab.org/index.php/p/jlatexmath/

Usage
-----

For this computer algebra program, the lowercase english letters a-z, the digits 0-9, and the operators +, -, *, /, ^ as well as parentheses ( and ) are recognized. Everything else is not. The letters a-z serve as variables. You may input any valid mathematical expression using these recognized symbols and the program will attempt to "manipulate" it. "Manipulate" is defined as executing as many operations +, -, *, /, ^ as possible, and then collecting like terms. If the program cannot evaluate an expression, it will say so using a dialog box.

Shortcomings
-----

The program is limited in several ways. First, given an expression involving enough repeated exponentiation "^", the program will probably be unable to evaluate it. "x^(x^(x^(x))" is one such example. Second, it doesn't help with anything other than manipulating variables and performing exact numerical calculations. For example, many situations will require functions like sine or cosine, which this program does not support.

These shortcomings probably stem from a single source: the design of the program. I approached this with object-oriented programming, which, was and still is, in my opinion, a good attack. The problem was that my representation of the "polynomial" was poorly chosen, and consequently, it became difficult to represent a complicated thing such as "x^(x^(x^(x)))". A survey of the Polynomial class's code will show that the design eventually broke down and became too difficult manage. I certainly learned my lesson in this experiment: thorough planning - drawn out and written on **physical paper** - will significantly help detect flaws in design well in advance.

With more knowledge of abstract algebra, I now believe that it would have been better to take an object-oriented approach using algebraic structures. Although the development would be more complicated and take significantly longer, I predict it would yield much cleaner code and more room for future expansions (e.g. to include trigonometric functions). 
