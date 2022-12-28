package ru.itmo.db.orm_classses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_mountains")
@RequiredArgsConstructor
@Getter
@Setter
public class Mountain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    @Length(min = 4, message = "В наименовании горы должно быть минимум 4 буквы")
    @NonNull
    private String name;

    @Column(length = 200, nullable = false)
    @Size(min = 4, message = "В наименовании страны должно быть минимум 4 буквы")
    @NonNull
    private String country;

    @Column(nullable = false)
    @Min(value = 100, message = "Минимальная высота горы равна 100 метрам")
    @NonNull
    private int height;
}
