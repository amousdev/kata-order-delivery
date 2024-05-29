package fr.kata.order.delivery.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Store {

    private Long idStore;
    private String name;
}
