package fr.kata.order.delivery.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Table(name = "slots")
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class SlotEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "used_capacity", nullable = false)
    private Integer usedCapacity;

    @Column(name = "day_capacity", nullable = false)
    private Integer dayCapacity;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "begin_date")
    private OffsetDateTime beginDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;
}
