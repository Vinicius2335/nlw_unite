package com.github.vinicius2335.passin.domain.event;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String details;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, name = "maximun_attendees")
    private Integer maximunAttendees;
}
