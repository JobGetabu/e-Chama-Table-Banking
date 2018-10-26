package app.hacela.chamatablebanking.model;

/**
 * Created by Job on Wednesday : 9/5/2018.
 */
public class GroupsAccount {
    private double amount;
    private double divident;

    public GroupsAccount() {
    }

    public GroupsAccount(double amount, double divident) {
        this.amount = amount;
        this.divident = divident;
    }

    @Override
    public String toString() {
        return "GroupsAccount{" +
                "amount=" + amount +
                ", divident=" + divident +
                '}';
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDivident() {
        return divident;
    }

    public void setDivident(double divident) {
        this.divident = divident;
    }
}
