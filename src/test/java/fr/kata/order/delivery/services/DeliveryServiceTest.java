package fr.kata.order.delivery.services;

import fr.kata.order.delivery.exceptions.UnvailableDeleverySlotException;
import fr.kata.order.delivery.exceptions.UnvailableServiceDeleveryException;
import fr.kata.order.delivery.models.DeliveryMethodEnum;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;
import fr.kata.order.delivery.repositories.DeliveryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    @Test
    public void getAvailableDeliveryMethods_should_return_values_when_services_are_enable() {
        // Given
        when(deliveryRepository.findAvailableDeliveryMethodsByStoreId(1L))
                .thenReturn(Arrays.asList(
                        buildServiceDelivery(1L, 1L, DeliveryMethodEnum.DRIVE, TRUE),
                        buildServiceDelivery(2L, 1L, DeliveryMethodEnum.DELIVERY, TRUE),
                        buildServiceDelivery(3L, 1L, DeliveryMethodEnum.DELIVERY_TODAY, FALSE),
                        buildServiceDelivery(4L, 1L, DeliveryMethodEnum.DELIVERY_ASAP, FALSE)
                ));

        // When
        List<ServiceDelivery> deliveryMethods = deliveryService.getAvailableDeliveryMethods(1L);

        // Then
        assertEquals(2, deliveryMethods.size());
        assertEquals(1L, deliveryMethods.get(0).getIdService());
        assertEquals(DeliveryMethodEnum.DRIVE, deliveryMethods.get(0).getDeliveryMethod());
        assertEquals(2L, deliveryMethods.get(1).getIdService());
        assertEquals(DeliveryMethodEnum.DELIVERY, deliveryMethods.get(1).getDeliveryMethod());
    }

    @Test
    public void getAvailableDeliveryMethods_should_return_emptyList_when_services_are_disable() {
        // Given
        when(deliveryRepository.findAvailableDeliveryMethodsByStoreId(5L))
                .thenReturn(Arrays.asList(
                        buildServiceDelivery(10L, 5L, DeliveryMethodEnum.DRIVE, FALSE),
                        buildServiceDelivery(11L, 5L, DeliveryMethodEnum.DELIVERY_ASAP, FALSE),
                        buildServiceDelivery(12L, 5L, DeliveryMethodEnum.DELIVERY_TODAY, FALSE)
                ));

        // When
        List<ServiceDelivery> deliveryMethods = deliveryService.getAvailableDeliveryMethods(5L);

        // Then
        assertEquals(0, deliveryMethods.size());
    }

    @Test
    public void getAvailableDeliverySlots_should_return_values_when_slots_are_availables() throws UnvailableDeleverySlotException, UnvailableServiceDeleveryException {
        // Given
        when(deliveryRepository.findAvailableDeliverySlots(1L))
                .thenReturn(Arrays.asList(
                        buildStore(1L, 5, 10, 1L, 1),
                        buildStore(2L, 3, 10, 1L, 2)
                ));

        // When
        List<Slot> deliverySlots = deliveryService.getAvailableDeliverySlots(1L);

        // Then
        assertEquals(2, deliverySlots.size());
        assertEquals(1L, deliverySlots.get(0).getIdSlot());
        assertEquals(5, deliverySlots.get(0).getUsedCapacity());
        assertEquals(1L, deliverySlots.get(1).getIdSlot());
        assertEquals(3, deliverySlots.get(1).getUsedCapacity());
    }


    @Test
    public void getAvailableDeliverySlots_should_throws_UnvailableDeleverySlotException_when_serviceDelivery_is_disabled() {
        // Given
        when(deliveryRepository.findAvailableDeliverySlots(1L))
                .thenReturn(Arrays.asList(
                        buildStore(1L, 5, 10, 1L, 1),
                        buildStore(2L, 3, 10, 1L, 2)
                ));

        when(deliveryRepository.findServiceDeliveryById(1L)).thenReturn(buildServiceDelivery(1L, 2L,
                DeliveryMethodEnum.DRIVE, FALSE));

        // When Then
        UnvailableServiceDeleveryException exception = Assertions.assertThrows(UnvailableServiceDeleveryException.class,
                () -> deliveryService.getAvailableDeliverySlots(1L));
        Assertions.assertEquals("Service Delevery is unvailable", exception.getMessage());

    }


    private Slot buildStore(Long idStore, Integer usedCapacity, Integer dayCapacity, Long idService, Integer plusDays) {
        OffsetDateTime dayDateTime = OffsetDateTime.now().plusDays(plusDays);
        return Slot.builder()
                .idSlot(idStore)
                .usedCapacity(usedCapacity)
                .dayCapacity(dayCapacity)
                .idService(idService)
                .beginDate(dayDateTime)
                .endDate(dayDateTime.plusHours(2))
                .build();
    }

    private ServiceDelivery buildServiceDelivery(Long idService, Long idStore, DeliveryMethodEnum deliveryMethod, Boolean enabled) {
        return ServiceDelivery.builder()
                .idService(idService)
                .idStore(idStore)
                .deliveryMethod(deliveryMethod)
                .enable(enabled)
                .build();
    }

}