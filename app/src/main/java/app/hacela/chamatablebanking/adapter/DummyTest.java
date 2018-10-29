package app.hacela.chamatablebanking.adapter;


public class DummyTest {
    private String name;
    private String phone;
    private String amount;
    private String owing;

    public DummyTest(String name, String phone, String amount, String owing) {
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.owing = owing;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAmount() {
        return amount;
    }

    public String getOwing() {
        return owing;
    }
}
