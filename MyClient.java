import java.net.*;  
import java.io.*;  
import java.util.ArrayList;
class MyClient{  
public static void main(String args[])throws Exception{  
    Socket s=new Socket("localhost",50000);  
    BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));  
    DataOutputStream dout=new DataOutputStream(s.getOutputStream()); 

    String serverInput="";  //Used for when the server sends information to this client.
    String username = System.getProperty("user.name");   //Use for authenication
    ArrayList<String> servers = new ArrayList<String>(); //Will store all servers
    ArrayList<String[]> LargestServers = new ArrayList<String[]>(); //Will store servers that will be used for scheduling
   
  
    dout.write(("HELO\n").getBytes()); //start of the handshake procedure  
    dout.flush();  
    serverInput=din.readLine(); //Gets OK from server if it recieves our first message
    dout.write(("AUTH " + username +"\n").getBytes());  
    dout.flush();  
    serverInput=din.readLine();  //Get a welcome message from server
    dout.write(("REDY\n").getBytes());  
    dout.flush(); 
    String nextjob =din.readLine();  //This will get the first job
    if(nextjob.equals("NONE") == false){
        dout.write(("GETS All\n").getBytes());  //get all server informaiton
        dout.flush();
        serverInput=din.readLine(); //will recieve DATA message from server 
        String[] info = serverInput.split(" "); //split the DATA Message up so we an gain how many servers to go through
        int highestCores = -100; //this variable will be used to keep track of the higehest cores
        String selectedServerType = ""; //This will store the name of the largest server type
        String[] serverInfo; 
        dout.write(("OK\n").getBytes());  
        dout.flush();
        for(int i = 0;i < Integer.parseInt(info[1]); i++){ 
            serverInput = din.readLine(); 
            System.out.println("serverInput: " +serverInput);
            servers.add(serverInput);
            serverInfo = serverInput.split(" ");
            if(Integer.parseInt(serverInfo[4]) > highestCores){
                selectedServerType = serverInfo[0];
                highestCores = Integer.parseInt(serverInfo[4]);
            }
        }
        for(int j = 0; j < servers.size(); j++){
            serverInfo = servers.get(j).split(" ");
                    if(serverInfo[0].equals(selectedServerType)){
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
        String[] jobInfo = nextjob.split(" ");
        if(jobInfo[0].equals("JOBN")){
            if(index == LargestServers.size()){
                index = 0;
            }
            if(LargestServers.get(index)[2].equals("unavailable") == false){
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
            } else {
                index++;
            }
        } else if(jobInfo[0].equals("JCPL")) {
            dout.write(("REDY\n").getBytes());  
            dout.flush();
            nextjob =din.readLine();  
        } else {
            System.out.println("ERRRORORRR");
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