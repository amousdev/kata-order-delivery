package fr.kata.order.delivery.services;

import fr.kata.order.delivery.exceptions.UnvailableServiceDeleveryException;
import fr.kata.order.delivery.exceptions.UnvailableDeleverySlotException;
import fr.kata.order.delivery.models.DeliveryMethodEnum;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;

import java.util.List;

public interface IDeliveryService {

    List<ServiceDelivery> getAvailableDeliveryMethods(Long storeId);

    List<Slot> getAvailableDeliverySlots(Long idService)
            throws UnvailableDeleverySlotException, UnvailableServiceDeleveryException;
}
