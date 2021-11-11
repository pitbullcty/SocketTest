import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serversocket;
    private Socket clientsocket;


    public static void main(String[] args)throws IOException {
        try {
            Server server = new Server(5000);
            server.start();
            server.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("服务器关闭！");
        //ServerSocket server=new ServerSocket(5000);
    }

    public Server(int portnum) throws Exception{
        serversocket = new ServerSocket(portnum);
        clientsocket = null;
    }

    public void start(){
        System.out.println("服务器启动！");
        System.out.println("服务器信息："+serversocket.getInetAddress()+" 端口:"+serversocket.getLocalPort());
    }

    //向客户端回送消息
    public void send(String message) throws Exception{
        PrintStream outputStream=new PrintStream(clientsocket.getOutputStream());
        outputStream.println(message);
    }


    //读取客户端消息
    public String receive() throws Exception{
        BufferedReader Inputstream=new BufferedReader(new InputStreamReader(clientsocket.getInputStream(),"UTF-8"));
        String message = Inputstream.readLine();
        System.out.println("从客户端收到"+message);
        return message;
    }

    //处理
    public void process() throws Exception{
        while (true){
            try{
                if(clientsocket==null) clientsocket = serversocket.accept();
                String received = receive();
                send(received);
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("发生错误");
                break;
            }
        }
        System.out.println("客户端退出！");
    }
}
