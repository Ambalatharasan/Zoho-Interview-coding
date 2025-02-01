
import java.util.Scanner;

class Stack {

    private int[] stack;
    private int top;
    private int size;

    public Stack(int size) {
        this.size = size;
        stack = new int[size];
        top = -1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == size - 1;
    }

    public void push(int element) {
        if (isFull()) {
            System.out.println("Stack is full! Cannot push element: " + element);
            return;
        }
        stack[++top] = element;
        System.out.println("Pushed: " + element);
    }

    public int pop() {
        if (isEmpty()) {
            System.out.println("Stack is empty! Cannot pop element.");
            return -1;
        }
        System.out.println("Popped: " + stack[top]);
        return stack[top--];
    }

    public void displayStack() {
        if (isEmpty()) {
            System.out.println("Stack is empty!");
            return;
        }
        System.out.print("Stack elements: ");
        for (int i = top; i >= 0; i--) {
            System.out.print(stack[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the size of the stack: ");
        int size = scanner.nextInt();

        Stack s = new Stack(size);

        while (true) {
            System.out.println("\n--- Stack Operations ---");
            System.out.println("1. Push");
            System.out.println("2. Pop");
            System.out.println("3. Display Stack");
            System.out.println("4. Check if Stack is Empty");
            System.out.println("5. Check if Stack is Full");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter element to push: ");
                    int element = scanner.nextInt();
                    s.push(element);
                    break;

                case 2:
                    s.pop();
                    break;

                case 3:
                    s.displayStack();
                    break;

                case 4:
                    System.out.println("Is stack empty? " + s.isEmpty());
                    break;

                case 5:
                    System.out.println("Is stack full? " + s.isFull());
                    break;

                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
