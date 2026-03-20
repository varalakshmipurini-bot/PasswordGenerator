# Advanced Password Generator

Advanced Password Generator is a desktop application built using **Java Swing**. It helps users generate secure and customizable passwords with useful features like password strength checking, entropy calculation, password history, copy to clipboard, save to file, and pronounceable password generation.

## Features
- Generate secure random passwords
- Include lowercase, uppercase, numbers, and special characters
- Password strength checker
- Entropy calculation
- Avoid similar characters like `0, O, l, I, 1`
- No repeated characters option
- Minimum one character from each selected type
- Custom special characters
- Exclude certain characters
- Generate multiple passwords at once
- Pronounceable password generation
- Password history
- Copy to clipboard
- Save passwords to `.txt` and `.csv`

## Project Files
AdvancedPasswordGenerator/
│
├── src/
│   └── com/
│       └── lakhi/
│           └── password/
│               ├── Main.java
│               ├── PasswordGenerator.java
│               └── FileUtils.java
│
└── README.md

### Main.java
Handles the Java Swing GUI, user input, buttons, password display, strength display, entropy display, history, copy, and save functions.

### PasswordGenerator.java
Contains the main logic for password generation, strength checking, entropy calculation, no-repeat logic, similar character filtering, and pronounceable password generation.

### FileUtils.java
Handles saving passwords into text and CSV files.

## How It Works
The user enters the password length, selects required options, and generates one or more passwords. The application displays the generated password, password strength, entropy, and password history. Users can also copy, regenerate, generate pronounceable passwords, and save passwords to files.

## How to Run
1. Open the project in **Eclipse** or any Java IDE.
2. Create the package: `com.lakhi.password`
3. Add these files:
   - `Main.java`
   - `PasswordGenerator.java`
   - `FileUtils.java`
4. Run `Main.java`

## Screenshots
(<img width="921" height="742" alt="Screenshot 2026-03-20 122101" src="https://github.com/user-attachments/assets/ea094add-f93b-437b-929a-62e3bf6108c7" />
 <img width="921" height="742" alt="Screenshot 2026-03-20 122147" src="https://github.com/user-attachments/assets/4ddd3890-e0f2-45db-83c4-9bb9a8229896" />

## Requirements
- Java 8 or above
- Eclipse or any Java IDE

## Resume Description
**Advanced Password Generator using Java Swing**  
Developed a desktop-based password generator application using Java Swing with features like password strength checking, entropy calculation, pronounceable passwords, custom special characters, excluded characters, no repeated characters, password history, clipboard support, and file export to TXT/CSV.

## Author
**Varalakshmi Purini**
