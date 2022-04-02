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
    ArrayList<Server> servers = new ArrayList<Server>(); //Will store all servers
    ArrayList<Server> LargestServers = new ArrayList<Server>(); //Will store servers that will be used for scheduling
    Server currentServer; //this varibale is the current server that is being looked at 
   
  
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
        int highestCore = -100; //this variable will be used to keep track of the higehest cores
        String selectedServerType = ""; //This will store the name of the largest server type
        dout.write(("OK\n").getBytes());  
        dout.flush();
        for(int i = 0;i < Integer.parseInt(info[1]); i++){ 
            serverInput = din.readLine(); 
            currentServer = new Server(serverInput);
            servers.add(currentServer);
            if(currentServer.getCore() > highestCore){
                selectedServerType = currentServer.serverType;
                highestCore = currentServer.getCore();
            }
        }
        for(int j = 0; j < servers.size(); j++){
                currentServer = servers.get(j);
                if(currentServer.serverType.equals(selectedServerType)){
                    LargestServers.add(currentServer);
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
            currentServer = LargestServers.get(index);
            if(currentServer.state.equals("unavailable") == false){
                dout.write(("SCHD " + jobInfo[2] + " "+ currentServer.serverType + " " + currentServer.serverID + "\n").getBytes());  
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