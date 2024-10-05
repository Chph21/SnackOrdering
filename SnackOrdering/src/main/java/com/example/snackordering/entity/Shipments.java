package com.example.snackordering.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Shipments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "shipment_id", nullable = false)
    private Integer shipmentId;

    @Column(name = "shipment_date", nullable = false)
    private Date shipmentDate;

    @Column(name = "expected_delivery_date", nullable = false)
    private Date expectedDeliveryDate;

    @Column(name = "shipment_status", nullable = false)
    private int shipmentStatus;

    @Column(name = "actual_delivery_date")
    private Date actualDeliveryDate;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "shipping_cost", nullable = false)
    private String shippingCost;

    @Column(name = "note")
    private String note;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "branch_id", nullable = false)
    private Order order;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @Column(name = "updated_date", nullable = false)
    @UpdateTimestamp
    private Date updatedDate;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Shipments that = (Shipments) o;
        return getShipmentId() != null && Objects.equals(getShipmentId(), that.getShipmentId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
