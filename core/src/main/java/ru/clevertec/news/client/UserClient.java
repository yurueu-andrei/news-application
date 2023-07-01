package ru.clevertec.news.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.clevertec.news.dto.UserDto;

/**
 * Open Feign user client
 *
 * @author Yuryeu Andrei
 */
@FeignClient(name = "user-client", url = "${client.baseUrl}")
public interface UserClient {

    @PostMapping("/auth/users")
    UserDto getUserDetails(@RequestHeader("Authorization") String token);
}
