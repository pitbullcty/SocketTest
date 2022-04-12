package class2;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class HeartBeatClient {
    public static void main(String[] args) throws InterruptedException {
        Socket clientsocket = new Socket();
        try {
            clientsocket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 12000));
            System.out.println("已连接至服务器");
            while (true){
                System.out.println("请输入姓名");
                Scanner input = new Scanner(System.in);  //Scanner读入键盘输入
                PrintStream inputStream = new PrintStream(clientsocket.getOutputStream());//获取client输出流
                String message = input.nextLine();
                inputStream.println(message);
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
