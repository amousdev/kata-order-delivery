package fr.kata.order.delivery.repositories;

import fr.kata.order.delivery.entities.ServiceDeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceDeliveryRepository extends JpaRepository<ServiceDeliveryEntity, Long> {

    @Override
    Optional<ServiceDeliveryEntity> findById(Long serviceId);

    List<ServiceDeliveryEntity> findAllByStoreId(Long aLong);
}
