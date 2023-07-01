package ru.clevertec.news.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.clevertec.news.dto.NewsSaveDto;
import ru.clevertec.news.dto.NewsUpdateDto;
import ru.clevertec.news.util.WithAuthority;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NewsIntegrationTest extends BaseIntegrationTest {

    @Test
    void findByIdTest_shouldReturnNewsWithId5() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/news/5"))
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.title").value("news Ukraine title fifth five"))
                .andExpect(jsonPath("$.text").value("text of the fifth news about Ukraine"))
                .andExpect(jsonPath("$.createDate").value("2023-04-12T18:55:07.319"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Test
    void findAllTest_shouldReturnNewsWithIdFrom3To4() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/news?page=1&size=2"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(3L))
                .andExpect(jsonPath("$[0].title").value("news Ukraine title third three"))
                .andExpect(jsonPath("$[1].id").value(4L))
                .andExpect(jsonPath("$[1].title").value("news AI title fourth four"))
                .andExpect(jsonPath("$[2]").doesNotExist())
                .andExpect(status().isOk())
                .andReturn();

        //then
        Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Nested
    class WithWriteNewsAuthorityOnly {

        @Test
        @WithAuthority(authorities = "WRITE_NEWS")
        void addTest_shouldAddNews() throws Exception {
            //given
            NewsSaveDto news = new NewsSaveDto("title", "text", "username");

            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .content(mapper.writeValueAsString(news))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(21L))
                    .andExpect(jsonPath("$.title").value("title"))
                    .andExpect(jsonPath("$.text").value("text"))
                    .andExpect(jsonPath("$.username").value("username"))
                    .andExpect(jsonPath("$.createDate").isNotEmpty())
                    .andExpect(status().isCreated())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }

        @Nested
        class Update {

            @Test
            @WithAuthority(authorities = "WRITE_NEWS")
            void updateTest_shouldUpdateNewsTitleAndText() throws Exception {
                //given
                NewsUpdateDto newsUpdateDto = new NewsUpdateDto(8L, "new title", "new text");

                //when
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/news")
                                .content(mapper.writeValueAsString(newsUpdateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(8L))
                        .andExpect(jsonPath("$.title").value("new title"))
                        .andExpect(jsonPath("$.text").value("new text"))
                        .andExpect(jsonPath("$.createDate").value("2023-04-25T23:11:07.319"))
                        .andExpect(status().isOk())
                        .andReturn();

                mockMvc.perform(MockMvcRequestBuilders.get("/news/8"))
                        .andExpect(jsonPath("$.id").value(8L))
                        .andExpect(jsonPath("$.title").value("new title"))
                        .andExpect(jsonPath("$.text").value("new text"))
                        .andExpect(status().isOk());

                //then
                Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
            }

            @Test
            @WithAuthority(authorities = "WRITE_NEWS")
            void updateTest_shouldUpdateNewsTitleOnly() throws Exception {
                //given
                NewsUpdateDto newsUpdateDto = new NewsUpdateDto();
                newsUpdateDto.setId(9L);
                newsUpdateDto.setTitle("new title");

                //when
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/news")
                                .content(mapper.writeValueAsString(newsUpdateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(9L))
                        .andExpect(jsonPath("$.title").value("new title"))
                        .andExpect(jsonPath("$.text").value("text of the ninth news about Ukraine"))
                        .andExpect(jsonPath("$.createDate").value("2023-04-10T05:22:07.319"))
                        .andExpect(status().isOk())
                        .andReturn();

                mockMvc.perform(MockMvcRequestBuilders.get("/news/9"))
                        .andExpect(jsonPath("$.id").value(9L))
                        .andExpect(jsonPath("$.title").value("new title"))
                        .andExpect(jsonPath("$.text").value("text of the ninth news about Ukraine"))
                        .andExpect(status().isOk());

                //then
                Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
            }

            @Test
            @WithAuthority(authorities = "WRITE_NEWS")
            void updateTest_shouldUpdateNewsTextOnly() throws Exception {
                //given
                NewsUpdateDto newsUpdateDto = new NewsUpdateDto();
                newsUpdateDto.setId(10L);
                newsUpdateDto.setText("new text");

                //when
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/news")
                                .content(mapper.writeValueAsString(newsUpdateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(10L))
                        .andExpect(jsonPath("$.title").value("news crypto title tenth ten"))
                        .andExpect(jsonPath("$.text").value("new text"))
                        .andExpect(jsonPath("$.createDate").value("2023-04-19T01:00:07.319"))
                        .andExpect(status().isOk())
                        .andReturn();

                mockMvc.perform(MockMvcRequestBuilders.get("/news/10"))
                        .andExpect(jsonPath("$.id").value(10L))
                        .andExpect(jsonPath("$.title").value("news crypto title tenth ten"))
                        .andExpect(jsonPath("$.text").value("new text"))
                        .andExpect(status().isOk());

                //then
                Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
            }
        }

        @Test
        @WithAuthority(authorities = "WRITE_NEWS")
        void deleteTest_shouldThrowAccessDeniedException() throws Exception {
            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/news/6"))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("Access Denied"))
                    .andExpect(jsonPath("$.path").value("/news/6"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }
    }

    @Nested
    class WithDeleteNewsAuthorityOnly {

        @Test
        @WithAuthority(authorities = "DELETE_NEWS")
        void addTest_shouldThrowAccessDeniedException() throws Exception {
            //given
            NewsSaveDto news = new NewsSaveDto("title", "text", "username");

            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .content(mapper.writeValueAsString(news))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("Access Denied"))
                    .andExpect(jsonPath("$.path").value("/news"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }

        @Test
        @WithAuthority(authorities = "DELETE_NEWS")
        void updateTest_shouldThrowAccessDeniedException() throws Exception {
            //given
            NewsUpdateDto newsUpdateDto = new NewsUpdateDto(8L, "new title", "new text");

            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/news")
                            .content(mapper.writeValueAsString(newsUpdateDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("Access Denied"))
                    .andExpect(jsonPath("$.path").value("/news"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }

        @Test
        @WithAuthority(authorities = "DELETE_NEWS")
        void deleteTest_shouldDeleteNewsWithId6() throws Exception {
            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/news/6"))
                    .andExpect(jsonPath("$").value(true))
                    .andExpect(status().isOk())
                    .andReturn();

            mockMvc.perform(MockMvcRequestBuilders.get("/news/6"))
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.error").value("Not Found"))
                    .andExpect(jsonPath("$.message").value("The piece of news with id = 6 was not found"))
                    .andExpect(jsonPath("$.path").value("/news/6"))
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
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/news/100"))
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.error").value("Not Found"))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.path").value("/news/100"))
                    .andExpect(status().isNotFound())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }

        @Test
        @WithAuthority
        void addTest_shouldReturnErrorInfoWith400StatusWhenServiceExceptionHandled() throws Exception {
            //given
            NewsSaveDto newsSaveDto = new NewsSaveDto();
            newsSaveDto.setTitle("title");

            //when
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .content(mapper.writeValueAsString(newsSaveDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.path").value("/news"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            //then
            Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        }
    }
}
