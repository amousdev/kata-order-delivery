package fr.kata.order.delivery.repositories;

import fr.kata.order.delivery.models.Delivery;
import fr.kata.order.delivery.models.ServiceDelivery;

import java.util.List;

public interface IDeliveryRepository {

    List<ServiceDelivery> findAvailableDeliveryMethodsByStoreId(Long storeId);
    ServiceDelivery findServiceDeliveryById(Long idService);
    Delivery save(Delivery delivery);
    Delivery findDeliveryById(Long deliveryId);
}
