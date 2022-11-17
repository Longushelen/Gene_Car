package genecar.root.security.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountFormVO {

    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "영어 혹은 숫자만 입력 가능합니다.")
    @Length(min = 1, max = 10)
    @NotBlank(message = "아이디는 필수로 작성해야 합니다.")
    private String id;

    private String password;
    
    private String reqAdmin; // 어드민 페이지 접근

    private final UserRole role = UserRole.USER;

}
