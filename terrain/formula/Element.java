/* This class is used to store elements of a formula.
 * An element can be a number, variable, operator or function.
 * For a number T is a double and the other variables are false, false, false.
 * For a variable T is char and the other variables are false, true, false.
 * For an operator T is a char and the other variables are true, false, false.
 * For a function T is a String and and the other variables are, false, false, true.
 */

package terrain.formula;

public class Element<T> {
   
   // Value stores the value of the Element.
   // The boolean variables code for the type of Element.
   public T value;
   public boolean isOperator;
   public boolean isVariable;
   public boolean isFunction;
   
   // Constructor for the Element object.
   public Element(T value, boolean isOperator, boolean isVariable, boolean isFunction) {
      this.value = value;
      this.isOperator = isOperator;
      this.isVariable = isVariable;
      this.isFunction = isFunction;
   }
}