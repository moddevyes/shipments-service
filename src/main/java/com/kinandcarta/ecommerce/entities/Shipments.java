package com.kinandcarta.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "shipments")
@Slf4j
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shipments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private ShipmentAddress shipmentAddress;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private ShipmentAccount shipmentAccount;

    @Embedded
    private ShippedDate shippedDate;

    @Embedded
    private DeliveryDate deliveryDate;

    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @Column
    private Set<OrderLineItems> orderLineItems;

    @CreationTimestamp
    @Column(name="created_dt")
    private Instant createDateTime;

    @UpdateTimestamp
    @Column(name="updated_dt")
    private Instant updateDateTime;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Shipments shipments = (Shipments) o;
        return getId() != null && Objects.equals(getId(), shipments.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
