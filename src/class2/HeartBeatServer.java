package class2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class HeartBeatServer {

    class ClientWorker implements Runnable{
        private Socket socket;
        private String name;
        private Date lastHeartBeat;

        public ClientWorker(Socket socket){
            this.socket = socket;
            lastHeartBeat = new Date();
        }

        @Override
        public void run() {
            try {
                BufferedReader Inputstream = null;
                Inputstream = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                while (true){
                    String msg = Inputstream.readLine();
                    if (msg==null){
                        break;
                    }
                    else if(name==null){
                        name = msg;
                        System.out.println("接收到"+name+"请求["+new Date()+"]");
                        System.out.println(socket.hashCode());
                        System.out.println(this.hashCode());
                    }
                    else{
                        lastHeartBeat = new Date();
                        System.out.println("接收到"+name+"消息"+msg+"["+ lastHeartBeat+"]");
                        System.out.println(socket.hashCode());
                        System.out.println(this.hashCode());
                    }
                    Thread.sleep(100);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Date getLastHeartBeat() {
            return lastHeartBeat;
        }

        public String getName() {
            return name;
        }

        public void close() throws IOException{
            socket.close();
        }
    }

    class Monitor implements Runnable{
        private CopyOnWriteArrayList<ClientWorker> clientWorkers;
        Monitor(CopyOnWriteArrayList<ClientWorker> clientWorkers){
            this.clientWorkers = clientWorkers;
        }
        @Override
        public void run() {
            try{
                while (true){
                    for(var worker:clientWorkers){
                        Date now = new Date();
                        if(worker!=null && now.getTime()-worker.getLastHeartBeat().getTime()>=20000){
                            System.out.println(worker.getName()+"Time out...");
                            worker.close();
                            clientWorkers.remove(worker);
                        }
                    }
                    Thread.sleep(100);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void lauch(){
        try {
            ServerSocket serversocket = new ServerSocket(12000);
            Socket clientsocket = null;
            CopyOnWriteArrayList<ClientWorker> clientWorkers = new CopyOnWriteArrayList<>();
            Monitor monitor = new Monitor(clientWorkers);
            new Thread(monitor).start();
            while (true) {
               /*if(clientsocket==null){
                    clientsocket = serversocket.accept();
                }*/
                //不能使用上述写法是因为在第一次获取套接口之后不会阻塞，所以每次ClientWorker都不一样
                clientsocket = serversocket.accept();
                ClientWorker clientWorker = new ClientWorker(clientsocket);
                new Thread(clientWorker).start();
                System.out.println("这是主线程");
                clientWorkers.add(clientWorker);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        HeartBeatServer heartBeatServer = new HeartBeatServer();
        heartBeatServer.lauch();
    }
}
