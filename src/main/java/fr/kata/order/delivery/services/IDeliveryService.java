package fr.kata.order.delivery.services;

import fr.kata.order.delivery.exceptions.UnavailableServiceDeliveryException;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;

import java.util.List;

public interface IDeliveryService {

    List<ServiceDelivery> getAvailableDeliveryMethods(Long storeId);

    List<Slot> getAvailableDeliverySlots(Long idService) throws UnavailableServiceDeliveryException;
}
