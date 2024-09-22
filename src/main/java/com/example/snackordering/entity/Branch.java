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
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "branch_id", nullable = false)
    private Integer branchId;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "manager", nullable = false)
    private String manager;

    @Column(name = "manager_phone", nullable = false)
    private String managerPhone;

    @Column(name = "manager_email", nullable = false)
    private String managerEmail;

    @Column(name = "manager_address", nullable = false)
    private String managerAddress;

    @Column(name = "manager_image", nullable = false)
    private String managerImage;

    @Column(name = "manager_description", nullable = false)
    private String managerDescription;

    @Column(name = "manager_status", nullable = false)
    private String managerStatus;

    @Column(name = "manager_opening_hour", nullable = false)
    private String managerOpeningHour;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "opening_hour", nullable = false)
    private String openingHour;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @OneToOne(mappedBy = "branch")
    private AccountEntity account;

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
        Branch that = (Branch) o;
        return getBranchId() != null && Objects.equals(getBranchId(), that.getBranchId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
