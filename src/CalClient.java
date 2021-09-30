import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @todo comment
 * @todo print
 * */
public class CalClient {
    Socket clientSocket;

    /** Constructor
     *
     * connecting server with given ip, port
     * */
    public CalClient(String ip, int port){
        try {
            this.clientSocket = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println("ERR_CONNECTION");
        }
        System.out.println("Connected!");
    }

    /** startClient
     *
     * read expression message from user (exp should space one by one)
     * send it and read result from server
     * */
    public void startClient() {
        DataOutputStream outToServer = null;
        BufferedReader inFromUser = null;
        BufferedReader inFromServer = null;

        try {
            inFromUser = new BufferedReader(
                    new InputStreamReader(System.in));
            inFromServer = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            while(true) {
                Queue<Character> operators = new LinkedList<>();
                Queue<Double> operands = new LinkedList<>();

                System.out.print("EXP(q to quit): ");
                String exp = inFromUser.readLine();

                if(exp.equalsIgnoreCase("q")) {
                    outToServer.writeBytes(exp);
                    break;
                }

                decomposeExp(operators, operands, exp);

                String requestMSG = MyProtocol.makeRequest(operators, operands);
                outToServer.writeBytes(requestMSG);

                String responseMSG = inFromServer.readLine();
                System.out.println(responseMSG);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("ERR_MAKE_EXP");
        }

        finally {
            try {
                inFromUser.close();
                outToServer.close();
                clientSocket.close();
                System.out.println("Socket closed");
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("ERR_CLIENT_CLOSE");
            }
        }
    }

    /** decomposeExp
     * decomposing exp to separate operator and operand
     *
     * non number to operator (even if it is not correct)
     * number to operand
     * */
    private void decomposeExp(Queue<Character> operators, Queue<Double> operands, String exp) {
        String[] elem = exp.split(" ");

        for(String s : elem) {
            try {
                operands.add(Double.parseDouble(s));
            }
            catch (NumberFormatException e) {
                operators.add(s.charAt(0));
            }
        }
    }
}
