import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

/** CalServer
 *
 * with TCP connection
 * available multi-client connection
 * @todo data file read
 * */
public class CalServer {
    private ServerSocket welcomeSocket;

    /** Constructor
     *
     * bind with given port number
     * */
    public CalServer(int port) {
        try {
            welcomeSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("ERR_SERVER_OPEN");
        }
    }

    /** startServer
     *
     * waiting connection
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

    /** CalServerThread
     * for multiple client
     *
     * read message from client and interpret it
     * send message with result
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
            System.out.println(this.getName() +" connected");
            try {
                inFromClient = new BufferedReader(
                        new InputStreamReader(connectionSocket.getInputStream()));
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            } catch(IOException e) {
                e.printStackTrace();
            }

            try {
                while(true) {
                    String requestMSG = inFromClient.readLine();
                    if(requestMSG.equalsIgnoreCase("q")) break;
                    System.out.println(this.getName() + " received " + requestMSG);

                    Calculator cal = new Calculator(requestMSG);
                    cal.exec();

                    String responseMSG = MyProtocol.makeResponse(cal.code, cal.content);
                    outToClient.writeBytes(responseMSG);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            finally {
                try {
                    inFromClient.close();
                    outToClient.close();
                    connectionSocket.close();
                    System.out.println(this.getName() + " closed");
                } catch (Exception e) {
                    System.out.println("ERR_SERVER_CLOSE");
                }
            }
        }
    }

    /** Calculator
     * make answer or error and content
     *
     * interpret request message to arithmetic expression
     * calculate it with considering operator's priority
     * if expression has error, handle it by setting code and content
     * */
    private static class Calculator {
        String msg;
        int code;
        String content;
        String exp;

        Calculator(String msg) {
            this.msg = msg;
            this.code = -1;
            this.content = null;
            this.exp = null;
        }

        void exec() {
            if((exp = interpretMSG()) == null) return;

            double answer;
            try{
                answer = calculate(exp);
            } catch (IllegalArgumentException e) {
                code = 1;
                content = "Divide by 0";
                return;
            }

            content = String.valueOf(answer);
            code = 0;
        }

        String interpretMSG() {
            Queue<Character> operators = new LinkedList<>();
            Queue<Double> operands = new LinkedList<>();

            String[] elems = msg.split(" ");
            int operatorNum = Integer.parseInt(elems[0]);

            for(int i=1; i<=operatorNum; i++) {
                try{
                    operators.add(
                            MyProtocol.Operator
                                    .valueOf(elems[i])
                                    .getOpr());
                } catch(IllegalArgumentException e) {
                    code = 3;
                    content = "unknown operator(you should space one by one)";
                    return null;
                }
            }

            for(int i=operatorNum + 1; i<elems.length; i++) {
                operands.add(Double.parseDouble(elems[i]));
            }

            return toArithmeticEXP(operators, operands);
        }

        String toArithmeticEXP(Queue<Character> operators, Queue<Double> operands) {
            StringBuilder infix = new StringBuilder();

            while(!operators.isEmpty()) {
                infix.append(operands.poll());
                infix.append(operators.poll());
            }

            if(operands.isEmpty()) {
                code = 2;
                content = "Too many operator";
                return null;
            }
            else if(operands.size() > 1) {
                code = 2;
                content = "Fewer operands";
                return null;
            }

            infix.append(operands.poll());
            return infix.toString();
        }

        double calculate(String infix) throws IllegalArgumentException {
            int pos = infix.indexOf('+');
            if (pos != -1) {
                return calculate(infix.substring(0, pos)) + calculate(infix.substring(pos + 1));
            } else {
                pos = infix.indexOf('-');
                if (pos != -1) {
                    return calculate(infix.substring(0, pos)) - calculate(infix.substring(pos + 1));
                } else {
                    pos = infix.indexOf('*');
                    if (pos != -1) {
                        return calculate(infix.substring(0, pos)) * calculate(infix.substring(pos + 1));
                    } else {
                        pos = infix.indexOf('/');
                        if (pos != -1) {
                            double x = calculate(infix.substring(0, pos));
                            double y = calculate(infix.substring(pos + 1));
                            if(y == 0) throw new IllegalArgumentException();
                            return x / y;
                        }
                    }
                }
            }

            String toProcess = infix.trim();
            if(toProcess.isEmpty()) return 0;
            return Double.parseDouble(toProcess);
        }
    }
}
