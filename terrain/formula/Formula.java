/* This datastructure stores a formula, f, in reverse polish notation.
 * It uses the Dijkstra's shunting yard algorithm to generate the reverse polish notation.
 * The Formula object can return the proper value for f(x, y) 
 * and a opproximation of the slopes for x and y at any point (x, y).
 */

package terrain.formula;

import java.util.*;
import terrain.tools.Stack;

public class Formula {
   
   // Epsilon determines the acuracy of the slope approximation.
   private static final double epsilon = 0.1;
   
   // The Elements of the formula are stored in reverse polish notation.
   private Element[] formula;
   
   // The formula is constructed with a String as input.
   public Formula(String s) {
      ArrayList<Element> elementList = stringToList(s);
      formula = shuntingYard(elementList);
   }
   
   // Calculates and returns f(x, y).
   public double calc(double x, double y) {
      int n = formula.length;
      Stack stack = new Stack(n);
      
      // Goes through all the Elements of the formula.
      for (int i = 0; i < n; i++) {
         Element e = formula[i];
         
         // Does a operation on the last two values on the Stack and pushes the result.
         if (e.isOperator) {
            double result = operate((double) stack.pop(), (double) stack.pop(), (char) e.value);
            stack.push(result);
         }
         // The value for variable x or y is pushed on the Stack.
         else if (e.isVariable) {
            if ((char) e.value == 'x') {stack.push(x);}
            else {stack.push(y);}
         }
         // A function is executed on the last Element of the Stack.
         else if (e.isFunction) {
            double result = executeFunction((double) stack.pop(), (String) e.value);
            stack.push(result);
         }
         // The current Element contains a number and is pushed on the Stack.
         else {
            stack.push((double) e.value);
         }
      }
      
      // The Stack now only contains one value, which is the result of the formula.
      return (double) stack.pop();
   }
   
   // Returns a approximation for the slope of x at the point (x, y).
   public double slopeX(double x, double y) {
      return (calc(x + epsilon, y) - calc(x - epsilon, y)) / (2*epsilon);
   }
   
   // Returns a approximation for the slope of y at the point (x, y).
   public double slopeY(double x, double y) {
      return (calc(x, y + epsilon) - calc(x, y - epsilon)) / (2*epsilon);
   }
   
   // Reads a String and as input and cuts it into Elements of a formula.
   private ArrayList<Element> stringToList(String s) {
      char[] characters = s.toCharArray();
      ArrayList<Element> elementList = new ArrayList<Element>();
      
      // Goes through every character of the input String.
      for (int i = 0; i < characters.length; i++) {
         char ch = characters[i];
         
         // An operator Element is created.
         if (isOperator(ch)) {
            elementList.add(new Element(ch, true, false, false));
         }
         // A variable Element is created.
         else if (ch == 'x' || ch == 'y') {
            elementList.add(new Element(ch, false, true, false));
         }
         // A number Element is created.
         else if (isNumber(ch)){
            // Collects all the digits and decimal point of the number.
            String numberString = "" + ch;
            boolean end = i == characters.length-1;
            while (!end) {
               i++;
               ch = characters[i];
               
               // The next char belongs to the number.
               if (isNumber(ch)) {
                  numberString += ch;
               }
               // The next cahr does not belong to the number.
               else {
                  end = true;
                  i--;
               }
            }
            
            double number = Double.parseDouble(numberString);
            elementList.add(new Element(number, false, false, false));
         }
         // A number Element with value e is created.
         else if (ch == 'e') {
            double number = Math.E;
            elementList.add(new Element(number, false, false, false));
         }
         // A function Element of sin or sqrt is created.
         else if (ch == 's') {
            String function = "" + ch + characters[i+1] + characters[i+2];
            elementList.add(new Element(function, false, false, true));
            i += 2;
         }
         // A function Element of cos is created.
         else if (ch == 'c') {
            String function = "" + ch + characters[i+1] + characters[i+2];
            elementList.add(new Element(function, false, false, true));
            i += 2;
         }
         // A number Element with value pi is created.
         else if (ch == 'p') {
            double number = Math.PI;
            elementList.add(new Element(number, false, false, false));
            i++;
         }
      }
      return elementList;
   }
   
