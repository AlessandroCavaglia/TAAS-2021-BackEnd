package esameTAAS.userMicroservice.Models.Comunication;

public class BasicInfoUser {
    private String username;
    private String password;
    private Boolean remember_me;

    public BasicInfoUser(String username, String password, Boolean remember_me) {
        this.username = username;
        this.password = password;
        this.remember_me = remember_me;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRemember_me() {
        return remember_me;
    }

    public void setRemember_me(Boolean remember_me) {
        this.remember_me = remember_me;
    }
}
