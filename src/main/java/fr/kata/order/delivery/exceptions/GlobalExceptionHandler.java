package fr.kata.order.delivery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UnavailableDeliverySlotException.class})
    public ResponseEntity<Object> handleUnavailableDeliverySlotException(UnavailableDeliverySlotException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({UnavailableServiceDeliveryException.class})
    public ResponseEntity<Object> handleUnavailableDeliverySlotException(UnavailableServiceDeliveryException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}

