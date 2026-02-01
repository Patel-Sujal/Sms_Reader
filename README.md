# 🔊 SMS Payment Speaker (Android Application)

An Android application that automatically detects incoming bank transaction SMS messages and announces credited payment amounts using Text-to-Speech (TTS).  
Designed to help shop owners and users receive instant voice alerts for payments without checking the phone screen.

---

## 🎯 Project Objective

The main objective of this project is to:

- Provide hands-free payment confirmation  
- Reduce manual checking of SMS notifications  
- Improve real-time transaction awareness  
- Assist shopkeepers during busy operations  

---

## 🚀 Features

- Automatic detection of bank credit SMS  
- Keyword-based message filtering (example: "credited")  
- Real-time voice announcement using Text-to-Speech  
- Works in background even when screen is off  
- Lightweight and fast processing  

---

## 🛠 Tech Stack

- Kotlin  
- Android Studio  (Electric eel)
- BroadcastReceiver  
- SMS API  
- Text-to-Speech (TTS)  
- Runtime Permissions  

---

## 📱 How It Works

1. App listens for incoming SMS messages  
2. Filters messages based on keywords  
3. Extracts transaction amount  
4. Converts text into voice output  
5. Announces payment instantly  

---

## ⚙ Setup Instructions

1. Open project in Android Studio  
2. Allow SMS and Notification permissions  
3. Enable background execution permission  
4. Run application on real device  
5. Send test SMS containing "credited" keyword  

---

## 📄 License

This project is developed for educational and demonstration purposes only.
