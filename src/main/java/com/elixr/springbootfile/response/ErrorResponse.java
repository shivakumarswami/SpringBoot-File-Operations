package com.elixr.springbootfile.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String success;
    private String errorMsg;
}
