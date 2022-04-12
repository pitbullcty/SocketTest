package class1;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Client_test {

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        for(int i=0; i<10; i++) {
            Thread.sleep(1000);//创建端口太快，降低创建速率
            new Thread(new ClientThread(i)).start();
        }
    }
}

class ClientThread implements Runnable {
    private int num;
    ClientThread(int i){
        this.num = i;
    }
    @Override
    public void run() {
        try {
            Socket client = new Socket("127.0.0.1", 5000);
            System.out.println(String.format("client %d connected server", num));
            InputStream is = client.getInputStream();
            OutputStream os = client.getOutputStream();

            os.write(String.format("client %d hello world!", num).getBytes(StandardCharsets.UTF_8));
            client.shutdownOutput();//这一句非常重要啊，如果没有这句，服务端的read()会一直阻塞

            int length = 0;
            byte[] buffer = new byte[200];

            while(-1 != (length = is.read(buffer, 0, buffer.length))) {
                String receiveString = new String(buffer, 0, length);
                System.out.println(String.format("clint %d receiveString : ", num) + receiveString);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}