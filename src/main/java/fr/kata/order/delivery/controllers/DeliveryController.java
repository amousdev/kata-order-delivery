package fr.kata.order.delivery.controllers;

import fr.kata.order.delivery.exceptions.UnavailableDeliverySlotException;
import fr.kata.order.delivery.exceptions.UnavailableServiceDeliveryException;
import fr.kata.order.delivery.models.Delivery;
import fr.kata.order.delivery.models.DeliveryMethod;
import fr.kata.order.delivery.models.ServiceDelivery;
import fr.kata.order.delivery.models.Slot;
import fr.kata.order.delivery.services.IDeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveries")
@Tag(name = "Deliveries", description = "Endpoint to create delivery")
@Slf4j
@RequiredArgsConstructor
public class DeliveryController {

    private final IDeliveryService deliveryService;

    @GetMapping("/{storeId}/methodes")
    @Operation(summary = "Get availible delivery methods",
            description = "Get availible delivery methods for store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Delivery not found", content = @Content),
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(implementation = DeliveryMethod.class)))})
    public ResponseEntity<List<ServiceDelivery>> getAvailableDeliveryMethods(@PathVariable Long storeId) {
        List<ServiceDelivery> deliveryMethods = deliveryService.getAvailableDeliveryMethods(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(deliveryMethods);
    }

    @GetMapping("/services/{serviceId}/slots")
    @Operation(summary = "Get availibles slots",
            description = "Get availibles slots for service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Delivery not found", content = @Content),
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(implementation = Slot.class)))})
    public ResponseEntity<List<Slot>> getAvailableDeliverySlots(@PathVariable Long serviceId) throws UnavailableServiceDeliveryException {
        List<Slot> slots = deliveryService.getAvailableDeliverySlots(serviceId);
        return ResponseEntity.status(HttpStatus.OK).body(slots);
    }

    @PostMapping
    @Operation(summary = "Create a new delivery",
            description = "Create a new delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
            @ApiResponse(responseCode = "201", description = "Delivery has added successfuly",
                    content = @Content(schema = @Schema(implementation = Delivery.class)))
    })
    public ResponseEntity<Delivery> createDelivery(@Valid @RequestBody Delivery delivery) throws UnavailableServiceDeliveryException, UnavailableDeliverySlotException {
        Delivery createdDelivery = deliveryService.createDelivery(delivery.getCustomerId(), delivery.getOrderId(), delivery.getSlot().getIdSlot());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDelivery);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get delivery by id",
            description = "Get delivery by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Delivery not found", content = @Content),
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(implementation = Delivery.class)))
    })
    ResponseEntity<Delivery> getDeliveryById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(deliveryService.findDeliveryById(id));
    }


}

