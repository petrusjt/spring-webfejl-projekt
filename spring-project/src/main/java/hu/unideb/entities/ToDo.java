package hu.unideb.entities;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class ToDo {
    private Long id;
    private String shortDescription;
    private String longDescription;
    private Date deadline;
    private Level level;
    private boolean done;
}
