package fr.kata.order.delivery.repositories;

import fr.kata.order.delivery.entities.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {

    @Override
    Optional<DeliveryEntity> findById(Long deliveryId);

    @Override
    DeliveryEntity save(DeliveryEntity dto);
}
