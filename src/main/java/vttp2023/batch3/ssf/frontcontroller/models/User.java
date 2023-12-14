package vttp2023.batch3.ssf.frontcontroller.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Size(min = 2)
    private String username;
    @Size(min = 2)
    private String password;
}
