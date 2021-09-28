import java.net.*;
import java.io.*;

/**
 * @ https://stackoverflow.com/questions/10131377/socket-programming-multiple-client-to-one-server
 * @todo calculator
 * */
public class CalServer {
    private ServerSocket welcomeSocket;

    public CalServer(String ip, int port) {
        try {
            welcomeSocket = new ServerSocket();
            welcomeSocket.bind(new InetSocketAddress(ip, port));
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("ERR_SERVER_OPEN");
        }
    }

    public void startServer() {
        while(true) {
            try {
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Server start...");

                CalServerThread cst = new CalServerThread(connectionSocket);
                cst.start();
            } catch (Exception e) {
                System.out.println("ERR_SERVER_CONNECT");
            }
        }
    }

    private class Calculator {
        int errCode;
        String content;

        void exec(String requestMSG) { this.errCode = 0; this.content = "test"; }
    }

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
                while(!requestMSG.equalsIgnoreCase("end")) {

                    Calculator cal = new Calculator();
                    cal.exec(requestMSG);

                    String responseMSG = MyProtocol.makeResponse(cal.errCode, cal.content);
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
