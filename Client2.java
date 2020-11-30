import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client2 {
    static int key = 13;
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9000);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeInt(key);
        String confirmConn = in.readUTF();
        if (!confirmConn.equals("OK")) {
            System.out.println(Colors.ANSI_RED  + "Authentication Failed ! " + Colors.ANSI_RESET);
            System.exit(0);
        }
        System.out.println(Colors.ANSI_GREEN + "Client 2 connected to server !" + Colors.ANSI_RESET);
        String command = "";
        do {
            System.out.print("~$ ");
            command = br.readLine();
            out.writeUTF(command);
            out.flush();
            String response = in.readUTF();
            System.out.println(Colors.ANSI_YELLOW + response + Colors.ANSI_RESET);
        } while (!command.equals("quit"));
        System.out.println(Colors.ANSI_RED + "Client 2 disconnected !" + Colors.ANSI_RESET);
    }
}
