package tk.kolumb;

public class User {
    private static int counter = 0;
    private String name;
    private String userName;
    private int id;

    public User() {
        super();
        this.userName = "user" + (int)Math.floor(Math.random()*1000);
        this.id = counter;
        counter++;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
