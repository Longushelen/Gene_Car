package genecar.security.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountFormVO {

    private String id;

    private String password;
    
    private String reqAdmin; // 어드민 페이지 접근

    private final UserRole role = UserRole.USER;

}
