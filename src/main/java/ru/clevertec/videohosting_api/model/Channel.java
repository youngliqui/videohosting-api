package ru.clevertec.videohosting_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

@Data
@ToString(exclude = {"subscribers", "author"})
@EqualsAndHashCode(exclude = {"subscribers", "author"})
@Entity
@Table(name = "channels")
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

    @ManyToMany(mappedBy = "subscriptions", fetch = FetchType.LAZY)
    private Set<User> subscribers;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date creationDate;

    @Column(nullable = false)
    private String language;

    @Lob
    private byte[] avatar;

    @Enumerated(EnumType.STRING)
    private Category category;
}
