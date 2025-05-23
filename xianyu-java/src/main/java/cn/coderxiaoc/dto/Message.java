package cn.coderxiaoc.dto;

import lombok.Data;

@Data
public class Message {
    public Headers headers;
    private String lwp;
    private Body body;
}
