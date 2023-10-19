# Messenger Application

## Technologies
- Spring Boot
- Swagger
- PostgreSQL
- Docker
- Liquibase

## Introduction
This is a simple Messenger application built using Spring Boot and PostgreSQL. It allows users to send and receive messages, manage their friends, update user information, and perform various security-related actions. The application leverages Docker for containerization and Liquibase for database schema management.

## Methods

### Base Methods Implemented

- All fundamental user registration, authentication, and message sending/receiving methods are implemented and operational.

### Security

- **User Registration:** Allows users to register by providing their information. (POST /security)
- **Create Authorization Token:** Generates an authorization token for user interactions. (POST /security/auth)
- **Invalidate Authorization Token:** Marks a token as invalid, making it unusable. (PUT /security/invalidation)

**JWT (JSON Web Tokens):** The application uses JWT for secure user authentication and authorization. It provides a stateless and secure way to manage user sessions.

**CORS Policy:** The application has Cross-Origin Resource Sharing (CORS) policies in place to control and secure interactions with the frontend.

**Hashed Passwords:** User passwords are securely hashed and stored to ensure data security and privacy.

- **Activate Email:** Allows users to activate their email by providing an activation code. (GET /security/activate/{code})

### Friends Controller

- **Add Friend:** Allows users to add friends by providing their friend's nickname. (POST /friends/add)
- **Get all Friends:** Get list of friends by user's nickname. (GET /friends/get)

### Messages Controller

- **Send Message:** Enables users to send messages to other users. (POST /messages)
- **Retrieve Message History:** Retrieves the message history for a specific user and companion. (GET /messages/restore/{companionNickname})

### Swagger Documentation

You can access Swagger documentation for the available APIs at `http://localhost:8080/swagger-ui.html`. It provides detailed information about the available endpoints, request parameters, and response formats.

## Development Opportunities
This Messenger Application serves as a foundation for further development. Here are some development opportunities:
- Add user profiles, avatars, and more user-specific details.
- Improve friend management, allowing users to accept or reject friend requests.
- Implement group chats and support for multiple users in a chat.
- Develop a mobile app or a web frontend for a richer user experience.
- Enhance the security with additional authentication methods like two-factor authentication (2FA).

Feel free to contribute and expand the functionality of this application!
