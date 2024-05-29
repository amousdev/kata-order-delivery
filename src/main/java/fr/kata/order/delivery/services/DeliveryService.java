package fr.kata.order.delivery.services;

import fr.kata.order.delivery.exceptions.UnavailableDeliverySlotException;
import fr.kata.order.delivery.exceptions.UnavailableServiceDeliveryException;
import fr.kata.order.delivery.models.Delivery;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;
import fr.kata.order.delivery.repositories.IDeliveryRepository;
import fr.kata.order.delivery.repositories.ISlotRepository;
import fr.kata.order.delivery.repositories.impl.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService implements IDeliveryService {

    private final IDeliveryRepository deliveryRepository;

    private final ISlotRepository slotRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, ISlotRepository slotRepository) {
        this.deliveryRepository = deliveryRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public List<ServiceDelivery> getAvailableDeliveryMethods(Long storeId) {
        return this.deliveryRepository.findAvailableDeliveryMethodsByStoreId(storeId);
    }

    @Override
    public List<Slot> getAvailableDeliverySlots(Long serviceId) throws UnavailableServiceDeliveryException {
        checkDeliveryServiceEnable(serviceId);
        return this.slotRepository.findAvailableDeliverySlots(serviceId);
    }

    @Override
    @Transactional
    public Delivery createDelivery(Long customerId, Long orderId, Long slotId) throws UnavailableDeliverySlotException, UnavailableServiceDeliveryException {
        Optional<Slot> optionalSlot = this.slotRepository.findSlotById(slotId);
        Slot slot = optionalSlot.orElseThrow(() -> new UnavailableDeliverySlotException("Slot not found"));
        if (slot.isAvailable()) {
            slot.bookCapacity();
        } else {
            throw new UnavailableDeliverySlotException("Slot is unavailable");
        }
        this.slotRepository.saveSlot(slot);
        Delivery delivery = initDelivery(customerId, orderId, slot);
        return this.deliveryRepository.save(delivery);
    }

    private Delivery initDelivery(Long customerId, Long orderId, Slot slot) throws UnavailableServiceDeliveryException {
        ServiceDelivery serviceDelivery = getServiceDelivery(slot.getServiceDelivery().getIdService());
        return Delivery.builder().orderId(orderId).customerId(customerId).deliveryMethod(serviceDelivery.getDeliveryMethod()).slot(slot).build();
    }

    @Override
    public Delivery findDeliveryById(Long deliveryId) {
        return this.deliveryRepository.findDeliveryById(deliveryId);
    }

    private ServiceDelivery checkDeliveryServiceEnable(Long serviceId) throws UnavailableServiceDeliveryException {
        ServiceDelivery serviceDelivery = getServiceDelivery(serviceId);
        if (!serviceDelivery.getIsEnable()) {
            throw new UnavailableServiceDeliveryException("Service Delivery disabled");
        }
        return serviceDelivery;
    }

    private ServiceDelivery getServiceDelivery(Long serviceId) throws UnavailableServiceDeliveryException {
        return Optional.ofNullable(this.deliveryRepository.findServiceDeliveryById(serviceId))
                        .orElseThrow(() -> new UnavailableServiceDeliveryException("Service Delivery not found"));
    }

}
