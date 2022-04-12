package class2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;


public class BlackHoleServerWithHeartbeat {

	class ClientWorker implements Runnable {
		// 为连入的客户端打开的套接口
		private Socket socket;
		//worker名称
		private String name=null;
		// 最后一次心跳时间
		private Date lastHeartbeat = new Date();

		ClientWorker(Socket socket) {
			this.socket = socket;
		}
		public Date getLastHeartbeat() {
			return lastHeartbeat;
		}
		public void setLastHeartbeat(Date lastHeartbeat) {
			this.lastHeartbeat = lastHeartbeat;
		}
		@Override
		public String toString(){
			return name;
		}
		public void close(){
			try {
				System.out.println(this+" close...");
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				// 打开sock输入输出流
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				while(true){
					// 读取来自客户端的字符串并输出
					String msg = reader.readLine();
					if (msg==null){
						break;
					}
					else if (name==null){
						name = msg;
						System.out.println(name+ " comes." + " [" + (new Date())+" ]");
						System.out.println(socket.hashCode());
					}
					else {
						//更新最后一次心跳
						this.setLastHeartbeat(new Date());
						System.out.println(name + ": " + msg + " [" + (new Date())+" ]");
						System.out.println(socket.hashCode());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					//当前worker退出
					this.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	class HeartbeatMonitor implements Runnable {
		//所有连入的客户端worker
		private Vector<ClientWorker> workers = null;

		HeartbeatMonitor(Vector<ClientWorker> workers) {
			this.workers = workers;
		}
		
		@Override
		public void run() {
			try {
				while(true){
					try {
						Iterator<ClientWorker> iterWorker = workers.iterator();
						while(iterWorker.hasNext()){
							ClientWorker worker = iterWorker.next();
							long diffTime = (new Date()).getTime() - worker.getLastHeartbeat().getTime();
							if (diffTime>=20000){
								System.out.println(worker+" heartbeat timeout...");
								worker.close();
								iterWorker.remove();
							}
						}
						Thread.sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		BlackHoleServerWithHeartbeat server = new BlackHoleServerWithHeartbeat();
		server.launch();
	}

	void launch() throws IOException {
		int portNumber = 12000;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			Vector<ClientWorker> workers = new Vector<ClientWorker>();
			(new Thread(new HeartbeatMonitor(workers))).start();
			while (true) {
				Socket clientSocket = serverSocket.accept();
				ClientWorker newWorker = new ClientWorker(clientSocket);
				workers.add(newWorker);
				System.out.println("这是主线程");
				(new Thread(newWorker)).start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			serverSocket.close();
		}
	}
}