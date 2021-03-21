package capprezy.ua.controller.exception.model;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String message;
}