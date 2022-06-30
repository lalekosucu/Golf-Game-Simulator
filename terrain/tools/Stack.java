/* This is a Stack object, which can do most of the operation of a standard Stack.
 * It can: push, pop, peak, isEmpty and return its size.
 */

package terrain.tools;

public class Stack<E> {
   
   // In stack the values are stored.
   // The variable size is used to keep track of the size of the Stack.
   private E[] stack;
   private int size;
   
   // Constructs the Stack object and the maximum size is determined.
   public Stack(int maxSize) {
      stack = (E[]) new Object[maxSize];
      this.size = 0;
   }
   
   // Pushes an element on the Stack.
   public void push(E e) {
      stack[size++] = e;
   }
   
   // Pops the last element on the Stack.
   public E pop() {
      size--;
      E output = stack[size];
      stack[size] = null;
      return output;
   }
   
   // Shows the last element on the Stack.
   public E peak() {
      return stack[size-1];
   }
   
   // Return whether or not the Stack contains any elements.
   public boolean isEmpty() {
      return size == 0;
   }
   
   // Returns the size of the Stack.
   public int size() {
      return size;
   }
}