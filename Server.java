public class Server {
        String[] serverInfo;
        String serverType;
        String serverID;
        String state;
        int core;
    public Server(String serverInput){
        serverInfo = serverInput.split(" ");
        serverType = serverInfo[0];
        serverID = serverInfo[1];
        state = serverInfo[2];
        core = Integer.parseInt(serverInfo[4]);
    }
    public String getSeverType(){
        return serverType;
    }
    public String getID(){
        return serverID;
    }
    public String getState(){
        return state;
    }
    public int getCore(){
        return core;
    }
}
