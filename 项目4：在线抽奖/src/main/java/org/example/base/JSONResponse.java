package org.example.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * 统一响应的数据格式
 */
public class JSONResponse {
    private boolean success;
    private String code;
    private String message;
    private Object data;
}
