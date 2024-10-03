package com.meet.bot.utils;

import java.util.Random;

public class ChatroomNameGenerator {

    private static final String[] ADJECTIVES = {
            "Awesome", "Cool", "Funky", "Groovy", "Happy", "Jazzy", "Lovely", "Marvelous", "Peaceful", "Radiant",
            "Spicy", "Trendy", "Vibrant", "Wonderful", "Zen", "Epic", "Stellar", "Dynamic", "Serene", "Cozy"
    };

    private static final String[] NOUNS = {
            "Chat", "Lounge", "Hangout", "Cafe", "Space", "Zone", "Den", "Club", "Hub", "Haven",
            "Realm", "Sanctuary", "Nexus", "Haven", "Parlor", "Summit", "Oasis", "Haven"
    };

    private static final String[] SPECIAL_WORDS = {
            "The", "Secret", "Hidden", "Mystic", "Cyber", "Virtual", "Quantum", "Galactic", "Cosmic", "Arcane"
    };

    private static final Random random = new Random();

    public static String generateRandomChatroomName() {
        StringBuilder name = new StringBuilder();
        if (random.nextBoolean()) {
            name.append(SPECIAL_WORDS[random.nextInt(SPECIAL_WORDS.length)]).append(" ");
        }
        name.append(ADJECTIVES[random.nextInt(ADJECTIVES.length)]).append(" ");
        name.append(NOUNS[random.nextInt(NOUNS.length)]);
        return name.toString();
    }


}
