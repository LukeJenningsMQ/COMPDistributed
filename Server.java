public class Server {
        String[] serverInfo;
        String serverType;
        String serverID;
        String state;
        int core;
    //Server Object used to store information on servers, so when used for scheduling the code is more understandable
    public Server(String serverInput){
        serverInfo = serverInput.split(" ");
        serverType = serverInfo[0];
        serverID = serverInfo[1];
        state = serverInfo[2];
        core = Integer.parseInt(serverInfo[4]);
    }
    public String getSeverType(){
        return serverType; //Will give string on the type of the server 
    }
    public String getID(){
        return serverID; // Will return string of the server ID
    }
    public String getState(){
        return state; //Will return the server State
    }
    public int getCore(){
        return core; //Will return the server core
    }
}
