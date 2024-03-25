public class User {
    private String Username; //Username variable
    private String Password; //Password variable

    public User(String username, String password){ //Constructor for user
        this.Username = username;
        this.Password = password;
    }
    public String getUsername(){ //Getter for username
        return Username;
    }
    public String getPassword(){ //Getter for password
        return Password;
    }
    public void setUsername(String username){
        this.Username=username;
    }
    public void setPassword(String password){
        this.Password=password;
    }

}
