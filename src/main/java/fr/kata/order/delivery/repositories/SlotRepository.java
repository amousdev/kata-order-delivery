package fr.kata.order.delivery.repositories;

import fr.kata.order.delivery.entities.SlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<SlotEntity, Long> {

    @Override
    Optional<SlotEntity> findById(Long aLong);

    List<SlotEntity> findAllByServiceId(Long serviceId);

    @Override
    <S extends SlotEntity> S save(S entity);
}
