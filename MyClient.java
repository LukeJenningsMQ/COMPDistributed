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
    
    //This section finds the correct servers to schedule to by find the largest server type
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
            servers.add(currentServer); //adds the server to the list of all servers
            if(currentServer.getCore() > highestCore){
                selectedServerType = currentServer.serverType; //if a new server has a higher core, the server type is saved
                highestCore = currentServer.getCore();  //the new highest core is changed
            }
        }
        for(int j = 0; j < servers.size(); j++){
                currentServer = servers.get(j);
                if(currentServer.serverType.equals(selectedServerType)){
                    LargestServers.add(currentServer); //all servers that belong to the server type with the hightest core are added to this list
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
    while(nextjob.equals("NONE") == false){ //checking that there is a new job
        String[] jobInfo = nextjob.split(" ");
        if(jobInfo[0].equals("JOBN")){ //checks that a JOBN messages came through and not a JCPL
            if(index == LargestServers.size()){
                index = 0; // resets goes back to start of the list when reaches the end
            }
            currentServer = LargestServers.get(index);//grabs nex server to schedule a job to
            if(currentServer.state.equals("unavailable") == false){//checks if the server is avaliable
                dout.write(("SCHD " + jobInfo[2] + " "+ currentServer.serverType + " " + currentServer.serverID + "\n").getBytes());  
                dout.flush();
                scheduled =din.readLine();
                if(scheduled.equals("OK")){
                    index++; 
                    dout.write(("REDY\n").getBytes());  
                    dout.flush();
                    nextjob =din.readLine();  //reads in new command from the server
                } else {
                    System.out.println("job: " + nextjob);
                    System.out.println("scheduled: " + scheduled);
                    break;
                }
            } else {
                index++; //if not avaliable will go to the next server on the list and try to send the job to that one
            }
        } else if(jobInfo[0].equals("JCPL")) {
            dout.write(("REDY\n").getBytes());  
            dout.flush();
            nextjob =din.readLine();  //read next command from the server
        } else {
            System.out.println("ERRROR");
            break;
        }
    }
    dout.write(("QUIT\n").getBytes());  
    dout.flush();
    serverInput=din.readLine();   //gets server confirmation to quit    
    
    dout.close();  
    s.close();  
    }

}  