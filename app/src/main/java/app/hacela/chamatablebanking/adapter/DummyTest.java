package app.hacela.chamatablebanking.adapter;


public class DummyTest {
    private String name;
    private String phone, phone_desc;
    private String amount, amount_desc;
    private String owing, contribs_desc;
    private int photo;


    public DummyTest(String name, String phone, String amount, String owing, int photo, String phone_desc, String amount_desc, String contribs_desc) {
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.owing = owing;
        this.photo =photo;
        this.phone_desc = phone_desc;
        this.amount_desc = amount_desc;
        this.contribs_desc = contribs_desc;
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

    public String getPhone_desc() {
        return phone_desc;
    }

    public String getAmount_desc() {
        return amount_desc;
    }

    public String getContribs_desc() {
        return contribs_desc;
    }
}
