package backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String custName;
    private Integer custID;
}
