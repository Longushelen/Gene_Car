package genecar.security.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountVO {

    private String id;

    private String role;

    public AccountVO(String id, String role) {
        super();
        this.id = id;
        this.role = role;
    }
}
