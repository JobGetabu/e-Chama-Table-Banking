package app.hacela.chamatablebanking.datasource;

/**
 * Created by Job on Wednesday : 9/5/2018.
 */
public class GroupsMembers {
    private String groupid;
    private boolean ismember;
    private String userid;
    private String username;
    private String userrole;
    private String roledescription;

    public GroupsMembers() {
    }

    public GroupsMembers(String groupid, boolean ismember, String userid, String username, String userrole, String roledescription) {
        this.groupid = groupid;
        this.ismember = ismember;
        this.userid = userid;
        this.username = username;
        this.userrole = userrole;
        this.roledescription = roledescription;
    }

    @Override
    public String toString() {
        return "GroupsMembers{" +
                "groupid='" + groupid + '\'' +
                ", ismember=" + ismember +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", userrole='" + userrole + '\'' +
                ", roledescription='" + roledescription + '\'' +
                '}';
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public boolean isIsmember() {
        return ismember;
    }

    public void setIsmember(boolean ismember) {
        this.ismember = ismember;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }

    public String getRoledescription() {
        return roledescription;
    }

    public void setRoledescription(String roledescription) {
        this.roledescription = roledescription;
    }
}
