package gt.edu.miumg.engine.strategy.custom;

public class MyQueue {

    private Object[] data;
    private int front;
    private int rear;
    private int size;
    private static final int MAX_SIZE = 1000;

    public MyQueue() {
        this.data = new Object[MAX_SIZE];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public void enqueue(Object item) {
        if (!isFull()) {
            rear = (rear + 1) % MAX_SIZE;
            data[rear] = item;
            size++;
        }
    }

    public Object dequeue() {
        if (!isEmpty()) {
            Object item = data[front];
            data[front] = null;
            front = (front + 1) % MAX_SIZE;
            size--;
            return item;
        }
        return null;
    }

    public Object peek() {
        if (!isEmpty()) {
            return data[front];
        }
        return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == MAX_SIZE;
    }
}