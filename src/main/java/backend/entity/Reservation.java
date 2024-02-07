package backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预定信息
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Integer resvID;
    private String custName;
    private ObjectType resvType;
    private String resvKey;
}
