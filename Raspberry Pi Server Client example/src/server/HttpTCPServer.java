package Ser321.http.server;
import java.net.*;
import java.io.*;
import java.util.*;

public class HttpTCPServer extends Thread {
  private Socket conn;
  private int id;
  String header;
  String file;
  int size;
  int bytes;
  DataInputStream inFile;

  public HttpTCPServer (Socket sock, int id) {
    this.conn = sock;
    this.id = id;
  }

   public void run() {
      try {
	 DataOutputStream outSock = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
         
         InputStream inSocket = conn.getInputStream();
         byte clientInput[] = new byte[1024]; // 
         int numr = inSocket.read(clientInput,0,1024);
         while (numr != -1) {
            String clientString = new String(clientInput,0,numr);

	    	String[] file = clientString.split(" ");
			file = "www" + file[1]; 
			//name of the file

			try {
				inFile = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
				size = inFile.available();
            	System.out.println("read "+ size +" bytes");
				makeHeader();
				outSock.writeBytes(header);
			} catch (Exception e) {
				//if if not connect, or no file found
				System.out.println("\nError 404: File Not Found.\n");
			}
            System.out.println("read from client: "+id+" the string: "
                               +file[1]);

	    bytes = 0;
	    int i = inFile.read();
	    while (i != -1) {
	    	outSock.writeByte(i);
		bytes++;
		i = inFile.read();
	    }
	    System.out.println("Sent " + bytes + " bytes");
	    outSock.flush();


            numr = inSocket.read(clientInput,0,1024);
         }
         inSocket.close();
         outSock.close();
         conn.close();
      } catch (IOException e) {
         System.out.println("Can't get I/O for the connection.");
      }
   }
   public void makeHeader(){
		String contentType = "";
		if((file.toLowerCase().endsWith(".jpg")) || (file.toLowerCase().endsWith(".jpeg"))) {
			contentType = "image/jpg";
		} else if(file.toLowerCase().endsWith(".png")) {
			contentType = "image/png";
		} else if(file.toLowerCase().endsWith(".html")) {
			contentType = "text/html";
		} else if(file.toLowerCase().endsWith(".rtf")) {
			contentType = "text/rtf";
		}
		
		header = "HTTP/1.1 200 OK\n" +
				"Allow: GET\n" +
				"MIME-Version: 1.0\n" +
				"Content-Type: " + contentType + "\n" +
				"Content-Length: " + size + "\n\n";
		System.out.println(header);
   }
    
   public static void main (String args[]) {
    Socket sock;
    int id=0;
    try {
      if (args.length != 1) {
        System.out.println("Usage: java ser321.sockets.ThreadedEchoServer"+
                           " [portNum]");
        System.exit(0);
      }
      int portNo = Integer.parseInt(args[0]);
      if (portNo <= 1024) portNo=8888;
      ServerSocket serv = new ServerSocket(portNo);
      while (true) {
        System.out.println("server waiting for connetion on port: "
                            +portNo);
        sock = serv.accept();
        System.out.println("Echo server connected to client: "+id);
        HttpTCPServer myServerThread = new HttpTCPServer(sock,id++);
        myServerThread.start();
      }
    } catch(Exception e) {e.printStackTrace();}
  }

}

