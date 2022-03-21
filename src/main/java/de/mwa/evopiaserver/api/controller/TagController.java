package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.dto.TagDto;
import de.mwa.evopiaserver.db.kotlin.TagRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagController {

    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @PostMapping("/v2/tags/add")
    public String upsert(@RequestBody List<TagDto> tags) {
        return "Saved tags: " + tagRepository.saveAll(tags);
    }
}
