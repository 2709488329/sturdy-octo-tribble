# 领养小奶狗 - Android App 🐾

「领养小奶狗」是一款放置点击游戏。经营流浪狗救助站，招募员工，与"小紫"一起经历温馨故事。

## 技术架构

- **Android WebView 封装** — 游戏核心代码是一份完整的 HTML+CSS+JS 单页应用
- **assets/index.html** — 完整的游戏逻辑（439行，包含界面渲染、剧情事件、计时器、调试面板）
- **自适应图标** — Adaptive Icon + 矢量狗狗爪印

## 构建方式

### 方式一：GitHub Actions（推荐）

1. 将本项目推送到 GitHub 仓库
2. 前往 Actions → Build APK → Run workflow
3. 下载生成的 APK artifact

### 方式二：本地构建（需要 Android SDK）

```bash
# 确保安装了 Android SDK 和 JDK 17+
export ANDROID_HOME=$HOME/Library/Android/sdk
chmod +x gradlew
./gradlew assembleDebug
```

APK 输出位置：`app/build/outputs/apk/debug/app-debug.apk`

### 方式三：在线构建（无需本地工具）

使用 [Appetize.io](https://appetize.io) 或 [Expo](https://expo.dev) 等服务在线构建 APK。

## 游戏功能

- 🐾 **救助** — 点击救助流浪狗（需要达到一定点击次数）
- 📢 **宣传** — 消耗流浪狗获得声望
- 👥 **员工系统** — 爱护者（自动救助）、志愿者（自动宣传）、看守者（增加上限）
- ⭐ **深造升级** — 提高救助/宣传/声望获取效率
- 🎁 **道具系统** — 黄金体验、彩票、幸运手链、特制狗粮、纪念徽章
- 💎 **钻石系统** — 钻石招募、钻石兑换、概率提升
- 💗 **小紫剧情线** — 8个温馨事件（初遇→温暖的探访→宠物义诊→深夜长谈→小紫的抉择→意外的惊喜→心灵相通→一生之约）
- 🔧 **调试面板** — 密码 7813

## 项目结构

```
PuppyAdoptWebApp/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── assets/
│       │   │   └── index.html          # 游戏核心（HTML+CSS+JS）
│       │   ├── java/com/puppyadopt/webapp/
│       │   │   └── MainActivity.java   # WebView 封装
│       │   └── res/
│       │       ├── drawable/           # 启动图标（矢量）
│       │       ├── mipmap-*/           # 自适应图标
│       │       └── values/             # 主题、字符串
│       └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/wrapper/
│   └── gradle-wrapper.properties
├── gradlew
├── local_build.sh
└── .github/workflows/build.yml
```
