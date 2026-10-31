# SanderHexDump: Native Mobile Hex Editor

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)

SanderHexDump is a lightweight, zero-dependency Hex Editor & Reverse Engineering tool built entirely on Android using Termux. It is written in 100% Native Java without Gradle or Android Studio, designed for security researchers and reverse engineers who need a portable hex viewer on the go.

## Features

- **Classic Hex View:** 12-byte per line with Offset, Hex, and ASCII representation side-by-side.
- **Color Highlighting:** Blue for Offset, White for Hex, and Gray for non-printable ASCII. Optimized for readability.
- **Bold Monospace Font:** Tuned for 720p mobile screens to prevent text wrapping.
- **Copy Log:** Easily copy the entire hex dump to your clipboard for reporting.
- **BackTrack-Style UI:** Pure black background with a terminal-like interface. No bloated UI elements.

## Usage

1. Open the SanderHexDump terminal.
2. Type the following command:
   ```bash
   hex /sdcard/Download/yourfile.apk

3. Replace yourfile.apk with the actual path of the file you want to inspect.
4. Press Enter. The hex dump will be displayed.

Installation

1. Download the latest SanderHexDump.apk from the Releases page.
2. Enable "Install from Unknown Sources" on your Android device.
3. Install the APK and grant Storage permission (to read files).

Build from Source (Termux)

(Coming soon in v1.1)

License

This project is licensed under the GNU Affero General Public License v3.0 (AGPLv3).
You are free to use, modify, and distribute this software, but you MUST keep it open-source and give credit to the original author. Closed-source commercial use is strictly prohibited.

See the LICENSE file for details.

Credits

· Creator & Lead Developer: Aleksander Titan Pabiyuri (aleksandergrambelik-png)
· Co-Pilot & Assistant: DeepSeek AI
