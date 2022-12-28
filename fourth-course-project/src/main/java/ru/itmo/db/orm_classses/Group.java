package ru.itmo.db.orm_classses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.Min;

@Entity
@Table(name = "tb_groups")
@Getter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    @NonNull
    private String name;

    @Column(nullable = false)
    @Min(value = 3, message = "Максимальный размер группы не может быть меньше 3 человек")
    @NonNull
    private int maxAlpinists;

    @OneToOne(fetch = FetchType.LAZY)
    @NonNull
    private Mountain mountain;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Alpinist[] alpinists;
}
