import java.io.*;

public class CalClientDemo {
    public static void main(String[] args) {

        /* make server configuration file
        try {
            DataOutputStream tet = new DataOutputStream(new FileOutputStream("serverinfo.dat"));
            tet.writeUTF("localhost port 5001");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        String fileName = "serverinfo.dat";

        String ip;
        int port;

        try {
            DataInputStream confReader = new DataInputStream(
                    new FileInputStream(fileName));
            String conf = confReader.readUTF();

            String[] confs = conf.split(" ");

            ip = confs[0];
            port = Integer.parseInt(confs[2]);
        } catch (IOException e) {
            System.out.println(fileName+" not exist\n");
            ip = "localhost";
            port = 1234;
            e.printStackTrace();
        }

        CalClient calClient = new CalClient(ip, port);
        calClient.startClient();
    }

}
