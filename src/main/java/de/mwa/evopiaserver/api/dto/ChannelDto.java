package de.mwa.evopiaserver.api.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChannelDto {
    private String name;

    public ChannelDto() {}
    public ChannelDto(String name) {
        this.name = name;
    }
}
