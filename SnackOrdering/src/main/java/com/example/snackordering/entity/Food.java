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
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "food_id", nullable = false)
    private Integer foodId;

    @Column(name = "food_name", length = 50, nullable = false)
    private String foodName;

    @Column(name = "image_url", length = 250)
    private String imageURL;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "ingredients", length = 250)
    private String ingredients;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

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
        Food that = (Food) o;
        return getFoodId() != null && Objects.equals(getFoodId(), that.getFoodId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
