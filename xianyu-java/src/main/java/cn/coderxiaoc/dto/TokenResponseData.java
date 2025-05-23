package cn.coderxiaoc.dto;
import lombok.Data;

@Data
public class TokenResponseData {
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiredTime;
}