   // The list of Elements is sorted into reverse polish notation.
   private Element[] shuntingYard(ArrayList<Element> elementList) {
      int n = elementList.size();
      
      // Used to store Elements during the algorithm.
      ArrayList<Element> outputList = new ArrayList<Element>();
      Stack<Element> stack = new Stack(n);
      
      // Goes through all the elements.
      for (int i = 0; i < n; i++) {
         Element e = elementList.get(i);
         
         // e is an operator and the correct 
         if (e.isOperator) {
            
            // The Stack is empty. Hence the operator can be pushed.
            if (stack.isEmpty()) {
               stack.push(e);
            }
            // Some elements on the Stack might need to be popped.
            else {
               String nextItem;
               switch ((char) e.value) {
                  // Pops Elements from the Stack until the Stack is empty or the next Element is '('.
                  // Adds all poped Elements to the outputlist and pushes e on the Stack.
                  case '+': 
                     nextItem = "" + stack.peak().value;
                     while (nextItem.charAt(0) != '(') {
                        outputList.add(stack.pop());
                        if (!stack.isEmpty()) {
                           nextItem = "" + stack.peak().value;
                        }
                        else {
                           break;
                        }
                     }
                     stack.push(e);
                     break;
                  // Pops Elements from the Stack until the Stack is empty or the next Element is '('.
                  // Adds all poped Elements to the outputlist and pushes e on the Stack.
                  case '-':
                     nextItem = "" + stack.peak().value;
                     while (nextItem.charAt(0) != '(') {
                        outputList.add(stack.pop());
                        if (!stack.isEmpty()) {
                           nextItem = "" + stack.peak().value;
                        }
                        else {
                           break;
                        }
                     }
                     stack.push(e);
                     break;
                  // Pops Elements from the Stack until the Stack is empty or the next Element is '(', '+' or '-'.
                  // Adds all poped Elements to the outputlist and pushes e on the Stack.
                  case '*':
                     nextItem = "" + stack.peak().value;
                     while (nextItem.charAt(0) != '(' && nextItem.charAt(0) != '+' && nextItem.charAt(0) != '-') {
                        outputList.add(stack.pop());
                        if (!stack.isEmpty()) {
                           nextItem = "" + stack.peak().value;
                        }
                        else {
                           break;
                        }
                     }
                     stack.push(e);
                     break;
                  // Pops Elements from the Stack until the Stack is empty or the next Element is '(', '+' or '-'.
                  // Adds all poped Elements to the outputlist and pushes e on the Stack.
                  case '/':
                     nextItem = "" + stack.peak().value;
                     while (nextItem.charAt(0) != '(' && nextItem.charAt(0) != '+' && nextItem.charAt(0) != '-') {
                        outputList.add(stack.pop());
                        if (!stack.isEmpty()) {
                           nextItem = "" + stack.peak().value;
                        }
                        else {
                           break;
                        }
                     }
                     stack.push(e);
                     break;
                  // e is pushed on the Stack.
                  case '^':
                     stack.push(e);
                     break;
                  // e is pushed on the Stack.
                  case '(':
                     stack.push(e);
                     break;
                  // Pops Elements from the Stack until the Stack is empty or the next Element is '('.
                  // Adds all poped Elements to the outputlist and then pops '(' from the Stack.
                  case ')':
                     nextItem = "" + stack.peak().value;
                     while (nextItem.charAt(0) != '(') {
                        outputList.add(stack.pop());
                        if (!stack.isEmpty()) {
                           nextItem = "" + stack.peak().value;
                        }
                        else {
                           break;
                        }
                     }
                     stack.pop();
                     
                     // When there is a function on the top of the Stack it is added to the outputlist.
                     if (!stack.isEmpty() && stack.peak().isFunction) {
                        outputList.add(stack.pop());
                     }
                     break;
               }
            }
         }
         // e is a function and is pushed on the Stack.
         else if (e.isFunction) {
            stack.push(e);
         }
         // e is a number and is added to the outputlist.
         else {
            outputList.add(e);
         }
      }
      
      // Add all Elements from the Stack to the outputlist.
      for (int i = 0; i < stack.size(); i++) {
         outputList.add(stack.pop());
      }
      return listToArray(outputList);
   }
   
   // Converts a list f Elements to an array of Elements.
   private Element[] listToArray(ArrayList<Element> list) {
      int n = list.size();
      Element[] array = new Element[n];
      for (int i = 0; i < n; i++) {
         array[i] = list.get(i);
      }
      return array;
   }
   
   // Checks if a char is an operator.
   private boolean isOperator(char ch) {
      return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '(' || ch == ')';
   }
   
   // Checks if a char belongs to a number.
   private boolean isNumber(char ch) {
      return ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' ||
             ch == '6' || ch == '7' || ch == '8' || ch == '9' || ch == '.';
   }
   
   // Excutes an operation on two numbers.
   private double operate(double rightNumber, double leftNumber, char operator) {
      switch (operator) {
         case '+': return leftNumber + rightNumber;
         case '-': return leftNumber - rightNumber;
         case '*': return leftNumber * rightNumber;
         case '/': return leftNumber / rightNumber;
         case '^': return Math.pow(leftNumber, rightNumber);
      }
      System.out.println("invalid operator");
      return 0;
   }
   
   // Excutes an function on a number.
   private double executeFunction(double element, String function) {
      switch (function) {
         case "sin": return Math.sin(element);
         case "cos": return Math.cos(element);
         case "sqr": return Math.sqrt(element);
      }
      System.out.println("invalid operator");
      return 0;
   }
}