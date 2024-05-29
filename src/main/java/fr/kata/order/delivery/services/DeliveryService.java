package fr.kata.order.delivery.services;

import fr.kata.order.delivery.exceptions.UnvailableDeleverySlotException;
import fr.kata.order.delivery.exceptions.UnvailableServiceDeleveryException;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;
import fr.kata.order.delivery.repositories.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService implements IDeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public List<ServiceDelivery> getAvailableDeliveryMethods(Long storeId) {
        return null;
    }

    @Override
    public List<Slot> getAvailableDeliverySlots(Long idService) throws UnvailableDeleverySlotException,
            UnvailableServiceDeleveryException {
        return null;
    }

}
