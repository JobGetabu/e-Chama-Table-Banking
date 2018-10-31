package app.hacela.chamatablebanking.adapter;


public class DummyTest {
    private String name;
    private String phone;
    private String amount;
    private String owing;
    private int photo;

    public DummyTest(String name, String phone, String amount, String owing, int photo) {
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.owing = owing;
        this.photo =photo;
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

    public int getPhoto() { return photo; }
}
