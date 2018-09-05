package app.hacela.chamatablebanking.datasource;

/**
 * Created by Job on Wednesday : 9/5/2018.
 */
public class GroupsContributionDefault {

    private double amount;
    private String penaltycondition;
    private double penaltyamount;
    private double entryfee;
    private String cycleintervaltype;
    private String dayofmonth;
    private String dayofweek;
    private int cycleperiod;

    public GroupsContributionDefault() {
    }

    public GroupsContributionDefault(double amount, String penaltycondition, double penaltyamount,
                                     double entryfee, String cycleintervaltype, String dayofmonth, String dayofweek, int cycleperiod) {
        this.amount = amount;
        this.penaltycondition = penaltycondition;
        this.penaltyamount = penaltyamount;
        this.entryfee = entryfee;
        this.cycleintervaltype = cycleintervaltype;
        this.dayofmonth = dayofmonth;
        this.dayofweek = dayofweek;
        this.cycleperiod = cycleperiod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPenaltycondition() {
        return penaltycondition;
    }

    public void setPenaltycondition(String penaltycondition) {
        this.penaltycondition = penaltycondition;
    }

    public double getPenaltyamount() {
        return penaltyamount;
    }

    public void setPenaltyamount(double penaltyamount) {
        this.penaltyamount = penaltyamount;
    }

    public double getEntryfee() {
        return entryfee;
    }

    public void setEntryfee(double entryfee) {
        this.entryfee = entryfee;
    }

    public String getCycleintervaltype() {
        return cycleintervaltype;
    }

    public void setCycleintervaltype(String cycleintervaltype) {
        this.cycleintervaltype = cycleintervaltype;
    }

    public String getDayofmonth() {
        return dayofmonth;
    }

    public void setDayofmonth(String dayofmonth) {
        this.dayofmonth = dayofmonth;
    }

    public String getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(String dayofweek) {
        this.dayofweek = dayofweek;
    }

    public int getCycleperiod() {
        return cycleperiod;
    }

    public void setCycleperiod(int cycleperiod) {
        this.cycleperiod = cycleperiod;
    }

    @Override
    public String toString() {
        return "GroupsContributionDefault{" +
                "amount=" + amount +
                ", penaltycondition='" + penaltycondition + '\'' +
                ", penaltyamount=" + penaltyamount +
                ", entryfee=" + entryfee +
                ", cycleintervaltype='" + cycleintervaltype + '\'' +
                ", dayofmonth='" + dayofmonth + '\'' +
                ", dayofweek='" + dayofweek + '\'' +
                ", cycleperiod=" + cycleperiod +
                '}';
    }
}
