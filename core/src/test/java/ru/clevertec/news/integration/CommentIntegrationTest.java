package ru.clevertec.news.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.clevertec.news.dto.CommentSaveDto;
import ru.clevertec.news.dto.CommentUpdateDto;
import ru.clevertec.news.util.WithAuthority;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentIntegrationTest extends BaseIntegrationTest {

    @Test
    void findByIdTest_shouldReturnCommentWithId5() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/comments/5"))
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.text").value("well done"))
                .andExpect(jsonPath("$.username").value("dobrowydka"))
                .andExpect(jsonPath("$.createDate").value("2023-04-12T20:20:07.319"))
                .andExpect(jsonPath("$.news.id").value(5L))
                .andExpect(status().isOk())
                .andReturn();

        //then
        Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Test
    void findAllTest_shouldReturnCommentsWithIdFrom3To4() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/comments?page=1&size=2"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(3L))
                .andExpect(jsonPath("$[0].text").value("very exiting news"))
                .andExpect(jsonPath("$[0].username").value("dobrowydka"))
                .andExpect(jsonPath("$[1].id").value(4L))
                .andExpect(jsonPath("$[1].text").value("good job!"))
                .andExpect(jsonPath("$[1].username").value("nst.yrk"))
                .andExpect(jsonPath("$[2]").doesNotExist())
                .andExpect(status().isOk())
                .andReturn();

        //then
        Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Nested
    class WithWriteCommentsAuthorityOnly {

        @Test
        @WithAuthority(authorities = "WRITE_COMMENTS")
        void addTest_shouldAddComment() throws Exception {
            //given
            CommentSaveDto comment = new CommentSaveDto("text", "username", 5L);

            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                            .content(mapper.writeValueAsString(comment))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(12L))
                    .andExpect(jsonPath("$.text").value("text"))
                    .andExpect(jsonPath("$.username").value("username"))
                    .andExpect(jsonPath("$.createDate").isNotEmpty())
                    .andExpect(jsonPath("$.news.id").value(5L))
                    .andExpect(status().isCreated())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }

        @Test
        @WithAuthority(authorities = "WRITE_COMMENTS")
        void updateTest_shouldUpdateCommentsText() throws Exception {
            //given
            CommentUpdateDto commentUpdateDto = new CommentUpdateDto(8L, "new text");

            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/comments")
                            .content(mapper.writeValueAsString(commentUpdateDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(8L))
                    .andExpect(jsonPath("$.text").value("new text"))
                    .andExpect(jsonPath("$.username").value("over1337"))
                    .andExpect(jsonPath("$.createDate").value("2023-04-26T11:30:07.319"))
                    .andExpect(jsonPath("$.news.id").value(8L))
                    .andExpect(status().isOk())
                    .andReturn();

            mockMvc.perform(MockMvcRequestBuilders.get("/comments/8"))
                    .andExpect(jsonPath("$.id").value(8L))
                    .andExpect(jsonPath("$.text").value("new text"))
                    .andExpect(jsonPath("$.username").value("over1337"))
                    .andExpect(jsonPath("$.createDate").value("2023-04-26T11:30:07.319"))
                    .andExpect(jsonPath("$.news.id").value(8L))
                    .andExpect(status().isOk())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }

        @Test
        @WithAuthority(authorities = "WRITE_COMMENTS")
        void deleteTest_shouldThrowAccessDeniedException() throws Exception {
            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/comments/6"))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("Access Denied"))
                    .andExpect(jsonPath("$.path").value("/comments/6"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }
    }

    @Nested
    class WithDeleteCommentsAuthorityOnly {

        @Test
        @WithAuthority(authorities = "DELETE_COMMENTS")
        void addTest_shouldThrowAccessDeniedException() throws Exception {
            //given
            CommentSaveDto comment = new CommentSaveDto("text", "username", 5L);

            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                            .content(mapper.writeValueAsString(comment))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("Access Denied"))
                    .andExpect(jsonPath("$.path").value("/comments"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }

        @Test
        @WithAuthority(authorities = "DELETE_COMMENTS")
        void updateTest_shouldThrowAccessDeniedException() throws Exception {
            //given
            CommentUpdateDto commentUpdateDto = new CommentUpdateDto(9L, "new text");

            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/comments")
                            .content(mapper.writeValueAsString(commentUpdateDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("Access Denied"))
                    .andExpect(jsonPath("$.path").value("/comments"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            mockMvc.perform(MockMvcRequestBuilders.get("/comments/9"))
                    .andExpect(jsonPath("$.id").value(9L))
                    .andExpect(jsonPath("$.text").value("i recommend to study this material"))
                    .andExpect(jsonPath("$.username").value("shos"))
                    .andExpect(jsonPath("$.createDate").value("2023-04-28T22:50:07.319"))
                    .andExpect(jsonPath("$.news.id").value(7L))
                    .andExpect(status().isOk())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }

        @Test
        @WithAuthority(authorities = "DELETE_COMMENTS")
        void deleteTest_shouldDeleteCommentWithId6() throws Exception {
            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/comments/6"))
                    .andExpect(jsonPath("$").value(true))
                    .andExpect(status().isOk())
                    .andReturn();

            mockMvc.perform(MockMvcRequestBuilders.get("/comments/6"))
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.error").value("Not Found"))
                    .andExpect(jsonPath("$.message").value("The comment with id = 6 was not found"))
                    .andExpect(jsonPath("$.path").value("/comments/6"))
                    .andExpect(status().isNotFound());

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }
    }

    @Nested
    class ServiceExceptionCases {

        @Test
        @WithAuthority
        void findByIdTest_shouldReturnErrorInfoWith404StatusWhenServiceExceptionHandled() throws Exception {
            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/comments/100"))
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.error").value("Not Found"))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.path").value("/comments/100"))
                    .andExpect(status().isNotFound())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }

        @Test
        @WithAuthority
        void addTest_shouldReturnErrorInfoWith400StatusWhenServiceExceptionHandled() throws Exception {
            //given
            CommentSaveDto commentSaveDto = new CommentSaveDto();
            commentSaveDto.setText("text");
            commentSaveDto.setUsername("username");

            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                            .content(mapper.writeValueAsString(commentSaveDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.path").value("/comments"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }
    }
}
