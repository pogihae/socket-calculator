import java.net.*;
import java.io.*;

/**CalServer
 * @todo calculator
 * @todo print
 * */
public class CalServer {
    private ServerSocket welcomeSocket;

    /** Constructor
     * bind welcome socket
     * */
    public CalServer(int port) {
        try {
            welcomeSocket = new ServerSocket(port);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("ERR_SERVER_OPEN");
        }
    }

    /** startServer
     * waiting connect
     * allocate one thread for one client
     * */
    public void startServer() {
        System.out.println("Server start...");
        while(true) {
            try {
                Socket connectionSocket = welcomeSocket.accept();

                CalServerThread cst = new CalServerThread(connectionSocket);
                cst.start();
            } catch (Exception e) {
                System.out.println("ERR_SERVER_CONNECT");
            }
        }
    }

    /** Calculator
     * to make answer or error content
     * */
    private static class Calculator {
        String msg;
        int code;
        String content;

        Calculator(String msg) {
            this.msg = msg.toUpperCase();
        }

        void exec() { this.code = 0; this.content = msg; }
    }

    /** CalServerThread
     * for multiple client
     * */
    private class CalServerThread extends Thread {
        BufferedReader inFromClient;
        DataOutputStream outToClient;
        Socket connectionSocket;

        public CalServerThread(Socket connectionSocket) {
            this.connectionSocket = connectionSocket;
        }

        @Override
        public void run() {
            try {
                inFromClient = new BufferedReader(
                        new InputStreamReader(connectionSocket.getInputStream()));
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            } catch(IOException e) {
                e.printStackTrace();
            }

            try {
                String requestMSG = inFromClient.readLine();
                while(!requestMSG.equalsIgnoreCase("q")) {
                    System.out.println(this.getName() + " received " + requestMSG);

                    Calculator cal = new Calculator(requestMSG);
                    cal.exec();

                    String responseMSG = MyProtocol.makeResponse(cal.code, cal.content);
                    outToClient.writeBytes(responseMSG);

                    requestMSG = inFromClient.readLine();
                }
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("ERR_SERVER_CALC");
            }
            finally {
                try {
                    inFromClient.close();
                    outToClient.close();
                    connectionSocket.close();
                    System.out.println("Socket closed");
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("ERR_SERVER_CLOSE");
                }
            }
        }
    }

}
