# Mobile Temp - IA 真棒

一个简单的 React Native 应用，在手机屏幕上显示 "IA 真棒"。

## 项目信息

- **项目名称**: mobile_tepm
- **React Native 版本**: 0.80.0
- **支持平台**: Android 和 iOS
- **最低 Android SDK**: 24 (Android 7.0)
- **目标 Android SDK**: 35 (Android 15)
- **iOS 最低版本**: iOS 13.0

## 功能特性

- 🎨 现代化 UI 设计
- 🌙 支持深色/浅色主题
- 📱 响应式布局
- 🔄 自动主题切换

## 运行要求

### Android
- Android Studio
- Android SDK 35
- Android NDK 27.1.12297006
- Java 17 或更高版本

### iOS
- Xcode 15 或更高版本
- iOS 13.0 或更高版本
- CocoaPods

## 安装和运行

### 1. 安装依赖

```bash
npm install
```

### 2. 启动 Metro 服务器

```bash
npm start
```

### 3. 运行 Android 应用

```bash
npm run android
```

### 4. 运行 iOS 应用

```bash
npm run ios
```

## 项目结构

```
mobile_tepm/
├── android/          # Android 原生代码
├── ios/             # iOS 原生代码
├── App.tsx          # 主应用组件
├── index.js         # 应用入口点
├── package.json     # 项目依赖
└── README.md        # 项目说明
```

## 开发说明

- 应用使用 TypeScript 开发
- 支持热重载开发
- 使用 ESLint 进行代码检查
- 使用 Prettier 进行代码格式化

## 构建发布版本

### Android APK
```bash
cd android
./gradlew assembleRelease
```

### iOS
在 Xcode 中选择 Product > Archive

## 技术栈

- React Native 0.80.0
- React 19.1.0
- TypeScript 5.0.4
- Metro Bundler
- Hermes JavaScript Engine

## 许可证

MIT License
