package fr.kata.order.delivery.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ServiceDelivery {

    private Long idService;
    private Store store;
    private DeliveryMethod deliveryMethod;
    private Boolean isEnable;
    @JsonIgnore
    private List<Slot> slots;


}
