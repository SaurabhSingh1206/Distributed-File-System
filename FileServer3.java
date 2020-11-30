import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileServer3 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9003);
        String prefixToAccess = "/home/saurabh/dfs/FS3";
        boolean isStopped = false;
        Socket socket = serverSocket.accept();
        while (!isStopped) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String command = in.readUTF();
            if (command.equals("pwd")) {
                out.writeUTF(prefixToAccess);
                out.flush();
            } else if (command.equals("ls")) {
                StringBuilder stringBuilder = new StringBuilder();
                File directoryPath = new File(prefixToAccess);
                String contents[] = directoryPath.list();
                for (int i= 0;i < contents.length;i++) {
                    stringBuilder.append(contents[i]);
                    if (i < contents.length - 1)
                        stringBuilder.append("\n");
                }
                out.writeUTF(stringBuilder.toString());
                out.flush();
            } else if (command.length() >= 2 && command.substring(0, 2).equals("cp")) {
                String[] s = command.substring(2).trim().split(" ");
                String sourceFile = s[0];
                String destFile = s[1];
                File infile = new File(prefixToAccess + "/" + sourceFile);
                File outfile = new File(prefixToAccess + "/" + destFile);
                if (sourceFile.equals(destFile)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot copy ! Source file and destination files cannot have same names.");
                    out.writeUTF(stringBuilder.toString());
                    out.flush();
                } else {
                    FileInputStream instream = new FileInputStream(infile);
                    FileOutputStream outputStream = new FileOutputStream(outfile);
                    byte[] buffer = new byte[1024];
                    int length;
                    System.out.println("Opened up");
                    while ((length = instream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    instream.close();
                    outputStream.close();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("File copied successfully !");
                    out.writeUTF(stringBuilder.toString());
                    out.flush();
                }
            } else if (command.length() >= 3 && command.substring(0, 3).equals("cat")) {
                StringBuilder stringBuilder = new StringBuilder();
                String fileName = command.substring(4);
                String completePath = prefixToAccess + "/" + fileName;
                File file = new File(completePath);
                if (!file.exists()) {
                    stringBuilder.append("No such file exists at path : " + completePath);
                } else {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String st;
                    while ((st = br.readLine()) != null) {
                        stringBuilder.append(st).append("\n");
                    }
                }
                out.writeUTF(stringBuilder.toString());
                out.flush();
            } else if (command.length() >= 2 && command.substring(0, 2).equals("cd")) {
                String destinationPath = command.substring(2);
                if (destinationPath.equals("")) {
                    prefixToAccess = "/home/saurabh/dfs/FS3";
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Root changed to : " + prefixToAccess);
                    out.writeUTF(stringBuilder.toString());
                    out.flush();
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    destinationPath = destinationPath.trim();
                    String completePath = prefixToAccess + "/" + destinationPath;
                    File f = new File(completePath);
                    if (f.exists() && f.isDirectory()) {
                        prefixToAccess = completePath;
                        stringBuilder.append("Path changed to : " + prefixToAccess);
                    } else {
                        stringBuilder.append("No such file path exists : " + completePath);
                    }
                    out.writeUTF(stringBuilder.toString());
                    out.flush();
                }
            }
        }
    }
}
