package fr.kata.order.delivery.repositories.impl;

import fr.kata.order.delivery.models.*;
import fr.kata.order.delivery.repositories.IDeliveryRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public class DeliveryRepository implements IDeliveryRepository {

    @Override
    public List<ServiceDelivery> findAvailableDeliveryMethodsByStoreId(Long storeId) {
        return Arrays.asList(
                ServiceDelivery.builder().idService(1L)
                        .store(Store.builder().idStore(storeId).build()).deliveryMethod(DeliveryMethod.DRIVE).isEnable(true).build(),
                ServiceDelivery.builder().idService(2L)
                        .store(Store.builder().idStore(storeId).build()).deliveryMethod(DeliveryMethod.DELIVERY).isEnable(true).build()
        );
    }

    @Override
    public ServiceDelivery findServiceDeliveryById(Long idService) {
        return ServiceDelivery.builder()
                .idService(idService)
                .store(Store.builder().idStore(3L).build())
                .deliveryMethod(DeliveryMethod.DRIVE)
                .isEnable(true)
                .build();
    }

    @Override
    public Delivery save(Delivery delivery) {
        return Delivery.builder()
                .id(100L)
                .customerId(delivery.getCustomerId())
                .orderId(delivery.getOrderId())
                .slot(
                        Slot.builder().idSlot(delivery.getSlot().getIdSlot())
                                .dayCapacity(5).usedCapacity(1).beginDate(delivery.getSlot().getBeginDate()).endDate(delivery.getSlot().getEndDate()).build()
                )
                .deliveryMethod(delivery.getDeliveryMethod())

                .build();
    }

    @Override
    public Delivery findDeliveryById(Long deliveryId) {
        return Delivery.builder()
                .id(deliveryId)
                .customerId(10333999L)
                .orderId(11110333L)
                .slot(
                        Slot.builder().idSlot(8L)
                                .dayCapacity(5).usedCapacity(1).beginDate(OffsetDateTime.now().plusDays(2)).endDate(OffsetDateTime.now().plusDays(2).plusHours(2)).build()
                )
                .deliveryMethod(DeliveryMethod.DELIVERY)
                .build();    }

}
