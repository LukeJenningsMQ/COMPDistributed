import java.net.*;  
import java.io.*;  
class MyServer{  
    public static void main(String args[])throws Exception{  
        ServerSocket ss=new ServerSocket(3333);  
        Socket s=ss.accept();  
        DataInputStream din=new DataInputStream(s.getInputStream());  
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
  
        String str="",str2="Gday", str3="BYE";   
        str=din.readUTF();  
        System.out.println("client says: "+str);  
        dout.writeUTF(str2);  
        dout.flush();
        str=din.readUTF();  
        System.out.println("client says: "+str);  
        dout.writeUTF(str3);  
        dout.flush();    
        din.close();  
        s.close();  
        ss.close();  
    }
}  