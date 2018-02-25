/**
 * @author thejussinghj
 *
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.PatternSyntaxException;



public class Server {

	/**
	 * @param args
	 */
	private static ServerSocket s;
	private static String root;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		root = args[1];
		int port = Integer.parseInt(args[3]);
		s = new ServerSocket(port);
		System.out.println("Litening for requests at port: " + port + " .......");
		acceptRequest(s);
	}
	
	public static void acceptRequest(ServerSocket serverSocket) throws IOException {
		// TODO Auto-generated method stub
		while(true){
			Socket s = serverSocket.accept();
			ConnectionAccept c = new ConnectionAccept(s);
			c.start();
		}
	}
	
	public String manageRequest(String request) {
		// TODO Auto-generated method stub
		String[] splitArray = null;
		String reqOutput = null;
		try{
			splitArray = request.split("\\s+");
		}
		catch(PatternSyntaxException e){
			System.out.println(e);
		}
		
		if(splitArray.length <= 1){
			reqOutput = fileRead("index.html");
		}
		if(splitArray[1].equals("/")){
			reqOutput = fileRead("index.html");
		}
		else if(splitArray[1].charAt(splitArray[1].length() - 1) != '/'){
			reqOutput = fileRead(splitArray[1].substring(1));
		}
		else{
			reqOutput = "403";
		}
		
		return reqOutput;
	}
	
	public String fileRead(String filePath) {
		// TODO Auto-generated method stub
		String path = root+filePath;
		System.out.println("Searching for file in path: " + path);
		StringBuffer fileOutput = new StringBuffer();
		try{
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(path));
			while((sCurrentLine = br.readLine()) != null){
				fileOutput.append(sCurrentLine);
			}
		}
		catch(IOException e){
			fileOutput.append("404");
		}
		return fileOutput.toString();
	}
}



class ConnectionAccept extends Thread{
	Socket socket;
	PrintWriter out;
	BufferedReader reader;
	
	public ConnectionAccept(Socket socket) throws IOException{
		this.socket = socket;
		reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		out = new PrintWriter(this.socket.getOutputStream());
	}
	
	public void run(){
		try{
			String request = "";
			try{
				StringBuffer buffer = new StringBuffer();
				while(true){
					int ch = reader.read();
					if((ch < 0) || (ch == '\n')){
						break;
					}
					buffer.append((char)ch);
				}
				request = buffer.toString();
				String response;
				Server ser = new Server();
				response = ser.manageRequest(request);
				if(response.equals("404")){
					System.out.println("HTTP/1.1 404 Not Found\r\n");
					out.println("HTTP/1.1 404 Not Found\r\n");
					out.flush();
				}
				else if(response.equals("403")){
					System.out.println("HTTP/1.1 403 Bad Request\r\n");
					out.println("HTTP/1.1 403 Bad Request\r\n");
					out.flush();
				}
				else{
					System.out.println("Writing data to client");
					out.println("HTTP/1.1 200 OK\r\n");
					out.print(response);
					out.flush();
				}
			}
			catch(IOException e){
				out.flush();
			}
			System.out.println("Socket Terminated.....");
			System.out.println();
			reader.close();
			out.close();
			this.socket.close();
		}
		catch(IOException e){
			System.out.println("Socket Error");
			System.exit(1);
		}
	}
}
