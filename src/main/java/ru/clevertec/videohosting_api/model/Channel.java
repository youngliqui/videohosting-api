package ru.clevertec.videohosting_api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

@Builder
@Data
@ToString(exclude = {"subscribers", "author"})
@EqualsAndHashCode(exclude = {"subscribers", "author"})
@Entity
@Table(name = "channels")
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    @Id
    @SequenceGenerator(name = "channelsIdSeqGen", sequenceName = "channels_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "channelsIdSeqGen")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @OneToOne
    @JoinColumn(name = "author_id", nullable = false, unique = true)
    private User author;

    @ManyToMany(mappedBy = "subscriptions", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<User> subscribers;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date creationDate;

    @Column(nullable = false)
    private String language;

    private String avatar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
}
