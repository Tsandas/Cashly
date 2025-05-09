💸 Cashly – Personal Finance & Investment Tracker
Cashly is a mobile app developed as part of a university project.
It enables users to track their daily expenses and manage a basic stock portfolio, all from a simple, user-friendly interface.
The app supports offline access for transactions and syncs data using Firebase Firestore, making personal finance easy and accessible.

https://github.com/user-attachments/assets/2e8960ad-2c0e-4b35-bf92-6d11c3fd8b31

📱 Features
✅ Expense Tracking
Log daily transactions to get a clear view of your spending.

📈 Stock Portfolio Management
Add and monitor your stock investments (no real-time market data).
Due to API limitations, only a limited selection of stocks is available, and data is updated with end-of-day prices only.

🔐 User Identification with Firebase
Firebase Authentication is used to link each user with their personal data.

🌐 Cloud Sync with Firestore
User data is stored in Firebase Firestore for syncing across devices.

📦 Offline Support (Transactions Only)
Transactions can be added and viewed offline using Room Database. They sync when the user goes online.