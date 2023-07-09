package net.javaguides.springboot.web.dto;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    Long id;
    String name;
    String intro;
    String owner_name;
    String status;
    Date startDate;
    Date endDate;
}
