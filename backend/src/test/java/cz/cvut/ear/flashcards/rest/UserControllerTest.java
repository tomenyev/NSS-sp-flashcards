package cz.cvut.ear.flashcards.rest;

import cz.cvut.ear.flashcards.service.UserService;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class UserControllerTest extends BaseControllerTestRunner {

    @InjectMocks
    private UserController userControllerMock;

    @Mock
    private UserService userServiceImplMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(userControllerMock);
    }

//    @WithMockUser("USER")
//    @Test
//    public void addUser() throws Exception {
//        User user = Generator.generateUser();
//
//        Mockito.when(userServiceImplMock.addUser(Mockito.any(User.class))).thenReturn(user);
//
//        final RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post("/api/users/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(toJson(user));
//
//        final MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
//
//        final User result = readValue(mvcResult, User.class);
//
//        assertNotNull(result);
//        assertEquals(result.getId(), result.getId());
//        assertEquals(user.getUsername(), result.getUsername());
//    }

//    @WithMockUser("USER")
//    @Test
//    public void deleteUser() throws Exception {
//        mockMvc.perform(delete("/api/users/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }

}
