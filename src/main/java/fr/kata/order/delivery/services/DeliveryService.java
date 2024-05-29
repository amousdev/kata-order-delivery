package fr.kata.order.delivery.services;

import fr.kata.order.delivery.exceptions.UnavailableDeliverySlotException;
import fr.kata.order.delivery.exceptions.UnavailableServiceDeliveryException;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;
import fr.kata.order.delivery.repositories.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService implements IDeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public List<ServiceDelivery> getAvailableDeliveryMethods(Long storeId) {
        return this.deliveryRepository.findAvailableDeliveryMethodsByStoreId(storeId);
    }

    @Override
    public List<Slot> getAvailableDeliverySlots(Long idService) throws UnavailableServiceDeliveryException {
        ServiceDelivery serviceDelivery =
                Optional.ofNullable(this.deliveryRepository.findServiceDeliveryById(idService))
                        .orElseThrow(() -> new UnavailableServiceDeliveryException("Service Delivery not found"));
        if (!serviceDelivery.getIsEnable()) {
            throw new UnavailableServiceDeliveryException("Service Delivery disabled");
        }
        return this.deliveryRepository.findAvailableDeliverySlots(1L);
    }

}
