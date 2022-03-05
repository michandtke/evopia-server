package de.mwa.evopiaserver.profile;

import de.mwa.evopiaserver.db.tag.Tag;
import de.mwa.evopiaserver.db.tag.TagRepository;
import de.mwa.evopiaserver.registration.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class TagRestController {

    @Autowired
    private TagRepository tagRepository;

    @PostMapping("/tag/register")
    public GenericResponse registerChannel(@Valid @RequestBody Tag tag,
                                           final HttpServletRequest request) {
        Tag saved = tagRepository.save(tag);
        return new GenericResponse("Saved tag " + saved);
    }
}
