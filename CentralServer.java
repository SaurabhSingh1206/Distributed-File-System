import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CentralServer {


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        boolean isStopped = false;
        Map<Integer, Integer> keyToPort = new HashMap<>();
        keyToPort.put(12, 9001);
        keyToPort.put(13, 9002);
        keyToPort.put(14, 9003);
        while (!isStopped) {
            Socket socket = serverSocket.accept();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            int key = in.readInt();
            if (keyToPort.containsKey(key)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("OK");
                out.writeUTF(stringBuilder.toString());
                out.flush();
                Socket centralToFileSocket = new Socket("127.0.0.1", keyToPort.get(key));
                DataInputStream din = new DataInputStream(centralToFileSocket.getInputStream());
                DataOutputStream dout = new DataOutputStream(centralToFileSocket.getOutputStream());
                String req = in.readUTF();
                while (!req.equals("quit")) {
                    dout.writeUTF(req);
                    dout.flush();
                    String xx = din.readUTF();
                    out.writeUTF(xx);
                    out.flush();
                    req = in.readUTF();
                }
                out.writeUTF("");
                out.flush();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                out.writeUTF(stringBuilder.toString());
                out.flush();
            }
        }
    }
}
