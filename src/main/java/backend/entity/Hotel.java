package backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 酒店信息
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    private String location;
    private Integer price;
    private Integer numRooms;
    private Integer numAvail;
}
