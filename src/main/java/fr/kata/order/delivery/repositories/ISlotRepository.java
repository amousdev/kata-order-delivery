package fr.kata.order.delivery.repositories;

import fr.kata.order.delivery.models.Slot;

import java.util.List;
import java.util.Optional;

public interface ISlotRepository {
    List<Slot> findAvailableDeliverySlots(Long serviceId);
    Optional<Slot> findSlotById(Long slotId);
    void saveSlot(Slot slot);
}
