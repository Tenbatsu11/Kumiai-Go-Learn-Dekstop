# Kumiai Go Learn Desktop

A Java-based desktop application for [Kumiai Go Learn](https://kumiaigolearn.com), featuring exclusive integrations and a new interactive quiz system.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Current Development Status](#current-development-status)
- [Known Limitations](#known-limitations)
- [Related Projects](#related-projects)

## Overview

This desktop application is an enhanced version of the Kumiai Go Learn website, providing exclusive features and integrations unavailable in the web version. It includes access to the Kumiai Go RPG prototype and additional learning tools designed specifically for desktop users.

## Features

- **Kanji Learning** – Study kanji characters with interactive lessons
- **Vocabulary** – Build your Japanese vocabulary
- **User Profile** – Track your learning progress
- **Kumiai Go RPG Prototype** – Exclusive RPG learning experience (desktop only)
- **Subscription Management** – Manage your account subscription
- **Quiz System** – Test your knowledge of kanji (currently in development)

## Prerequisites

Before setting up the desktop application, ensure you have:

- **Java 11+** (or your project's required version)
- **Maven** (for building the project)
- **Spring Boot API** – The Kumiai Go RPG backend service running locally or remotely
- **Database** – A configured database connected to the Spring Boot API
- A running instance of the [Kumiai Go RPG API](https://github.com/Tenbatsu11/Kumiai-Go-RPG)

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Tenbatsu11/Kumiai-Go-Learn-Dekstop.git
   cd Kumiai-Go-Learn-Dekstop
   ```

2. **Build the project**
   ```bash
   mvn clean package
   ```

3. **Configure the API URL** (see [Configuration](#configuration) section below)

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

## Configuration

To connect to your Kumiai Go RPG API:

1. Open: `src/main/java/fr/kumiaigorpg/desktop/api/ApiClient.java`
2. Update the API URL to match your local or remote Spring Boot API instance
3. For detailed API setup instructions, refer to the [Kumiai Go RPG repository](https://github.com/Tenbatsu11/Kumiai-Go-RPG)

## Current Development Status

### Quiz Feature (In Development)

A new quiz system has been added to the dedicated quiz tab. This feature allows users to test their knowledge of kanji characters.

**Current Limitations:**
- No filtering by difficulty level – all kanji are included in the quiz
- No progression system yet

**Planned Improvements:**
- Difficulty level filtering
- Progression system that adapts quiz difficulty based on user performance
- Personalized quiz recommendations

## Known Limitations

- Quiz currently uses all available kanji levels without filtering
- Progression system not yet implemented
- Database must be manually configured before use

## Related Projects

- [Kumiai Go RPG](https://github.com/Tenbatsu11/Kumiai-Go-RPG) – The backend API and RPG engine
- [Kumiai Go Learn Website](https://kumiaigolearn.com) – The web version of the application

---

Last updated: June 4, 2026
