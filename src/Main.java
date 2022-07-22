import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Arrays;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * <h1>Rational Root Theorem Calculator<h1>
 * Designed in a way to "hopefully" debug quickly!
 * 
 * @author Jimmy Padilla
 * @version 1.0
 * @since 2022-09-07
 */

public class Main {
    
    
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        runLoop(input);  
        input.close();
    }

    /**
     * The main run loop for the Automator
     * @param input Scanner input
     */
    public static void runLoop(Scanner input) {
        /**
         * list of letters for variable names
         */
        String[] variables = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        Integer[] integers = new Integer[26];
        Boolean accept = null;
        Integer degree = null;
        System.out.println("Welcome to the Rational Root Theorem Automator! \n Warning: all roots must be integers");
        /**
         * First module; confirms the degree of polynomial with the user.
         */
        while (accept == null) {
            degree = null;
            while (degree == null) {
                System.out.println("Please select degree: (2+)");
                String degreeChoice = input.nextLine();
                try {
                    if (Integer.parseInt(degreeChoice) >= 2) {
                        degree = (int) Integer.parseInt(degreeChoice);
                    } else throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    System.err.println("Incorrect Input... ");
                }
            }
            System.out.println("Your selected degree is: " + degree + ".");
            String line = "";
            if (degree == 2) line += "QUADRATIC";
            else if (degree == 3) line += "CUBIC";
            else if (degree == 4) line += "QUARTIC";
            else if (degree == 5) line += "QUINTIC";
            else if (degree >= 6) line += "DEGREE HIGHER THAN 5";
            else line += "N/A";
        
            Boolean correct = null;
            while (correct == null) {
                System.out.println("Currently selected function: " + line + " Correct? (Y/n)");
                String charChoice = input.nextLine();
                try {
                    if (charChoice.toLowerCase().equals("y")) {
                        correct = true;
                    } else if (charChoice.toLowerCase().equals("n")) {
                        correct = false;
                    } else {
                        throw new InputMismatchException();
                    }
                } catch (InputMismatchException e) {
                    System.err.println("Incorrect Input...");
                }
            }
            if (correct) accept = true;
            else {
                System.out.println("Retrying...");
            }
        }
        System.out.println("---Accepted. Continuing---");
        Boolean cycle = null;
        /**
         * 2nd module askes for the variables of the polynomial of n degrees and solves for the rational roots.
         */
        while (cycle == null) {
            cycle = null;
            termsPerDegree(input, variables, integers, degree);
            cycle = true;
            String finalResult = Arrays.toString(new Polynomial(degree, integers).rationalSolutions());
            String textResult;
            if (finalResult.equals("[]")) textResult = "No rational solutions";
            else textResult = finalResult;
            System.out.println("Your result is: " + (textResult));
        }

    }

    //refactored method for all degrees to collect terms (max 26 lmao)
    private static void termsPerDegree(Scanner input, String[] variables, Integer[] integers, Integer degree) {
        
        for (int i = 0; i < degree + 1; i++) {
            Integer term = null;
            while (term == null) {
                System.out.print(variables[i] + " = ");
                try {
                    term = (int) Integer.parseInt(input.nextLine());
                } catch (NumberFormatException e) {
                    System.err.println("Incorrect input...");
                }
            }
            integers[i] = term;
        }
    }

    

    
    //gets the factors of a number
    public static int[] getFactors(int num) {
        int newNum;
        if (num < 0) {
            newNum = -num;
        } else {
            newNum = num;
        }
        
        ArrayList<Integer> tempArray = new ArrayList<Integer>();
        for (int i = newNum; i > 0; i--) {
            if (newNum % i == 0) {
                tempArray.add(i);
            }
        }
        Collections.reverse(tempArray);
        int[] factors = new int[tempArray.size()];
        for (int i = 0; i < tempArray.size(); i++) {
            factors[i] = tempArray.get(i);
        }
        return factors;
    }

    //removes duplicates from an array
    public static double[] removeDuplicates(double[] array) {
        
        ArrayList<Double> tempArray = new ArrayList<Double>();
        for (double num : array) {
            if (tempArray.indexOf(num) == -1) {
                tempArray.add(num);
            }
        }
        Collections.sort(tempArray);
        return getStaticArray(tempArray);
    }

    //turns dynamic ArrayList<Double} into a static double[]
    public static double[] getStaticArray(ArrayList<Double> input) {
        double[] returnArray = new double[input.size()];
        for (int i = 0; i < input.size(); i++) {
            returnArray[i] = input.get(i);
        }
        return returnArray;
    }

    //formats all of the numbers in an array to avoid horrible floating point math
    public static double[] formatArray(double[] input) {
        final DecimalFormat df = new DecimalFormat("0.0000");
        ArrayList<Double> temp = new ArrayList<Double>();
        for (double num : input) {
            temp.add(Double.parseDouble(df.format(num)));
        }
        Collections.sort(temp);
        return getStaticArray(temp);
    }
}

//cubic class to figure out the pattern for a general polynomial class this used to be a quadratic class 
class Cubic {
    int a;
    int b;
    int c;
    int d;
    public Cubic(int a, int b, int c, int d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public double solve (double num) {
        //ax^2 + bx + c
        double total = 0;
        total += this.a * Math.pow(num, 3);
        total += this.b * Math.pow(num, 2);
        total += this.c * num;
        total += this.d;
        return total;
    }

    
}


//general class with all methods necesary to solve for the rational roots of the polynomial.
class Polynomial {
    Integer degree;
    Integer[] terms;
    public Polynomial(Integer degree, Integer[] terms) {
        this.degree = degree;
        this.terms = terms;
    }

    public Integer[] getTerms() {
        return terms;
    }

    public int getDegree() {
        return degree;
    }

    //solves the polynomial for a specified x
    public double solve (double num) {
        //ax^2 + bx + c
        double total = 0;
        for (int i = 0; i < getDegree(); i++) {
            total += getTerms()[i] * Math.pow(num, getDegree() - i);
        }
        total += getTerms()[degree];
        return total;
    }

    //solves for all rationalSolutions of the given polynomial.
    public double[] rationalSolutions() {
        ArrayList<Double> solutions = new ArrayList<Double>();
        for (double root : getRoots()) {
            if (-0.005 < solve(root) && solve(root) < 0.005) {
                solutions.add(root);
            }
            if (-0.005 < solve(-root) && solve(-root) < 0.005) {
                solutions.add(-root);
            }
        }
        Collections.sort(solutions);
        return Main.getStaticArray(solutions);  
    }

    public int getLC() {
        return terms[0];
    }

    public int getConstant() {
        return terms[degree];
    }

    public double[] getRoots() {
        return getRationalRoots(getLC(), getConstant());
    }

    //returns all rational roots of a polynomial based of an LC and constant
    public static double[] getRationalRoots(int LC, int constant) {
        int[] constantFactors = Main.getFactors(constant);
        int[] leadingCoefficientFactors = Main.getFactors(LC);
        ArrayList<Double> possibleRationalRoots = new ArrayList<Double>();
        for (int constVal : constantFactors) {
            for (int lead : leadingCoefficientFactors) {
                possibleRationalRoots.add( (double) constVal / lead);
            }
        }
        Collections.sort(possibleRationalRoots);
        return Main.formatArray(Main.removeDuplicates(Main.getStaticArray(possibleRationalRoots)));
        // return returnArray;
    }

}