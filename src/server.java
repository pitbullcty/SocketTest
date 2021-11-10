import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    public static void main(String[] args)throws IOException {
        ServerSocket server=new ServerSocket(5000);
        System.out.println("服务器启动");
        System.out.println("服务器信息："+server.getInetAddress()+" Port:"+server.getLocalPort());
        //等待多个客户端连接，循环异步线程
        while (true){
            //得到客户端
            Socket clientsocket = server.accept();

            ClientHandler clientHandler = new ClientHandler( clientsocket);
            //启动线程
            clientHandler.start();
        }
    }


    //多个客户端需要做异步操作，建立异步处理类
    private static class ClientHandler extends Thread{//线程
        private  Socket socket;//代表当前的一个连接
        private boolean flag=true;
        ClientHandler(Socket socket){
            this.socket=socket;
        }//构造方法
        //一旦Thead启动起来，就会运行run方法，代表线程启动的部分
        @Override
        public void run(){
            super.run();
            //打印客户端的信息
            System.out.println("这个新客户端发起连接啦："+socket.getInetAddress()+" P:"+socket.getPort());
            //在发送过程中会触发一个IO过程，所以需要捕获异常
            try {
                //得到打印流，用于数据输出，服务器回送数据使用，即在屏幕上显示Server要回复Client的信息
                PrintStream socketOutput=new PrintStream(socket.getOutputStream());
                //得到输入流，用于接收数据，得到Client回复服务器的信息
                BufferedReader sockeInput=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
                do {
                    //客户端回复一条数据
                    String str = sockeInput.readLine();
                    if("bye".equalsIgnoreCase(str)){
                        flag=false;
                        //回送
                        socketOutput.println("bye");
                    }else{
                        //打印到屏幕，并回送数据长度
                        System.out.println(str);
                        socketOutput.println("Server回答说：" +str.length());
                    }
                }while(flag);
                sockeInput.close();
                socketOutput.close();
            }catch (Exception e){
                //触发异常时打印一个异常信息
                System.out.println("哦欧，连接异常断开了呀！！！");
            }finally {
                //连接关闭
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("拜拜，客户端退出咯~~："+socket.getInetAddress()+" P:"+socket.getPort());
        }
    }
}