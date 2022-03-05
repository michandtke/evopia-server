package de.mwa.evopiaserver.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChannel {
    private String name;
    private String value;
}
