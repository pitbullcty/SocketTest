package class1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_new {
    public static void main(String[] args) {
        try {
            ServerSocket serversocket = new ServerSocket(12000,50);
            Socket clientsocket = null;
            while (true) {
                if (clientsocket == null) clientsocket = serversocket.accept();
                BufferedReader Inputstream = new BufferedReader(new InputStreamReader(clientsocket.getInputStream(), "UTF-8"));
                String message = Inputstream.readLine();
                System.out.println(message);
                PrintStream outputStream = new PrintStream(clientsocket.getOutputStream());
                outputStream.println("服务器回显："+message);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
