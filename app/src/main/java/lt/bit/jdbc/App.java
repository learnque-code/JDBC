package lt.bit.jdbc;

public class App {
    public String getGreeting() {
        return "Hello to JDBC application!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
