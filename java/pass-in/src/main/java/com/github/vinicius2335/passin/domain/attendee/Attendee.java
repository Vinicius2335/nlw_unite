package com.github.vinicius2335.passin.domain.attendee;

import com.github.vinicius2335.passin.domain.event.Event;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "attendees")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Attendee {

    @Id
    @Column(nullable = false)
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendee_seq")
    @SequenceGenerator(name="attendee_seq", sequenceName = "SEQUENCE_ATTENDEES", initialValue=1001, allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}
