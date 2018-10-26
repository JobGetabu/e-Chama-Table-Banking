package app.hacela.chamatablebanking.model;

/**
 * Created by Job on Wednesday : 9/5/2018.
 */
public class GroupsContributionDefault {

    private double minregularcontribution;
    private double entryfee;
    private String cycleintervaltype;
    private String dayofmonth;
    private String dayofweek;
    private int cycleperiod;

    public GroupsContributionDefault() {
    }

    public GroupsContributionDefault(double minregularcontribution, double entryfee, String cycleintervaltype, String dayofmonth, String dayofweek,
                                     int cycleperiod) {
        this.minregularcontribution = minregularcontribution;
        this.entryfee = entryfee;
        this.cycleintervaltype = cycleintervaltype;
        this.dayofmonth = dayofmonth;
        this.dayofweek = dayofweek;
        this.cycleperiod = cycleperiod;
    }

    public double getMinregularcontribution() {
        return minregularcontribution;
    }

    public void setMinregularcontribution(double minregularcontribution) {
        this.minregularcontribution = minregularcontribution;
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
                "minregularcontribution=" + minregularcontribution +
                ", entryfee=" + entryfee +
                ", cycleintervaltype='" + cycleintervaltype + '\'' +
                ", dayofmonth='" + dayofmonth + '\'' +
                ", dayofweek='" + dayofweek + '\'' +
                ", cycleperiod=" + cycleperiod +
                '}';
    }
}
