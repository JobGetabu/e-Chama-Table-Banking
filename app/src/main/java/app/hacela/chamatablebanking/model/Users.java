package app.hacela.chamatablebanking.model;

/**
 * Created by Job on Wednesday : 9/5/2018.
 */
public class Users {
    private String username;
    private String devicetoken;
    private String photourl;

    public Users() {
    }

    public Users(String username, String devicetoken, String photourl) {
        this.username = username;
        this.devicetoken = devicetoken;
        this.photourl = photourl;
    }

    @Override
    public String toString() {
        return "Users{" +
                "username='" + username + '\'' +
                ", devicetoken='" + devicetoken + '\'' +
                ", photourl='" + photourl + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}

