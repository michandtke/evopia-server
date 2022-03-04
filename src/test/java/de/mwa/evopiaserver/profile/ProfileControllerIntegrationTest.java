package de.mwa.evopiaserver.profile;

import de.mwa.evopiaserver.registration.UserRepository;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
public class ProfileControllerIntegrationTest {

    private ProfileRepository profileRepositoryMock;
    private UserRepository userRepositoryMock;
    private TagRepository tagRepositoryMock;
    private IProfileChannelService profileChannelServiceMock;

    ProfileRestController profileController;

    private MockMvc mockMvc;

    ProfileControllerIntegrationTest() {
        profileRepositoryMock = Mockito.mock(ProfileRepository.class);
        userRepositoryMock = Mockito.mock(UserRepository.class);
        tagRepositoryMock = Mockito.mock(TagRepository.class);
        profileChannelServiceMock = Mockito.mock(IProfileChannelService.class);

        profileController = new ProfileRestController(profileRepositoryMock,
                userRepositoryMock,
                tagRepositoryMock,
                profileChannelServiceMock);
        mockMvc = MockMvcBuilders
                .standaloneSetup(profileController)
                .build();
    }

    @Test
    public void whenPostRequestToRegisterProfileAndValidNewProfile_thenCorrectResponse() throws Exception {
        String user = "{" +
                "\"image\": \"myImagePath\"," +
                "\"channels\": \"myChannels\"," +
                "\"tags\": \"myTags\"," +
                "\"email\" : \"bob@domain.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/register/profile")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToRegisterProfileAndInValidUser_thenCorrectResponse() throws Exception {
        String user = "{\"name\": \"\", \"email\" : \"bob@domain.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/register/profile")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.image", Is.is("Image is mandatory.")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }
}
