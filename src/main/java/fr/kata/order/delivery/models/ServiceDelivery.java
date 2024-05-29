package fr.kata.order.delivery.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServiceDelivery {

    private Long idService;
    private Long idStore;
    private DeliveryMethodEnum deliveryMethod;
    private Boolean isEnable;
}
