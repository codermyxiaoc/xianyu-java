package cn.coderxiaoc.dto;

import lombok.Data;

@Data
public class BodyData {
    private String bizType;
    private String data;
    private String streamId;
    private Long objectType;
    private String syncId;
}
