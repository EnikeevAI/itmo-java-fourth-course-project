package ru.itmo.db.orm_classses;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "tb_groups")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private int id;

    @Column(nullable = false)
    @Min(value = 3, message = "Минимальный размер группы для создания - 3 человека")
    @NonNull
    private int maxAlpinists;

    @Column(nullable = false)
    @NonNull
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    private Mountain mountain;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Alpinist> alpinists;

    public Group(int maxAlpinists, Mountain mountain) {
        this.maxAlpinists = maxAlpinists;
        this.mountain = mountain;
        alpinists = new ArrayList<>(maxAlpinists);
        dateTime = LocalDateTime.now().plusMonths(1);
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() { return id; }

    private boolean isRecruitOpen() {
        return alpinists.size() < maxAlpinists;
    }

    public void addAlpinist(Alpinist alpinist) {
        if (isRecruitOpen()) {
            alpinists.add(alpinist);
            alpinist.setGroup(this);
            System.out.println("Альпинист " + alpinist.getName() + " добавлен в группу");
        }
    }

    public void removeAlpinist(Alpinist alpinist) {
        if (alpinists.contains(alpinist)) {
            alpinists.remove(alpinist);
            alpinist.setGroup(null);
            System.out.println("Альпинист " + alpinist.getName() + " удален из группы");
        } else {
            System.out.println("Альпинист " + alpinist.getName() + " не состоит в группе");
        }
    }
}
