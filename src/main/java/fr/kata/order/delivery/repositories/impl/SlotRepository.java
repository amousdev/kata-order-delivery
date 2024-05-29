package fr.kata.order.delivery.repositories.impl;

import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;
import fr.kata.order.delivery.repositories.ISlotRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.TRUE;

@Repository
public class SlotRepository implements ISlotRepository {
    @Override
    public List<Slot> findAvailableDeliverySlots(Long serviceId) {
        return Arrays.asList(
                Slot.builder().idSlot(1L).usedCapacity(5).dayCapacity(10)
                        .serviceDelivery(ServiceDelivery.builder().idService(serviceId).isEnable(TRUE).build())
                        .beginDate(OffsetDateTime.now().plusDays(1))
                        .endDate(OffsetDateTime.now().plusDays(1).plusHours(2))
                        .build(),
                Slot.builder().idSlot(2L).usedCapacity(3).dayCapacity(10)
                        .serviceDelivery(ServiceDelivery.builder().idService(serviceId).isEnable(TRUE).build())
                        .beginDate(OffsetDateTime.now().plusDays(2))
                        .endDate(OffsetDateTime.now().plusDays(2).plusHours(2))
                        .build()
        );
    }

    @Override
    public Optional<Slot> findSlotById(Long slotId) {
        return Optional.of(
                Slot.builder().idSlot(1L).usedCapacity(5).dayCapacity(10).serviceDelivery(ServiceDelivery.builder().idService(5L)
                        .build()).beginDate(OffsetDateTime.now().plusDays(1)).endDate(OffsetDateTime.now().plusDays(1).plusHours(2)).build()
        );
    }

    @Override
    public void saveSlot(Slot slot) {

    }
}
