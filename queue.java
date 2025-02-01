import java.util.Scanner;

class Queue {
    private int[] queue;
    private int front;
    private int rear;
    private int list;
    private int siz;
    public Queue(int siz) {
        this.siz = siz;
        queue = new int[siz];
        front = 0;
        rear = -1;
        list = 0;
    }
    public boolean isEmpty() {
        return list == 0;
    }
    public boolean isFull() {
        return list == siz;
    }
    public void enqueue(int element) {
        if (isFull()) {
            System.out.println("Queue is full! Cannot enqueue element: " + element);
            return;
        }
        rear = (rear + 1) % siz;
        queue[rear] = element;
        list++;
        System.out.println("Enqueued: " + element);
    }
    public int dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty! Cannot dequeue element.");
            return -1; 
        }
        int element = queue[front];
        front = (front + 1) % siz; 
        list--;
        System.out.println("Dequeued: " + element);
        return element;
    }
    public void displayQueue() {
        if (isEmpty()) {
            System.out.println("Queue is empty!");
            return;
        }
        System.out.print("Queue elements: ");
        for (int i = 0; i < list; i++) {
            System.out.print(queue[(front + i) % siz] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the size of the queue: ");
        int siz = sc.nextInt();

        Queue q = new Queue(siz);

        while (true) {
            System.out.println("\n--- Queue Operations ---");
            System.out.println("1. Enqueue");
            System.out.println("2. Dequeue");
            System.out.println("3. Display Queue");
            System.out.println("4. Check if Queue is Empty");
            System.out.println("5. Check if Queue is Full");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter element to enqueue: ");
                    int element = sc.nextInt();
                    q.enqueue(element);
                    break;

                case 2:
                    q.dequeue();
                    break;

                case 3:
                    q.displayQueue();
                    break;

                case 4:
                    System.out.println("Is queue empty? " + q.isEmpty());
                    break;

                case 5:
                    System.out.println("Is queue full? " + q.isFull());
                    break;

                case 6:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
