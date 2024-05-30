package fr.kata.order.delivery.controllers;

import fr.kata.order.delivery.entities.DeliveryEntity;
import fr.kata.order.delivery.entities.ServiceDeliveryEntity;
import fr.kata.order.delivery.entities.SlotEntity;
import fr.kata.order.delivery.models.Delivery;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;
import fr.kata.order.delivery.services.impl.UtilService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DeliveryMapper {

    @Autowired
    protected UtilService utilService;

    @Mapping(target = "slot", expression = "java(utilService.buildSlot(entity.getSlotId()))")
    public abstract Delivery deliveryEntityToDto(DeliveryEntity entity);

    @Mapping(target = "store", expression = "java(utilService.buildStore(entity.getStoreId()))")
    @Mapping(target = "idService", source = "id")
    public abstract ServiceDelivery serviceEntityToDto(ServiceDeliveryEntity entity);

    @Mapping(target = "serviceDelivery", expression = "java(utilService.buildServiceDelivery(entity.getServiceId()))")
    @Mapping(target = "idSlot", source = "id")
    public abstract Slot slotEntityToDto(SlotEntity entity);

    @Mapping(target = "serviceId", expression = "java(utilService.getServiceId(source))")
    @Mapping(target = "id", source = "idSlot")
    public abstract SlotEntity slotDtoToEntity(Slot source);
}
