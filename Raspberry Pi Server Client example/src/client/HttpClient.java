package Ser321.http.client;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JOptionPane;
import java.net.URL;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import javax.swing.JEditorPane;
import javax.swing.text.EditorKit;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.text.*;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.tree.TreePath;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.StringBufferInputStream;
import java.io.*;
import java.util.Stack;

public class HttpClient extends BrowserGUI
                            implements ActionListener,HyperlinkListener {

   private static final boolean debugOn = true;

   private Stack<String> back = new Stack<String>();
   private URL helpURL;
   private String curr = null;
   private String ip;
   private int port;

   public HttpClient() {
      super(".");
      WindowListener wl = new WindowAdapter() {
         public void windowClosing(WindowEvent e) {System.exit(0);}
      };
      this.addWindowListener(wl);
      displayButt.addActionListener(this);
      homeButt.addActionListener(this);
      backButt.addActionListener(this);
      initHelp();
      htmlPane.setEditable(false);
      htmlPane.addHyperlinkListener(this);
	//local host for localserver
	ip = "localhost";
	port = 8080;
   }

   public HttpClient(String ip, int port) {
	super(".");
      WindowListener wl = new WindowAdapter() {
         public void windowClosing(WindowEvent e) {System.exit(0);}
      };
      this.addWindowListener(wl);
      displayButt.addActionListener(this);
      homeButt.addActionListener(this);
      backButt.addActionListener(this);
      initHelp();
      htmlPane.setEditable(false);
      htmlPane.addHyperlinkListener(this);
	this.ip = ip;
	this.port = port;
   }

   public void hyperlinkUpdate(HyperlinkEvent e) {
	try{
	      if(HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
		 debug("activated a hyperlink.");
		 urlTF.setText(e.getURL().toExternalForm());
		 String urlStr = urlTF.getText();
		 if (curr != null) back.push(curr);
		 curr = urlStr;
		 this.displayURL(new URL(urlStr));
	      }
	}catch (Exception ex) {
	}
   }

  //buttons go here
   public void actionPerformed(ActionEvent e) {
      try{
         if (e.getActionCommand().equals("Show/Refresh")){
            debug("Show/Refresh Page "+urlTF.getText());
            String urlStr = urlTF.getText();
	    if (curr != null) back.push(curr);
	    curr = urlStr;
            this.displayURL(new URL(urlStr));
         }else if(e.getActionCommand().equals("Home")){
            debug("Go to home page clicked");
            urlTF.setText("http://" + ip + ":" + port + "/Ser321/index.html");
            String urlStr = urlTF.getText();
	    if (curr != null) back.push(curr);
	    curr = urlStr;
            this.displayURL(new URL(urlStr));
         }else if(e.getActionCommand().equals("Back")){
		//stak implementation of bcak button, pops to go back pushes when new link clicked.
            debug("Back Button clicked");
            if (back.size() > 0) {
	    	curr = back.peek();
		urlTF.setText(back.pop());
		String urlStr = urlTF.getText();
		this.displayURL(new URL(urlStr));
	    }
         }
      }catch (Exception ex) {
         JOptionPane.showMessageDialog(this, "Exception: "+ex.getMessage());
         ex.printStackTrace();
      }
   }

   private void initHelp() {
      String s = null;
      try {
         s = "file://" 
            + System.getProperty("user.dir")
            + System.getProperty("file.separator")
            + "TreeDemoHelp.html";
         debug("Help URL is " + s);
         helpURL = new URL(s);
         displayURL(helpURL);
      } catch (Exception e) {
         System.err.println("Couldn't create help URL: " + s + " exception: "
                            +e.getMessage());
      }
   }

   private void displayURL(URL url) {
	htmlPane.setContentType("text/html");
	 Document document = htmlPane.getDocument();
	 document.putProperty(Document.StreamDescriptionProperty, null);
      try {
	 if (url.toExternalForm().toLowerCase().endsWith(".png")) {
		String urlStr = "<HTML><BODY><img src=\"" + url.toExternalForm() + "\"/></BODY></HTML>";
		htmlPane.setText(urlStr);
	 }
	 else if (url.toExternalForm().toLowerCase().endsWith(".jpg") || url.toExternalForm().toLowerCase().endsWith(".jpeg")) {
		String urlStr = "<HTML><BODY><img src=\"" + url.toExternalForm() + "\"/></BODY></HTML>";
		htmlPane.setText(urlStr);
	 }
	 else if (url.toExternalForm().toLowerCase().endsWith(".rtf")) {
		htmlPane.setPage(url);
	 }
	 else htmlPane.setPage(url);
      } catch (IOException e) {
         System.err.println("Attempted to read a bad URL: " + url);
      }
   }
   private void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }

   public static void main(String[] args) {
      System.out.println("syntax: java -cp classes ser321.http.client.BrowserStudent");
	if (args.length == 2) new HttpClient(args[0], Integer.parseInt(args[1]));
      else new HttpClient();
   }
}

