package fr.kata.order.delivery.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Delivery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long customerId;
    private Long orderId;
    private DeliveryMethod deliveryMethod;
    private Slot slot;
    private OffsetDateTime creationDate;

    public Long getSlotId() {
        if (this.slot != null) {
            return this.slot.getIdSlot();
        }
        return null;
    }
}
