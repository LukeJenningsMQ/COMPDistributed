import java.net.*;  
import java.io.*;  
class MyClient{  
public static void main(String args[])throws Exception{  
    Socket s=new Socket("localhost",50000);  
    BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));  
    DataOutputStream dout=new DataOutputStream(s.getOutputStream());   
    String str2="";  
   
  
    dout.write(("HELO\n").getBytes());  
    dout.flush();  
    str2=din.readLine();  
    System.out.println("Server says: "+str2); 
    dout.write(("AUTH Luke Jennings\n").getBytes());  
    dout.flush();  
    str2=din.readLine();  
    System.out.println("Server says: "+str2);
    dout.write(("REDY\n").getBytes());  
    dout.flush();
    str2=din.readLine();  
    System.out.println("Server says: "+str2);
    dout.write(("QUIT\n").getBytes());  
    dout.flush();
    str2=din.readLine();  
    System.out.println("Server says: "+str2);     
  
    dout.close();  
    s.close();  
}
}  