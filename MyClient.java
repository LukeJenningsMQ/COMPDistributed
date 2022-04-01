import java.net.*;  
import java.io.*;  
import java.util.ArrayList;
class MyClient{  
public static void main(String args[])throws Exception{  
    Socket s=new Socket("localhost",50000);  
    BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));  
    DataOutputStream dout=new DataOutputStream(s.getOutputStream());   
    String serverInput="";  
   
  
    dout.write(("HELO\n").getBytes());  
    dout.flush();  
    serverInput=din.readLine();   
    dout.write(("AUTH luke\n").getBytes());  
    dout.flush();  
    serverInput=din.readLine();  
    dout.write(("REDY\n").getBytes());  
    dout.flush();
    String nextjob =din.readLine();  
    ArrayList<String> servers = new ArrayList<String>();
    ArrayList<String[]> LargestServers = new ArrayList<String[]>();
    if(nextjob.equals("NONE") == false){
        dout.write(("GETS All\n").getBytes());  
        dout.flush();
        serverInput=din.readLine();  
        String[] info = serverInput.split(" ");
        int highestCores = 0;
        String selectedServerType = "";
        dout.write(("OK\n").getBytes());  
        dout.flush();
        for(int i = 0;i < Integer.parseInt(info[1]); i++){
            serverInput = din.readLine(); 
            servers.add(serverInput);
            String[] serverInfo = serverInput.split(" ");
            if(Integer.parseInt(serverInfo[4]) > highestCores){
                selectedServerType = serverInfo[0];
                highestCores = Integer.parseInt(serverInfo[4]);
                LargestServers.clear();
            }
            if(serverInfo[0] == selectedServerType){
                LargestServers.add(serverInfo);
            }
        }
        dout.write(("OK\n").getBytes());  
        dout.flush();
        serverInput=din.readLine();
        if(serverInput.equals(".") == false){
            dout.write(("OK\n").getBytes());  
            dout.flush();
        }
    }
    int index = 0;
    String scheduled = "";
    while(nextjob.equals("NONE") == false){
            if(index == LargestServers.size()){
                index = 0;
            }
            String[] jobInfo = nextjob.split(" ");
            dout.write(("SCHD " + jobInfo[2] + " "+ LargestServers.get(index)[0] + " " + LargestServers.get(index)[1] + "\n").getBytes());  
            dout.flush();
            scheduled =din.readLine();
            if(scheduled.equals("OK")){
                index++; 
                dout.write(("REDY\n").getBytes());  
                dout.flush();
                nextjob =din.readLine();  
            } else {
                System.out.println("job: " + nextjob);
                System.out.println("scheduled: " + scheduled);
                break;
            }
    }
    dout.write(("QUIT\n").getBytes());  
    dout.flush();
    serverInput=din.readLine();       
    
    dout.close();  
    s.close();  
    }

}  