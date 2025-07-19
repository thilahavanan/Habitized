# Contributing to Habitized 🤝

Thank you for your interest in contributing to Habitized! This document provides guidelines and information for contributors.

## 📋 Table of Contents

- [🚀 Getting Started](#-getting-started)
- [🛠 Development Setup](#-development-setup)
- [📤 Submitting Changes](#-submitting-changes)
- [🐛 Issue Guidelines](#-issue-guidelines)

## 🚀 Getting Started

### Prerequisites

- **Android Studio** (Latest stable version)
- **Kotlin** 1.9+
- **Gradle** 8+
- **Android SDK** (API 26+)
- **Git**

## 🛠 Development Setup

### Environment Setup

1. **Clone and Setup**
   ```bash
   git clone https://github.com/codewithdipesh/habitized.git
   cd habitized
   ```

2. **Install Dependencies**
   - Open in Android Studio
   - Let Gradle sync complete
   - Install any missing SDK components if prompted


## 📤 Submitting Changes

### ⚠️ IMPORTANT: Proper Git Workflow

**ALWAYS follow this exact sequence to avoid conflicts:**

1. **Update your local main branch**
   ```bash
   git checkout master
   git pull origin master
   ```

2. **Create a NEW branch from updated main**
   ```bash
   git checkout -b feat/{issueNumber}-your-feature-name
   ```

3. **Make Your Changes**
   - Follow coding guidelines
   - Update documentation if needed

4. **Commit Your Changes**
   ```bash
   git add .
   git commit -m "feat: add new habit tracking feature"
   ```

5. **Push and Create PR**
   ```bash
   git push origin feat/your-feature-name
   ```

### ❌ Common Mistakes to Avoid

- **Don't commit to master branch directly**
- **Don't create new branches from old master**
- **Don't include previous commits in your PR**
- **Don't work on multiple issue in one branch**


### Commit Message Format

```

feat(habit): add s functionality
fix(ui): resolve bug/issue
docs(readme): update  instructions
style(theme): improve color consistency
enhancement : improve app 
chore: small code chnages (tiny)
```

### PR Guidelines

- **Title**: Clear, descriptive title
- **Description**: Explain what and why (not how)
- **Screenshots**: Include UI changes if applicable

## 🐛 Issue Guidelines

### Bug Reports

When reporting bugs, pls include:

- **Device/OS**: Android version, device model
- **App Version**: Current version number
- **Steps to Reproduce**: Clear, numbered steps
- **Expected vs Actual**: What should happen vs what happens
- **Screenshots**: If applicable
- **Logs**: Error logs if available

---

Thanks again for your contribution! 🙌  
Let’s build something amazing together. 🚀 
