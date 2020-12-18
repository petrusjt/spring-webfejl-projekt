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
    @EqualsAndHashCode.Exclude
    private Long id;
    private String shortDescription;
    private String longDescription;
    @EqualsAndHashCode.Exclude
    private Date deadline;
    private Level level;
    @EqualsAndHashCode.Exclude
    private boolean done;
}
