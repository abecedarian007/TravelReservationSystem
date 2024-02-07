package backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 航班信息
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    private String flightNum;
    private Integer price;
    private Integer numSeats;
    private Integer numAvail;
    private String fromCity;
    private String arivCity;
}
