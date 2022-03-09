package de.mwa.evopiaserver.api.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChannelDto {
    private final String name;
}
