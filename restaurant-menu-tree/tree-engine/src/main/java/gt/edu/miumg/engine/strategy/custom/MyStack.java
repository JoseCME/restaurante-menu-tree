package gt.edu.miumg.engine.strategy.custom;

public class MyStack {

    private Object[] data;
    private int top;
    private static final int MAX_SIZE = 1000;

    public MyStack() {
        this.data = new Object[MAX_SIZE];
        this.top = -1;
    }

    public void push(Object item) {
        if (!isFull()) {
            top++;
            data[top] = item;
        }
    }

    public Object pop() {
        if (!isEmpty()) {
            Object item = data[top];
            data[top] = null;
            top--;
            return item;
        }
        return null;
    }

    public Object peek() {
        if (!isEmpty()) {
            return data[top];
        }
        return null;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == MAX_SIZE - 1;
    }
}