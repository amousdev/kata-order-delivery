package fr.kata.order.delivery.models;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class Slot {

    private Long idSlot;
    private Integer usedCapacity;
    private Integer dayCapacity;
    private Long idService;
    private OffsetDateTime beginDate;
    private OffsetDateTime endDate;
}
