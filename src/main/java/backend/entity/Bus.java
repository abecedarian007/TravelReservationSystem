package backend.entity;

import lombok.*;

/**
 * 大巴信息
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bus {
    private String location;
    private Integer price;
    private Integer numBus;
    private Integer numAvail;
}
