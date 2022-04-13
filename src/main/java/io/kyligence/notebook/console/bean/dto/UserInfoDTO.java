package io.kyligence.notebook.console.bean.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kyligence.notebook.console.util.EntityUtils;
import io.kyligence.saas.iam.pojo.AuthInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDTO {

    private String id;

    private String username;

    @JsonProperty("is_admin")
    private Boolean isAdmin;

    public static UserInfoDTO valueOf(AuthInfo authInfo) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId(authInfo.getEntityId());
        userInfoDTO.setUsername(authInfo.getUsername());
        userInfoDTO.setIsAdmin(authInfo.getUsername().equalsIgnoreCase("admin"));
        return userInfoDTO;
    }
}
