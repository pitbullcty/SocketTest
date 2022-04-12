package class1;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client_new {

    public static void main(String[] args) throws InterruptedException {
        Socket clientsocket = new Socket();
        try {
            clientsocket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 12000));
            System.out.println("已连接至服务器");
            while (true){
                System.out.println("请输入信息：");
                Scanner input = new Scanner(System.in);  //Scanner读入键盘输入
                PrintStream inputStream = new PrintStream(clientsocket.getOutputStream());//获取client输出流
                String message = input.nextLine();
                inputStream.println(message);
                InputStream outputStream = clientsocket.getInputStream();//获取client输入流
                BufferedReader socketBuffer = new BufferedReader(new InputStreamReader(outputStream, "UTF-8"));
                System.out.println(socketBuffer.readLine());
            }
        } catch (Exception e) {
            try {
                clientsocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println(e);
        }
    }
}

