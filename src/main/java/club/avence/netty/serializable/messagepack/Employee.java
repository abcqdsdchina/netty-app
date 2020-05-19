package club.avence.netty.serializable.messagepack;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class Employee {
    private String code = "";
    private String name = "";
    private String idCard = "";
}
