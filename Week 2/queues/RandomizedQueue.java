/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] array;
    private int size;

    private RandomizedQueue<Item> copy() {
        RandomizedQueue<Item> copyOfRandomizedQueue = new RandomizedQueue<Item>();
        for (int i = 0; i < size; i++) {
            copyOfRandomizedQueue.enqueue(array[i]);
        }
        return copyOfRandomizedQueue;
    }

    private void resize(int newCapacity) {
        Item[] newArray = (Item[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        array = (Item[]) new Object[2];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (array.length == size) {
            resize(array.length * 2);
        }
        array[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int randomInt = StdRandom.uniform(0, size);
        Item randomItem = array[randomInt];
        array[randomInt] = array[size - 1];
        array[size - 1] = null;
        size--;
        //downsize?
        if (size < array.length / 4) {
            resize(array.length / 2);
        }
        return randomItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int randomInt = StdRandom.uniform(0, size);
        Item randomItem = array[randomInt];
        return randomItem;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new LinkedIterator(this.copy());
    }

    private class LinkedIterator implements Iterator<Item> {
        private RandomizedQueue<Item> randomizedQueue;

        public LinkedIterator(RandomizedQueue<Item> copyOfRandomizedQueue) {
            randomizedQueue = copyOfRandomizedQueue;
        }

        public boolean hasNext() {
            return (!randomizedQueue.isEmpty());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return (randomizedQueue.dequeue());

        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        System.out.println("1 (int) -> enqueue");
        System.out.println("2 -> deque");
        System.out.println("3 -> sample");
        System.out.println("4 -> iterate");
        System.out.println("5 -> size");
        System.out.println("6 -> isEmpty");
        System.out.println("7 -> Quit");

        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<Integer>();

        int instruction;
        while (true) {
            instruction = StdIn.readInt();

            if (instruction == 1) randomizedQueue.enqueue(StdIn.readInt());
            else if (instruction == 2) System.out.println(randomizedQueue.dequeue());
            else if (instruction == 3) System.out.println(randomizedQueue.sample());
            else if (instruction == 4) for (int i : randomizedQueue) System.out.print(i + " ");
            else if (instruction == 5) System.out.println("size is " + randomizedQueue.size());
            else if (instruction == 6)
                System.out.println(("isEmpty is " + randomizedQueue.isEmpty()));
            else if (instruction == 7) break;
        }
    }
}
