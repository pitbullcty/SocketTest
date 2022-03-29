import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Sever_thread {
    public static void main(String[] args) {
        try {
            System.out.println("请输入backlog值");
            Scanner input = new Scanner(System.in);
            int backlog = input.nextInt();
            ServerSocket serversocket = new ServerSocket(13000, backlog);
            Thread.sleep(10000); //模拟延时操作
            Socket clientsocket = null;
            while (true) {
                clientsocket = serversocket.accept();
                BufferedReader Inputstream = new BufferedReader(new InputStreamReader(clientsocket.getInputStream(), "UTF-8"));
                String message = Inputstream.readLine();
                System.out.println("服务器接收到" + message);
                PrintStream outputStream = new PrintStream(clientsocket.getOutputStream());
                outputStream.println("服务器回显：" + message);
                outputStream.close();
                Inputstream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
