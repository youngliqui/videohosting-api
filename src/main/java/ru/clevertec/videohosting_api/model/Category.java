package ru.clevertec.videohosting_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Data
@ToString(exclude = "channels")
@EqualsAndHashCode(exclude = "channels")
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @SequenceGenerator(name = "categoryIdSeqGen", sequenceName = "categories_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoryIdSeqGen")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Channel> channels;
}
