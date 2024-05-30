package fr.kata.order.delivery.handlers;

import fr.kata.order.delivery.exceptions.UnavailableDeliveryException;
import fr.kata.order.delivery.exceptions.UnavailableDeliverySlotException;
import fr.kata.order.delivery.exceptions.UnavailableServiceDeliveryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UnavailableDeliverySlotException.class, UnavailableDeliveryException.class})
    public ResponseEntity<String> handleUnavailableDeliverySlotException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND.value())
                .body(exception.getMessage());
    }

    @ExceptionHandler({UnavailableServiceDeliveryException.class})
    public ResponseEntity<String> handleUnavailableDeliverySlotException(UnavailableDeliveryException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND.value())
                .body(exception.getMessage());
    }
}

