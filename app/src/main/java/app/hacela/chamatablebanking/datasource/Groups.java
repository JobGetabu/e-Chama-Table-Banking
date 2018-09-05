package app.hacela.chamatablebanking.datasource;

import com.google.firebase.Timestamp;

/**
 * Created by Job on Wednesday : 9/5/2018.
 */
public class Groups {
    private String groupid;
    private String groupname;
    private Timestamp createdate;
    private String createbyid;
    private String photourl;
    private String createbyname;

    public Groups() {
    }

    public Groups(String groupid, String groupname, Timestamp createdate, String createbyid, String photourl, String createbyname) {
        this.groupid = groupid;
        this.groupname = groupname;
        this.createdate = createdate;
        this.createbyid = createbyid;
        this.photourl = photourl;
        this.createbyname = createbyname;
    }

    @Override
    public String toString() {
        return "Groups{" +
                "groupid='" + groupid + '\'' +
                ", groupname='" + groupname + '\'' +
                ", createdate=" + createdate +
                ", createbyid='" + createbyid + '\'' +
                ", photourl='" + photourl + '\'' +
                ", createbyname='" + createbyname + '\'' +
                '}';
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public Timestamp getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Timestamp createdate) {
        this.createdate = createdate;
    }

    public String getCreatebyid() {
        return createbyid;
    }

    public void setCreatebyid(String createbyid) {
        this.createbyid = createbyid;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getCreatebyname() {
        return createbyname;
    }

    public void setCreatebyname(String createbyname) {
        this.createbyname = createbyname;
    }
}
