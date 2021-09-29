public class CalClientDemo {
    public static void main(String[] args) {
        CalClient calClient = new CalClient("127.0.0.1", 5001);
        calClient.startClient();
    }
}
