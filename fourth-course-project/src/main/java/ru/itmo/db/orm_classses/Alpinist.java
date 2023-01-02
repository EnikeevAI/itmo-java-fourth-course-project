package ru.itmo.db.orm_classses;

import jakarta.persistence.*;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_alpinists")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Alpinist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    @Length(min = 3, message = "В имени альпиниста должно быть минимум 3 буквы")
    @NonNull
    private String name;

    @Column(length = 200, nullable = false)
    @Size(min = 5, message = "В имени альпиниста должно быть минимум 5 букв")
    @NonNull
    private String address;

    @Column(nullable = false)
    @Min(value = 18, message = "К восхождению в горы допускаются альпинисты старше 18 лет")
    @NonNull
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tb_groups_id")
    private Group group;
}
