import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket clientsocket;

    public static void main(String[] args){
        try{
            Client client = new Client(3000,5000);
            client.start();
            client.process();
            client.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("客户端已退出！");
    }

    public Client(int timeout,int portnum) throws Exception{
        clientsocket = new Socket();
        clientsocket.setSoTimeout(timeout);  //设置超时时间3s
        clientsocket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), portnum), timeout);
    }

    public void start(){
        System.out.println("发起连接");
        System.out.println("服务器IP地址：" + clientsocket.getLocalAddress() + " 端口:" + clientsocket.getLocalPort());
        System.out.println("客户端IP地址：" + clientsocket.getInetAddress() + " 端口:" + clientsocket.getPort());
    }

    //发送数据
    public void send() throws Exception{
        System.out.println("请输入信息：");
        Scanner input = new Scanner(System.in);  //Scanner读入键盘输入
        OutputStream outputStream = clientsocket.getOutputStream();
        PrintStream inputStream = new PrintStream(outputStream);//获取client输出流
        String message = input.next();
        inputStream.println(message);
    }

    //读取数据
    public String read() throws Exception{
        InputStream inputStream = clientsocket.getInputStream();//获取client输入流
        BufferedReader socketBuffer = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        return socketBuffer.readLine(); //读入缓冲数据
    }

    //处理数据
    public void process() throws Exception{
        while (true){
            send();
            String received = read();
            if ("bye".equalsIgnoreCase(received)) {
                System.out.println("服务器中断链接！");
                break;
            } else {
                System.out.println("服务器回复：" +received);
            }
        }
    }

    //关闭链接
    public void close() throws Exception{
        clientsocket.close();
    }
}
