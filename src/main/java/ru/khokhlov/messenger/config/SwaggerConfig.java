package ru.khokhlov.messenger.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Biletka API",
                description = "Билетная система ", version = "0.1.4",
                contact = @Contact(
                        name = "Biletka Group",
                        email = "DmitryXox02@yandex.ru"
                )
        )
)
public class SwaggerConfig {

}