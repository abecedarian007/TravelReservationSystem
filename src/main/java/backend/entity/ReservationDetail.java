package backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 预定信息
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetail extends Reservation{
    private Integer quantity;
    private Integer availability;
    private String locationFrom;
    private String locationTo;
    private Integer price;

    public ReservationDetail(Integer resvID, String custName, ObjectType resvType, String resvKey,
                             Integer quantity, Integer availability, String locationFrom, String locationTo, Integer price) {
        super(resvID, custName, resvType, resvKey);
        this.quantity = quantity;
        this.availability = availability;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.price = price;
    }
}
