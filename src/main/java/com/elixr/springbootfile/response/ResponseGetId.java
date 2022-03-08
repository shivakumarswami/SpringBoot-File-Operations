package com.elixr.springbootfile.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseGetId {
    private String status;
    private SuccessResponse data;
}
