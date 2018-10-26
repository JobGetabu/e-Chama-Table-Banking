package app.hacela.chamatablebanking.model;

import com.google.firebase.Timestamp;

/**
 * Created by Job on Wednesday : 9/5/2018.
 */
public class UsersTransactions {
    private double amount;
    private String details;
    private String paymentsystem;
    private String status;
    private Timestamp timestamp;
    private String transactionid;
    private String type;
    private String userid;
    private String username;

    public UsersTransactions() {
    }

    public UsersTransactions(double amount, String details, String paymentsystem, String status,
                             Timestamp timestamp, String transactionid, String type, String userid, String username) {
        this.amount = amount;
        this.details = details;
        this.paymentsystem = paymentsystem;
        this.status = status;
        this.timestamp = timestamp;
        this.transactionid = transactionid;
        this.type = type;
        this.userid = userid;
        this.username = username;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPaymentsystem() {
        return paymentsystem;
    }

    public void setPaymentsystem(String paymentsystem) {
        this.paymentsystem = paymentsystem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "UsersTransactions{" +
                "amount=" + amount +
                ", details='" + details + '\'' +
                ", paymentsystem='" + paymentsystem + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", transactionid='" + transactionid + '\'' +
                ", type='" + type + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
