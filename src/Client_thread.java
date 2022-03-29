import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client_thread {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 5; i++) {
            Thread.sleep(1000);
            new Thread(new clientthread(i)).start();
        }
    }
    static class clientthread implements Runnable {
        private String name;

        clientthread(String name) {
            this.name = name;
        }

        clientthread(int name) {
            this.name = String.valueOf(name);
        }

        public void run() {
            Socket clientsocket = new Socket();
            try {
                clientsocket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 13000));
                PrintStream inputStream = new PrintStream(clientsocket.getOutputStream());//获取client输出流
                System.out.println("client"+name+"链接");
                String message = "这是client"+name;
                inputStream.println(message);
                InputStream outputStream = clientsocket.getInputStream();//获取client输入流
                BufferedReader socketBuffer = new BufferedReader(new InputStreamReader(outputStream, "UTF-8"));
                System.out.println(socketBuffer.readLine());

            } catch (Exception e) {
                System.out.println("client"+name+"连接失败！");
                e.printStackTrace();
            }

        }
    }
}

