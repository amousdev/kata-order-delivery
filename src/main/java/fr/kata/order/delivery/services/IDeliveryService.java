package fr.kata.order.delivery.services;

import fr.kata.order.delivery.exceptions.UnavailableDeliveryException;
import fr.kata.order.delivery.exceptions.UnavailableDeliverySlotException;
import fr.kata.order.delivery.exceptions.UnavailableServiceDeliveryException;
import fr.kata.order.delivery.models.Delivery;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;

import java.util.List;

public interface IDeliveryService {

    List<ServiceDelivery> getAvailableDeliveryMethods(Long storeId);
    List<Slot> getAvailableDeliverySlots(Long serviceId) throws UnavailableServiceDeliveryException;
    Delivery createDelivery(Long customerId, Long orderId, Long slotId) throws UnavailableDeliverySlotException, UnavailableServiceDeliveryException;
    Delivery findDeliveryById(Long deliveryId) throws UnavailableDeliveryException, UnavailableDeliverySlotException;
}
