package fr.kata.order.delivery.exceptions;

public class UnavailableDeliverySlotException extends Exception {

    public UnavailableDeliverySlotException(String message) {
        super(message);
    }
}
