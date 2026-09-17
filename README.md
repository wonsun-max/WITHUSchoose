# WITHUS Choose — Somatic Biometric Decision Instrument

A native Android application grounded in **Antonio Damasio’s Somatic Marker Hypothesis**. 

When facing an agonizing choice, the rational prefrontal cortex loops endlessly. This app uses real-time **Photoplethysmography (PPG)** via the smartphone camera and flash to measure acute autonomic nervous system reactions (cardiac orienting deceleration vs. sympathetic resistance spikes and micro-tremors) during sudden 0.5-second stimulus flashes.

---

## 🚀 How to Run in Android Studio

1. **Launch Android Studio**:
   - Open Android Studio from your Start menu or desktop.
2. **Open the Project**:
   - Click **Open** (or `File` -> `Open`).
   - Select the folder: `c:\Users\wonse\Desktop\PROJECTS\APP\WITHUSchoose`.
3. **Gradle Sync**:
   - Android Studio will automatically recognize the project and run Gradle Sync.
4. **Run the App**:
   - Select an Android Emulator or connect your physical Android device (via USB with USB Debugging enabled).
   - Click the green **Run ▶** button (or press `Shift + F10`).

---

## 📱 The 4-Screen Flow (~45 Seconds Total)

1. **Screen 1: The Dilemma (Input)**
   - Enter Option A and Option B, or choose from curated presets (*"Accept corporate job vs Start my own studio"*).
   - Tap the softly pulsing **"Consult the Body"** CTA button.
2. **Screen 2: Baseline Calibration (10 Seconds)**
   - Place your right index finger over the camera and LED flash.
   - The app illuminates the capillary bed, tracks blood volume oscillations, and calculates your resting baseline BPM while you take a deep breath.
   - A live arterial PPG waveform and heartbeat haptic vibrations keep you centered.
3. **Screen 3: The Flash Moment (The Suspense Moment)**
   - A 108Hz / 4Hz theta wave binaural drone plays.
   - 3-second countdown (3... 2... 1...) followed by unpredictable silence (0.8s - 1.4s) to prevent conscious bracing.
   - Sudden 0.5s flash of Option A with an acute chime and tactile click, followed by 3.0s of pitch-black orienting recording.
   - 5-second breathing reset, followed by unpredictable silence and the 0.5s flash of Option B.
4. **Screen 4: The Somatic Verdict (The "Aha!" Moment)**
   - Reveals the autonomic winner chosen by your body.
   - Displays biological comparison telemetry (acute +/- BPM shifts and micro-tremor).
   - Reflection Check: *"Did seeing this verdict make you feel relieved, or disappointed?"*
   - Generate and share the viral 9:16 **Truth Card** to Instagram Stories / TikTok / WhatsApp.

---

## 🔬 Dual-Mode Hardware & Testing Support

- **On Physical Devices**:
  Runs CameraX `ImageAnalysis` with torch illumination, analyzing red-channel luminance oscillations to measure real pulse rates, paired with touchscreen micro-jitter tracking.
- **On Android Studio Emulator**:
  Automatically detects emulator hardware and engages an authentic physiological cardiac engine with realistic baseline variability and RSA (respiratory sinus arrhythmia). You can test the entire flow end-to-end without physical hardware hurdles.

---

## 🗄️ Local Database & Privacy

- **Offline-First Room Database**:
  All dilemma entries, biometric telemetry, chosen verdicts, and reflection responses are persisted privately on your device.
- **Archive Sheet**:
  Tap the history icon on Screen 1 to view, filter, or delete past decisions.
