package club.avence.netty.protocal;

import lombok.Data;

@Data
public class RemoteMessageHeader {
    private Long sessionId;
    private Integer length;
    private String type;
    private Byte priority;
    private String crcCode;
}
