package school.hei.haapi.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Setter
@Getter
@Table(name = "\"courses\"")
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private String id;

    private String code;
    private String name;
    private int credits;
    private int total_hours;

    @OneToOne
    @JoinColumn(name = "main_teacher")
    private User main_teacher;
}
