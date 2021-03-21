package capprezy.ua.model.dto;

import capprezy.ua.model.Dish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UploadDish {
    @Valid
    private Dish dish;
    private MultipartFile multipartFile;
}
