package fr.kata.order.delivery.services;

import fr.kata.order.delivery.exceptions.UnavailableDeliverySlotException;
import fr.kata.order.delivery.exceptions.UnavailableServiceDeliveryException;
import fr.kata.order.delivery.models.*;
import fr.kata.order.delivery.repositories.impl.DeliveryRepository;
import fr.kata.order.delivery.repositories.impl.SlotRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Captor
    ArgumentCaptor<Slot> slotCaptor;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    @Test
    public void getAvailableDeliveryMethods_should_return_values_when_services_are_enable() {
        // Given
        when(deliveryRepository.findAvailableDeliveryMethodsByStoreId(1L))
                .thenReturn(Arrays.asList(
                        buildServiceDelivery(1L, 1L, DeliveryMethod.DRIVE, TRUE),
                        buildServiceDelivery(2L, 1L, DeliveryMethod.DELIVERY, TRUE),
                        buildServiceDelivery(3L, 1L, DeliveryMethod.DELIVERY_TODAY, TRUE),
                        buildServiceDelivery(4L, 1L, DeliveryMethod.DELIVERY_ASAP, TRUE)
                ));

        // When
        List<ServiceDelivery> deliveryMethods = deliveryService.getAvailableDeliveryMethods(1L);

        // Then
        assertEquals(4, deliveryMethods.size());
        assertEquals(1L, deliveryMethods.get(0).getIdService());
        assertEquals(DeliveryMethod.DRIVE, deliveryMethods.get(0).getDeliveryMethod());
        assertEquals(2L, deliveryMethods.get(1).getIdService());
        assertEquals(DeliveryMethod.DELIVERY, deliveryMethods.get(1).getDeliveryMethod());
    }

    @Test
    public void getAvailableDeliveryMethods_should_return_emptyList_when_services_are_disable() {
        // Given
        when(deliveryRepository.findAvailableDeliveryMethodsByStoreId(5L))
                .thenReturn(Collections.emptyList());

        // When
        List<ServiceDelivery> deliveryMethods = deliveryService.getAvailableDeliveryMethods(5L);

        // Then
        assertEquals(0, deliveryMethods.size());
    }

    @Test
    public void getAvailableDeliverySlots_should_return_values_when_slots_are_availables() throws UnavailableServiceDeliveryException {
        // Given
        when(slotRepository.findAvailableDeliverySlots(1L))
                .thenReturn(Arrays.asList(
                        buildStore(1L, 5, 10, 1L, 1),
                        buildStore(2L, 3, 10, 1L, 2)
                ));
        // AND
        when(deliveryRepository.findServiceDeliveryById(1L)).thenReturn(buildServiceDelivery(1L, 2L,
                DeliveryMethod.DRIVE, TRUE));


        // When
        List<Slot> deliverySlots = deliveryService.getAvailableDeliverySlots(1L);

        // Then
        assertEquals(2, deliverySlots.size());
        assertEquals(1L, deliverySlots.get(0).getIdSlot());
        assertEquals(5, deliverySlots.get(0).getUsedCapacity());
        assertEquals(2L, deliverySlots.get(1).getIdSlot());
        assertEquals(3, deliverySlots.get(1).getUsedCapacity());
    }


    @Test
    public void getAvailableDeliverySlots_should_throws_UnvailableDeleverySlotException_when_serviceDelivery_is_not_found() {
        // Given
        when(deliveryRepository.findServiceDeliveryById(1L)).thenReturn(null);

        // When Then
        UnavailableServiceDeliveryException exception = Assertions.assertThrows(UnavailableServiceDeliveryException.class,
                () -> deliveryService.getAvailableDeliverySlots(1L));
        Assertions.assertEquals("Service Delivery not found", exception.getMessage());

    }


    @Test
    public void getAvailableDeliverySlots_should_throws_UnvailableDeleverySlotException_when_serviceDelivery_is_disabled() {
        // Given
        when(deliveryRepository.findServiceDeliveryById(1L)).thenReturn(buildServiceDelivery(1L, 2L,
                DeliveryMethod.DRIVE, FALSE));

        // When Then
        UnavailableServiceDeliveryException exception = Assertions.assertThrows(UnavailableServiceDeliveryException.class,
                () -> deliveryService.getAvailableDeliverySlots(1L));
        Assertions.assertEquals("Service Delivery disabled", exception.getMessage());

    }


    @Test
    void createDelivery_should_create_and_return_new_delivery_when_slot_is_available()
            throws UnavailableServiceDeliveryException, UnavailableDeliverySlotException {
        // Given
        when(this.slotRepository.findSlotById(1L))
                .thenReturn(Optional.of(Slot.builder().idSlot(1L)
                        .usedCapacity(5)
                        .dayCapacity(10)
                        .serviceDelivery(ServiceDelivery.builder().idService(1L).build())
                        .beginDate(OffsetDateTime.now().plusDays(1))
                        .endDate(OffsetDateTime.now().plusDays(1).plusHours(2))
                        .build())
                );
        // AND
        when(this.deliveryRepository.findServiceDeliveryById(1L))
                .thenReturn(ServiceDelivery.builder()
                        .idService(1L)
                        .store(Store.builder().idStore(1L).build())
                        .deliveryMethod(DeliveryMethod.DELIVERY)
                        .isEnable(TRUE)
                        .build()
                );
        // AND
        doNothing().when(this.slotRepository).saveSlot(any(Slot.class));

        // AND
        when(this.deliveryRepository.save(any(Delivery.class)))
                .thenReturn(Delivery.builder()
                        .id(600L)
                        .customerId(1L)
                        .orderId(1L)
                        .deliveryMethod(DeliveryMethod.DELIVERY)
                        .build());

        // When
        Delivery delivery = deliveryService.createDelivery(1L, 1L, 1L);

        // Then
        verify(this.slotRepository).saveSlot(slotCaptor.capture());

        assertEquals(6, slotCaptor.getValue().getUsedCapacity());
        assertEquals(1L, delivery.getCustomerId());
        assertEquals(1L, delivery.getOrderId());
        assertNotNull(delivery.getDeliveryMethod());
        assertEquals(600L, delivery.getId());
    }

    private Slot buildStore(Long idStore, Integer usedCapacity, Integer dayCapacity, Long idService, Integer plusDays) {
        OffsetDateTime dayDateTime = OffsetDateTime.now().plusDays(plusDays);
        return Slot.builder()
                .idSlot(idStore)
                .usedCapacity(usedCapacity)
                .dayCapacity(dayCapacity)
                .serviceDelivery(ServiceDelivery.builder().idService(idService).build())
                .beginDate(dayDateTime)
                .endDate(dayDateTime.plusHours(2))
                .build();
    }

    private ServiceDelivery buildServiceDelivery(Long idService, Long idStore, DeliveryMethod deliveryMethod, Boolean enabled) {
        return ServiceDelivery.builder()
                .idService(idService)
                .store(Store.builder().idStore(idStore).build())
                .deliveryMethod(deliveryMethod)
                .isEnable(enabled)
                .build();
    }
}