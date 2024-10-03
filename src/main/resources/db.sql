-- Create User table
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE
);

-- Create ChatRoom table
CREATE TABLE chat_rooms (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL
);

-- Create join table for User and ChatRoom (many-to-many relationship)
CREATE TABLE user_chat_room (
                                user_id BIGINT NOT NULL,
                                chat_room_id BIGINT NOT NULL,
                                PRIMARY KEY (user_id, chat_room_id),
                                FOREIGN KEY (user_id) REFERENCES users(id),
                                FOREIGN KEY (chat_room_id) REFERENCES chat_rooms(id)
);

-- Create indexes for better query performance
CREATE INDEX idx_user_chat_room_user_id ON user_chat_room(user_id);
CREATE INDEX idx_user_chat_room_chat_room_id ON user_chat_room(chat_room_id);
