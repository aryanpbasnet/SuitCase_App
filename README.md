# SuitCase App

## Overview

**SuitCase** is an Android application designed to help users efficiently manage their holiday shopping lists. The app simplifies the process of creating, editing, organizing, and tracking items required for holidays, helping users stay organised and avoid last-minute stress.

The application allows users to maintain detailed item records, mark items as purchased, and share item information with others for collaborative shopping.

---

## Features

### User Registration & Sign In

* Secure user account creation and authentication
* User credentials (email, password, unique user ID) stored locally
* Authentication handled using a **SQLite database**

---

### Adding Items

* Users can add items with:

  * Item name
  * Price
  * Description
  * Image
* Integrated image selection using an external image picker library

**External dependency used:**

```
implementation 'com.github.dhaval2404:imagepicker:2.1'
```

---

### Editing Items

* Users can modify item details such as:

  * Name
  * Price
  * Description
  * Image
* Editing is performed via a dedicated **Item Detail** screen

---

### Deleting Items

* Swipe left to delete an item
* Confirmation prompt prevents accidental deletion
* Visual swipe feedback with a red background

**External dependency used:**

```
implementation 'it.xabaras.android:recyclerview-swipedecorator:1.4'
```

> Deleted items are permanently removed from the database and cannot be restored.

---

### Marking Items as Purchased

* Swipe right to mark an item as purchased
* Purchased items are visually indicated with a green background
* Item status is updated in the SQLite database

---

### Item Delegation (Sharing)

* Users can share item details (name, price, description) via SMS
* Designed to delegate purchasing tasks to friends or family members

---

## Artwork Credits

* The application uses artwork sourced from **IconScout**
* Assets are used under the **IconScout Digital License**
* All artworks have been **modified and customised** to fit the app’s design requirements

---

## Technologies Used

* **Android (Java)**
* **SQLite** for local data storage

---

## Academic Context

This project was developed as part of an **undergraduate coursework assignment** in a Computer Systems / Software Engineering program.

The application was implemented independently to meet the academic requirements defined in the assessment brief.
It is shared publicly on GitHub **for portfolio and educational demonstration purposes only**.

---

## License

The **original source code and system design** for this project were created by the author and remain the author’s intellectual property.

Third-party assets and libraries are subject to their respective licenses.
