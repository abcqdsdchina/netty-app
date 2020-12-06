package club.avence.netty.serializable.messagepack;

public class Employee {

    private String code = "";
    private String name = "";
    private String idCard = "";

    public String getCode() {
        return code;
    }

    public Employee setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public Employee setName(String name) {
        this.name = name;
        return this;
    }

    public String getIdCard() {
        return idCard;
    }

    public Employee setIdCard(String idCard) {
        this.idCard = idCard;
        return this;
    }

}
