package class1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Sever_test {
    public static void main(String[] args) throws IOException, InterruptedException {

        int backlog = 3;
        ServerSocket serverSocket = new ServerSocket(5000, backlog);
        //此处会造成客户端的连接阻塞，这时就会把request connection放到请求队列，而请求队列的容量就是backlog的值
        //当程序启动，此处睡眠50秒，马上启动客户端，客户端每一秒钟起一个，起到第二个的时候，第三个就无法获得到ServerSocket
        //只能等待，前两个连接已获取ServerSocket连接，只有等这两个处理完了，后续第三个才会拿到连接，进行处理（可从客户端输出得出此结论）
        Thread.sleep(10000);//模拟服务端处理高延时任务
        while (true) {
            Socket socket = serverSocket.accept();

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(os));
            int length = -1;

            char[] buffer = new char[200];
            while (-1 != (length = br.read(buffer, 0, buffer.length))) {
                String string = new String(buffer, 0, length);
                System.out.println("TestBackLog receive String "
                        + socket.getInetAddress() + ":" + socket.getLocalPort() + string);
                out.write("server welcome!");
                out.flush();
                socket.shutdownOutput();

            }
            out.close();
            br.close();
            socket.close();
        }
    }

}
