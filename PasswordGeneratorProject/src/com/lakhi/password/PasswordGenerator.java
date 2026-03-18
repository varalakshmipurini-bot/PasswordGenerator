package com.lakhi.password;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String DEFAULT_SPECIAL = "!@#$%^&*()-_=+<>?";
    private static final String SIMILAR_CHARS = "0OlI1";

    private final SecureRandom random;

    public PasswordGenerator() {
        random = new SecureRandom();
    }

    public String generatePassword(int length,
                                   boolean useLower,
                                   boolean useUpper,
                                   boolean useNumbers,
                                   boolean useSpecial,
                                   boolean avoidSimilar,
                                   boolean noRepeat,
                                   String customSpecialChars,
                                   String excludeChars) {

        List<String> selectedSets = new ArrayList<>();
        StringBuilder characterPool = new StringBuilder();

        String lowerSet = filterCharacters(LOWERCASE, avoidSimilar, excludeChars);
        String upperSet = filterCharacters(UPPERCASE, avoidSimilar, excludeChars);
        String numberSet = filterCharacters(NUMBERS, avoidSimilar, excludeChars);

        String finalSpecial = (customSpecialChars != null && !customSpecialChars.trim().isEmpty())
                ? customSpecialChars
                : DEFAULT_SPECIAL;
        String specialSet = filterSpecialCharacters(finalSpecial, excludeChars);

        if (useLower && !lowerSet.isEmpty()) {
            selectedSets.add(lowerSet);
            characterPool.append(lowerSet);
        }
        if (useUpper && !upperSet.isEmpty()) {
            selectedSets.add(upperSet);
            characterPool.append(upperSet);
        }
        if (useNumbers && !numberSet.isEmpty()) {
            selectedSets.add(numberSet);
            characterPool.append(numberSet);
        }
        if (useSpecial && !specialSet.isEmpty()) {
            selectedSets.add(specialSet);
            characterPool.append(specialSet);
        }

        if (selectedSets.isEmpty()) {
            return "Error: No valid character types selected!";
        }

        // Length validity check
        if (length < selectedSets.size()) {
            return "Error: Length must be at least " + selectedSets.size()
                    + " to include all selected character types.";
        }

        // No repeated characters check
        if (noRepeat && length > characterPool.length()) {
            return "Error: Length is too big for no-repeat option with selected characters.";
        }

        List<Character> passwordChars = new ArrayList<>();

        // Add minimum one char from each selected set
        for (String set : selectedSets) {
            char ch = getUniqueRandomChar(set, passwordChars, noRepeat);
            passwordChars.add(ch);
        }

        // Fill remaining chars
        while (passwordChars.size() < length) {
            char ch = getUniqueRandomChar(characterPool.toString(), passwordChars, noRepeat);
            passwordChars.add(ch);
        }

        // Proper shuffle
        Collections.shuffle(passwordChars, random);

        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }

    public List<String> generateMultiplePasswords(int count,
                                                  int length,
                                                  boolean useLower,
                                                  boolean useUpper,
                                                  boolean useNumbers,
                                                  boolean useSpecial,
                                                  boolean avoidSimilar,
                                                  boolean noRepeat,
                                                  String customSpecialChars,
                                                  String excludeChars) {

        List<String> passwords = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            passwords.add(generatePassword(length, useLower, useUpper, useNumbers,
                    useSpecial, avoidSimilar, noRepeat, customSpecialChars, excludeChars));
        }

        return passwords;
    }

    public String generatePronounceablePassword(String baseWord, boolean addNumbers, boolean addSpecial) {
        if (baseWord == null || baseWord.trim().isEmpty()) {
            baseWord = "Ravi";
        }

        StringBuilder password = new StringBuilder();
        password.append(Character.toUpperCase(baseWord.charAt(0)));

        if (baseWord.length() > 1) {
            password.append(baseWord.substring(1).toLowerCase());
        }

        if (addSpecial) {
            String specials = "@#$%";
            password.append(specials.charAt(random.nextInt(specials.length())));
        }

        if (addNumbers) {
            int number = 1000 + random.nextInt(9000);
            password.append(number);
        }

        return password.toString();
    }

    public String checkPasswordStrength(String password) {
        if (password == null || password.isEmpty() || password.startsWith("Error")) {
            return "Invalid";
        }

        int score = 0;

        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*\\d.*")) score++;
        if (password.matches(".*[^a-zA-Z0-9].*")) score++;

        if (score <= 2) return "Weak";
        if (score <= 4) return "Medium";
        return "Strong";
    }

    public double calculateEntropy(String password, boolean useLower, boolean useUpper,
                                   boolean useNumbers, boolean useSpecial,
                                   String customSpecialChars, boolean avoidSimilar,
                                   String excludeChars) {

        int poolSize = 0;

        String lowerSet = filterCharacters(LOWERCASE, avoidSimilar, excludeChars);
        String upperSet = filterCharacters(UPPERCASE, avoidSimilar, excludeChars);
        String numberSet = filterCharacters(NUMBERS, avoidSimilar, excludeChars);

        String finalSpecial = (customSpecialChars != null && !customSpecialChars.trim().isEmpty())
                ? customSpecialChars
                : DEFAULT_SPECIAL;
        String specialSet = filterSpecialCharacters(finalSpecial, excludeChars);

        if (useLower) poolSize += lowerSet.length();
        if (useUpper) poolSize += upperSet.length();
        if (useNumbers) poolSize += numberSet.length();
        if (useSpecial) poolSize += specialSet.length();

        if (poolSize == 0 || password == null || password.isEmpty() || password.startsWith("Error")) {
            return 0.0;
        }

        return password.length() * (Math.log(poolSize) / Math.log(2));
    }

    private char getUniqueRandomChar(String chars, List<Character> existingChars, boolean noRepeat) {
        if (!noRepeat) {
            return chars.charAt(random.nextInt(chars.length()));
        }

        List<Character> available = new ArrayList<>();
        for (char c : chars.toCharArray()) {
            if (!existingChars.contains(c)) {
                available.add(c);
            }
        }

        if (available.isEmpty()) {
            throw new IllegalArgumentException("No more unique characters available.");
        }

        return available.get(random.nextInt(available.size()));
    }

    private String filterCharacters(String input, boolean avoidSimilar, String excludeChars) {
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            boolean isSimilar = avoidSimilar && SIMILAR_CHARS.indexOf(c) != -1;
            boolean isExcluded = excludeChars != null && excludeChars.indexOf(c) != -1;

            if (!isSimilar && !isExcluded) {
                result.append(c);
            }
        }

        return result.toString();
    }

    private String filterSpecialCharacters(String input, String excludeChars) {
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            boolean isExcluded = excludeChars != null && excludeChars.indexOf(c) != -1;
            if (!isExcluded) {
                result.append(c);
            }
        }

        return result.toString();
    }
}