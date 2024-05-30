package fr.kata.order.delivery.services.impl;

import fr.kata.order.delivery.models.Delivery;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;
import fr.kata.order.delivery.models.Store;
import org.springframework.stereotype.Service;

@Service
public class UtilService {

    public Long getSlotId(Delivery delivery) {
        if (delivery != null && delivery.getSlot() != null) {
            return delivery.getSlot().getIdSlot();
        }
        return null;
    }

    public Slot buildSlot(Long slotId) {
        return Slot.builder().idSlot(slotId).build();
    }

    public Store buildStore(Long storeId) {
        return Store.builder().idStore(storeId).build();
    }

    public ServiceDelivery buildServiceDelivery(Long serviceId) {
        return ServiceDelivery.builder().idService(serviceId).build();
    }

    public Long getServiceId(Slot slot) {
        if (slot != null && slot.getServiceDelivery() != null) {
            return slot.getServiceDelivery().getIdService();
        }
        return null;
    }
}
