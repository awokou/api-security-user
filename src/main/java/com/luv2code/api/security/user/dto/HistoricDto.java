package com.luv2code.api.security.user.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricDto {
    private Long id;
    private String name;
    private Instant loginTime;
}
