package fr.kata.order.delivery.repositories;

import fr.kata.order.delivery.models.DeliveryMethodEnum;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public class DeliveryRepository {

    public List<ServiceDelivery> findAvailableDeliveryMethodsByStoreId(Long storeId) {
        return Arrays.asList(
                ServiceDelivery.builder().idService(1L).idStore(storeId).deliveryMethod(DeliveryMethodEnum.DRIVE).isEnable(true).build(),
                ServiceDelivery.builder().idService(1L).idStore(storeId).deliveryMethod(DeliveryMethodEnum.DELIVERY).isEnable(true).build()
        );
    }

    public ServiceDelivery findServiceDeliveryById(Long idService) {
        return ServiceDelivery.builder().idService(idService).idStore(3L).deliveryMethod(DeliveryMethodEnum.DRIVE).isEnable(true).build();
    }

    public List<Slot> findAvailableDeliverySlots(Long idService) {
        return Arrays.asList(
                Slot.builder().idSlot(1L).usedCapacity(5).dayCapacity(10).idService(idService).beginDate(OffsetDateTime.now().plusDays(1)).endDate(OffsetDateTime.now().plusDays(1).plusHours(2)).build(),
                Slot.builder().idSlot(2L).usedCapacity(3).dayCapacity(10).idService(idService).beginDate(OffsetDateTime.now().plusDays(2)).endDate(OffsetDateTime.now().plusDays(2).plusHours(2)).build()
        );
    }
}
