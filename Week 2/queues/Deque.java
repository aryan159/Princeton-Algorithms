/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int n;         // number of elements on queue
    private Node<Item> first;    // beginning of queue
    private Node<Item> last;     // end of queue

    // helper linked list class
    private class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> previous;
    }

    // construct an empty deque
    public Deque() {
        n = 0;
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (size() == 0);
    }

    // return the number of items on the deque
    public int size() {
        return this.n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node<Item> oldFirst = first;
        first = new Node<Item>();
        first.item = item;
        first.previous = null;
        if (isEmpty()) last = first;
        else {
            first.next = oldFirst;
            oldFirst.previous = first;
        }
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node<Item> oldLast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else {
            oldLast.next = last;
            last.previous = oldLast;
        }
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = first.item;
        if (first.next == null) {
            first = null;
            last = null;
        }
        else {
            first = first.next;
            first.previous = null;
        }
        n--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = last.item;
        if (last.previous == null) {
            first = null;
            last = null;
        }
        else {
            last = last.previous;
            last.next = null;
        }
        n--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new LinkedIterator(first);
    }

    private class LinkedIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        System.out.println("1 (int) -> addFirst");
        System.out.println("2 (int) -> addLast");
        System.out.println("3 -> removeFirst");
        System.out.println("4 -> removeLast");
        System.out.println("5 -> size");
        System.out.println("6 -> isEmpty");
        System.out.println("7 -> Quit");

        Deque<Integer> deque = new Deque<Integer>();

        int instruction;
        while (true) {
            instruction = StdIn.readInt();

            if (instruction == 1) deque.addFirst(StdIn.readInt());
            else if (instruction == 2) deque.addLast(StdIn.readInt());
            else if (instruction == 3) System.out.println(deque.removeFirst());
            else if (instruction == 4) System.out.println(deque.removeLast());
            else if (instruction == 5) System.out.println("size is " + deque.size());
            else if (instruction == 6) System.out.println(("isEmpty is " + deque.isEmpty()));
            else if (instruction == 7) break;

            System.out.println();
            for (int i : deque) {
                System.out.print(i + " ");
            }
            System.out.println();

        }


    }

}
