package fr.kata.order.delivery.models;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Builder
public class Slot {

    private Long idSlot;
    private Integer usedCapacity;
    private Integer dayCapacity;
    private ServiceDelivery serviceDelivery;
    private OffsetDateTime beginDate;
    private OffsetDateTime endDate;

    public boolean isAvailable() {
        return Objects.nonNull(usedCapacity) && Objects.nonNull(dayCapacity) && usedCapacity < dayCapacity;
    }

    public Slot bookCapacity() {
        if(isAvailable()) {
            usedCapacity ++;
        }
        return this;
    }
}
