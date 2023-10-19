package ru.khokhlov.messenger.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Messenger API",
                description = "Messenger for chatting!", version = "0.1.3",
                contact = @Contact(
                        name = "Messenger Group",
                        email = "DmitryXox02@yandex.ru"
                )
        )
)
public class SwaggerConfig {

}