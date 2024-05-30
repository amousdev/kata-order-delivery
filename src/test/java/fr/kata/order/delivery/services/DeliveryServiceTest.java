package fr.kata.order.delivery.services;

import fr.kata.order.delivery.controllers.DeliveryMapper;
import fr.kata.order.delivery.entities.DeliveryEntity;
import fr.kata.order.delivery.entities.ServiceDeliveryEntity;
import fr.kata.order.delivery.entities.SlotEntity;
import fr.kata.order.delivery.exceptions.UnavailableDeliverySlotException;
import fr.kata.order.delivery.exceptions.UnavailableServiceDeliveryException;
import fr.kata.order.delivery.models.*;
import fr.kata.order.delivery.repositories.DeliveryRepository;
import fr.kata.order.delivery.repositories.ServiceDeliveryRepository;
import fr.kata.order.delivery.repositories.SlotRepository;
import fr.kata.order.delivery.services.impl.DeliveryService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {


    @Captor
    ArgumentCaptor<SlotEntity> slotCaptor;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private ServiceDeliveryRepository serviceRepository;

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    @Mock
    private DeliveryMapper deliveryMapper;


    @Test
    public void getAvailableDeliveryMethods_should_return_values_when_services_are_enable() {
        // Given
        List<ServiceDeliveryEntity> serviceDeliveryEntities = Arrays.asList(
                buildServiceDelivery(1L, 1L, DeliveryMethod.DRIVE, TRUE),
                buildServiceDelivery(2L, 1L, DeliveryMethod.DELIVERY, TRUE),
                buildServiceDelivery(3L, 1L, DeliveryMethod.DELIVERY_TODAY, TRUE),
                buildServiceDelivery(4L, 1L, DeliveryMethod.DELIVERY_ASAP, TRUE)
        );

        serviceDeliveryEntities.forEach(entity -> when(deliveryMapper.serviceEntityToDto(entity))
                .thenReturn(getServiceDeliveryDto(entity)));
        when(serviceRepository.findAllByStoreId(1L))
                .thenReturn(serviceDeliveryEntities);

        // When
        List<ServiceDelivery> deliveryMethods = deliveryService.getAvailableDeliveryMethods(1L);

        // Then
        assertEquals(4, deliveryMethods.size());
        assertEquals(1L, deliveryMethods.get(0).getIdService());
        assertEquals(DeliveryMethod.DRIVE, deliveryMethods.get(0).getDeliveryMethod());
        assertEquals(2L, deliveryMethods.get(1).getIdService());
        assertEquals(DeliveryMethod.DELIVERY, deliveryMethods.get(1).getDeliveryMethod());
    }

    private ServiceDelivery getServiceDeliveryDto(ServiceDeliveryEntity entity) {
        return ServiceDelivery.builder().idService(entity.getId())
                .deliveryMethod(entity.getDeliveryMethod())
                .isEnable(entity.getIsEnable())
                .store(Store.builder().idStore(entity.getStoreId()).build())
                .build();
    }

    @Test
    public void getAvailableDeliveryMethods_should_return_emptyList_when_services_are_disable() {
        // Given
        when(serviceRepository.findAllByStoreId(5L))
                .thenReturn(Collections.emptyList());

        // When
        List<ServiceDelivery> deliveryMethods = deliveryService.getAvailableDeliveryMethods(5L);

        // Then
        assertEquals(0, deliveryMethods.size());
    }

    @Test
    public void getAvailableDeliverySlots_should_return_values_when_slots_are_availables() throws UnavailableServiceDeliveryException {
       // Given
        List<SlotEntity> slotEntities = Arrays.asList(
                buildSlot(1L, 5, 10, 1L, 1),
                buildSlot(2L, 3, 10, 1L, 2)
        );
        when(slotRepository.findAllByServiceId(1L))
               .thenReturn(slotEntities);
        slotEntities.forEach(
                entity -> when(deliveryMapper.slotEntityToDto(entity)).thenReturn(getSlot(entity))
        );
        // AND
        ServiceDeliveryEntity serviceDelivery = buildServiceDelivery(1L, 2L, DeliveryMethod.DRIVE, TRUE);
        when(serviceRepository.findById(1L))
               .thenReturn(Optional.of(serviceDelivery));

       when(deliveryMapper.serviceEntityToDto(serviceDelivery))
               .thenReturn(getServiceDeliveryDto(serviceDelivery));

       // When
       List<Slot> deliverySlots = deliveryService.getAvailableDeliverySlots(1L);

       // Then
       assertEquals(2, deliverySlots.size());
       assertEquals(1L, deliverySlots.get(0).getIdSlot());
       assertEquals(5, deliverySlots.get(0).getUsedCapacity());
       assertEquals(2L, deliverySlots.get(1).getIdSlot());
       assertEquals(3, deliverySlots.get(1).getUsedCapacity());
    }

    private Slot getSlot(SlotEntity entity) {
        return Slot.builder().idSlot(entity.getId())
                .serviceDelivery(ServiceDelivery.builder().idService(entity.getServiceId()).build())
                .beginDate(entity.getBeginDate())
                .endDate(entity.getEndDate())
                .usedCapacity(entity.getUsedCapacity())
                .dayCapacity(entity.getDayCapacity())
                .build();
    }


    @Test
    public void getAvailableDeliverySlots_should_throws_UnvailableDeleverySlotException_when_serviceDelivery_is_not_found() {
       // Given
       when(serviceRepository.findById(1L)).thenReturn(Optional.empty());

       // When Then
       UnavailableServiceDeliveryException exception = assertThrows(UnavailableServiceDeliveryException.class,
               () -> deliveryService.getAvailableDeliverySlots(1L));
       assertEquals("Service Delivery not found", exception.getMessage());

    }


    @Test
    public void getAvailableDeliverySlots_should_throws_UnvailableDeleverySlotException_when_serviceDelivery_is_disabled() {
       // Given
        ServiceDeliveryEntity serviceEntity = buildServiceDelivery(1L, 2L, DeliveryMethod.DRIVE, FALSE);
        when(deliveryMapper.serviceEntityToDto(serviceEntity))
                .thenReturn(getServiceDeliveryDto(serviceEntity));
        when(serviceRepository.findById(1L))
                .thenReturn(Optional.of(serviceEntity));

       // When Then
       UnavailableServiceDeliveryException exception = Assertions.assertThrows(UnavailableServiceDeliveryException.class,
               () -> deliveryService.getAvailableDeliverySlots(1L));
       Assertions.assertEquals("Service Delivery disabled", exception.getMessage());

    }

    @Test
    void createDelivery_should_create_and_return_new_delivery_when_slot_is_available()
           throws UnavailableServiceDeliveryException, UnavailableDeliverySlotException {
        // Given
        SlotEntity slotEntity = SlotEntity.builder()
                .id(1L)
                .usedCapacity(5)
                .dayCapacity(10)
                .serviceId(1L)
                .beginDate(OffsetDateTime.now().plusDays(1))
                .endDate(OffsetDateTime.now().plusDays(1).plusHours(2))
                .build();
        when(this.slotRepository.findById(1L))
               .thenReturn(Optional.of(slotEntity));
        Slot slot = getSlot(slotEntity);
        when(this.deliveryMapper.slotEntityToDto(slotEntity)).thenReturn(slot);
        when(this.deliveryMapper.slotDtoToEntity(slot)).thenReturn(getSlotEntity(slot));
        // AND
        ServiceDeliveryEntity serviceDeliveryEntity = ServiceDeliveryEntity.builder()
                .id(1L)
                .storeId(1L)
                .deliveryMethod(DeliveryMethod.DELIVERY)
                .isEnable(TRUE)
                .build();
        when(this.serviceRepository.findById(1L))
               .thenReturn(Optional.of(serviceDeliveryEntity));
        when(this.deliveryMapper.serviceEntityToDto(serviceDeliveryEntity))
                .thenReturn(getServiceDeliveryDto(serviceDeliveryEntity));
        // AND
        when(this.slotRepository.save(slotEntity)).thenReturn(slotEntity);

        // AND
        DeliveryEntity deliveryEntity = DeliveryEntity.builder()
                .id(null)
                .customerId(1L)
                .orderId(1L)
                .slotId(1L)
                .deliveryMethod(DeliveryMethod.DELIVERY)
                .build();
        deliveryEntity.setId(600L);
        Delivery deliveryDto = getDeliveryDto(deliveryEntity);
        when(deliveryMapper.deliveryEntityToDto(deliveryEntity))
                .thenReturn(deliveryDto);
        when(this.deliveryRepository.save(any(DeliveryEntity.class)))
                .thenReturn(deliveryEntity);

        // When
        Delivery delivery = deliveryService.createDelivery(1L, 1L, 1L);

        // Then
        verify(this.slotRepository).save(slotCaptor.capture());

        assertEquals(5, slotCaptor.getValue().getUsedCapacity());
        assertEquals(1L, delivery.getCustomerId());
        assertEquals(1L, delivery.getOrderId());
        assertNotNull(delivery.getDeliveryMethod());
        assertEquals(600L, delivery.getId());
    }

    private Delivery getDeliveryDto(DeliveryEntity deliveryEntity) {
        return Delivery.builder()
                .id(deliveryEntity.getId())
                .orderId(deliveryEntity.getOrderId())
                .customerId(deliveryEntity.getCustomerId())
                .creationDate(deliveryEntity.getCreationDate())
                .deliveryMethod(deliveryEntity.getDeliveryMethod())
                .slot(Slot.builder().idSlot(deliveryEntity.getSlotId()).build())
                .build();
    }

    private SlotEntity getSlotEntity(Slot slot) {
        return SlotEntity.builder()
                .id(slot.getIdSlot())
                .serviceId(slot.getIdSlot())
                .beginDate(slot.getBeginDate())
                .endDate(slot.getEndDate())
                .dayCapacity(slot.getDayCapacity())
                .usedCapacity(slot.getUsedCapacity())
                .build();
    }

    private SlotEntity buildSlot(Long slotId, Integer usedCapacity, Integer dayCapacity, Long idService,
                                 Integer plusDays) {
        OffsetDateTime dayDateTime = OffsetDateTime.now().plusDays(plusDays);
        return SlotEntity.builder()
                .id(slotId)
                .usedCapacity(usedCapacity)
                .dayCapacity(dayCapacity)
                .serviceId(idService)
                .beginDate(dayDateTime)
                .endDate(dayDateTime.plusHours(2))
                .build();
    }

    private ServiceDeliveryEntity buildServiceDelivery(Long idService, Long idStore, DeliveryMethod deliveryMethod,
                                                       Boolean enabled) {
        return ServiceDeliveryEntity.builder()
                .id(idService)
                .storeId(idStore)
                .deliveryMethod(deliveryMethod)
                .isEnable(enabled)
                .build();
    }
}