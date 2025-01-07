# Cell Tower Information Collection

This project is an Android application developed in Android Studio to collect and analyze cell tower information for networks such as LTE, 5G, and other technologies. The application focuses on gathering data to identify potential fake base stations and to provide insights into mobile network performance.

## Features
- Collects cell tower information for LTE, 5G, and other supported networks.
- Provides detailed signal metrics such as signal strength, frequency, and bandwidth.
- Supports analysis of collected data for identifying anomalies.
- User-friendly interface for real-time monitoring and data collection.

## Prerequisites
- **Android Studio**: Latest version
- **Kotlin**: Project uses Kotlin as the primary language.
- **Android Device**: A device running Android 8.0 (Oreo) or later with cellular connectivity.
- **Permissions**: Ensure the app has the following permissions:
  - ACCESS_FINE_LOCATION
  - ACCESS_COARSE_LOCATION
  - READ_PHONE_STATE

## Setup and Installation
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Open the project in Android Studio.
3. Sync the project to download dependencies.
4. Build and run the app on a physical Android device (emulators may not support cellular data collection).

## Network Information Collected
The app collects the following network metrics:

| **Network Type** | **Parameter**              | **Description**                                         |
|-------------------|----------------------------|---------------------------------------------------------|
| **LTE**          | RSRP (Reference Signal Received Power) | Measures signal strength.                            |
|                   | RSRQ (Reference Signal Received Quality) | Measures signal quality.                             |
|                   | SINR (Signal-to-Interference-plus-Noise Ratio) | Measures signal-to-noise ratio.                     |
|                   | Frequency                  | Operating frequency of the cell tower.                 |
|                   | Bandwidth                  | Available bandwidth of the cell tower.                 |
| **5G**           | NR-ARFCN (New Radio Absolute Radio Frequency Channel Number) | Identifies the frequency used. |
|                   | SS-RSRP (Reference Signal Received Power) | Measures signal strength for 5G.                   |
|                   | SS-SINR (Signal-to-Interference-plus-Noise Ratio) | Measures signal quality.                           |
|                   | PCI (Physical Cell ID)     | Identifies the cell tower.                             |
| **General**       | MCC (Mobile Country Code)  | Identifies the country of the network.                 |
|                   | MNC (Mobile Network Code)  | Identifies the network operator.                       |
|                   | Cell ID                    | Unique identifier for the cell tower.                  |
|                   | Signal Strength            | Overall signal strength reported by the device.        |

## How It Works
1. The app uses Android's Telephony APIs and permissions to collect cell tower information.
2. The data is processed and displayed in a user-friendly interface.
3. Users can export collected data for offline analysis.

## Usage
1. Launch the app on an Android device.
2. Grant the necessary permissions.
3. Navigate to the "Live Data" tab to monitor real-time cell tower information.
4. Use the "Export Data" feature to save collected information for further analysis.

## Contributing
1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes and push the branch:
   ```bash
   git push origin feature-name
   ```
4. Submit a pull request.

## License
This project is licensed under the [MIT License](LICENSE).

---

For any issues or feature requests, please open an issue in the repository or contact the project maintainers.

