package com.example.linkconverter.util;

public class CharacterParser {
    public static String parseCharacter(String str) {

        char[] turkishChars = new char[]{0x131, 0x130, 0xFC, 0xDC, 0xF6, 0xD6, 0x15F, 0x15E, 0xE7, 0xC7, 0x11F, 0x11E};
        char[] latinChars = new char[]{'i', 'I', 'u', 'U', 'o', 'O', 's', 'S', 'c', 'C', 'g', 'G'};
        for (int i = 0; i < turkishChars.length; i++) {
            str = str.replaceAll(new String(new char[]{turkishChars[i]}), new String(new char[]{latinChars[i]}));
        }
        return str;

    }
}
