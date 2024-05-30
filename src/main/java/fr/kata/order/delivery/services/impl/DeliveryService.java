package fr.kata.order.delivery.services.impl;

import fr.kata.order.delivery.controllers.DeliveryMapper;
import fr.kata.order.delivery.entities.DeliveryEntity;
import fr.kata.order.delivery.entities.ServiceDeliveryEntity;
import fr.kata.order.delivery.entities.SlotEntity;
import fr.kata.order.delivery.exceptions.UnavailableDeliveryException;
import fr.kata.order.delivery.exceptions.UnavailableDeliverySlotException;
import fr.kata.order.delivery.exceptions.UnavailableServiceDeliveryException;
import fr.kata.order.delivery.models.Delivery;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;
import fr.kata.order.delivery.repositories.DeliveryRepository;
import fr.kata.order.delivery.repositories.SlotRepository;
import fr.kata.order.delivery.repositories.ServiceDeliveryRepository;
import fr.kata.order.delivery.services.IDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService implements IDeliveryService {

    private final DeliveryMapper deliveryMapper;

    private final DeliveryRepository repo;

    private final ServiceDeliveryRepository serviceDeliveryRepository;

    private final SlotRepository slotRepository;

    @Autowired
    public DeliveryService(
                           DeliveryMapper deliveryMapper,
                           DeliveryRepository deliveryRepository,
                           ServiceDeliveryRepository serviceDeliveryRepository,
                           SlotRepository slotRepository) {
        this.deliveryMapper = deliveryMapper;
        this.repo = deliveryRepository;
        this.serviceDeliveryRepository = serviceDeliveryRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public List<ServiceDelivery> getAvailableDeliveryMethods(Long storeId) {
        List<ServiceDeliveryEntity> serviceEntities = this.serviceDeliveryRepository.findAllByStoreId(storeId);
        return serviceEntities.stream()
                .map(this.deliveryMapper::serviceEntityToDto)
                .filter(ServiceDelivery::getIsEnable)
                .collect(Collectors.toList());
    }

    @Override
    public List<Slot> getAvailableDeliverySlots(Long serviceId) throws UnavailableServiceDeliveryException {
        checkDeliveryServiceEnable(serviceId);
        return this.slotRepository.findAllByServiceId(serviceId).stream()
                .map(this.deliveryMapper::slotEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Delivery createDelivery(Long customerId, Long orderId, Long slotId) throws UnavailableDeliverySlotException, UnavailableServiceDeliveryException {
        Slot slot = getSlot(slotId);
        if (slot.isAvailable()) {
            slot.bookCapacity();
        } else {
            throw new UnavailableDeliverySlotException("Slot is unavailable");
        }
        this.slotRepository.save(this.deliveryMapper.slotDtoToEntity(slot));
        DeliveryEntity delivery = initDelivery(customerId, orderId, slot);
        DeliveryEntity deliveryEntity = this.repo.save(delivery);
        return deliveryMapper.deliveryEntityToDto(deliveryEntity);
    }

    private Slot getSlot(Long slotId) throws UnavailableDeliverySlotException {
        SlotEntity slotEntity = this.slotRepository.findById(slotId).orElseThrow(() -> new UnavailableDeliverySlotException("Slot not found"));
        return this.deliveryMapper.slotEntityToDto(slotEntity);
    }

    @Override
    public Delivery findDeliveryById(Long deliveryId) throws UnavailableDeliveryException, UnavailableDeliverySlotException {
        DeliveryEntity entity = this.repo.findById(deliveryId)
                .orElseThrow(() -> new UnavailableDeliveryException("Delivery not found"));
        Delivery delivery = deliveryMapper.deliveryEntityToDto(entity);
        delivery.setSlot(getSlot(delivery.getSlotId()));
        return delivery;
    }

    public DeliveryEntity initDelivery(Long customerId, Long orderId, Slot slot) throws UnavailableServiceDeliveryException {
        ServiceDelivery serviceDelivery = getServiceDelivery(slot.getServiceDelivery().getIdService());
        return DeliveryEntity.builder()
                .orderId(orderId)
                .customerId(customerId)
                .deliveryMethod(serviceDelivery.getDeliveryMethod())
                .slotId(slot.getIdSlot())
                .creationDate(OffsetDateTime.now())
                .build();
    }

    private void checkDeliveryServiceEnable(Long serviceId) throws UnavailableServiceDeliveryException {
        ServiceDelivery serviceDelivery = getServiceDelivery(serviceId);
        if (!serviceDelivery.getIsEnable()) {
            throw new UnavailableServiceDeliveryException("Service Delivery disabled");
        }
    }

    private ServiceDelivery getServiceDelivery(Long serviceId) throws UnavailableServiceDeliveryException {
        ServiceDeliveryEntity serviceEntity = this.serviceDeliveryRepository.findById(serviceId)
                .orElseThrow(() -> new UnavailableServiceDeliveryException("Service Delivery not found"));
        return deliveryMapper.serviceEntityToDto(serviceEntity);
    }

}
