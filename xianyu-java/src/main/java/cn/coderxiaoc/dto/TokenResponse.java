package cn.coderxiaoc.dto;

import lombok.Data;

//{"api":"mtop.taobao.idlemessage.pc.login.token","data":{"accessToken":"oauth_k1:HMP4WdRVoEA94RBnRYTaFixOg65BMN95FiYgsdJJlDSSMapxCRT7F+FkGP+ycQIVk9tEo9QlTjWr51/JfTu8Pg==","accessTokenExpiredTime":"86400000","refreshToken":"oauth_k1:sgekYV7qRSbfO3Gwvb19TBfGEtCj3/fB1jF2tppFm+VNnAydytkHjHamX94sA+evKpjLXyywU6CU7Rooqvo+bQ=="},"ret":["SUCCESS::调用成功"],"v":"1.0"}
@Data
public class TokenResponse {
    private String api;
    private TokenResponseData data;
    private String[] ret;
    private String v;

}
