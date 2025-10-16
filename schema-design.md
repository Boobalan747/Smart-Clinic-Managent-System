# Smart Clinic Management System — MySQL Database Schema Design

## 1. Overview
This schema supports the main modules of the system:  
- **User Management** (Admin, Doctor, Patient, etc.)  
- **Appointments**  
- **Medical Records & Prescriptions**  
- **Billing / Invoices**  
- **Inventory (optional)**  

The design follows **3NF normalization** to avoid data redundancy and ensure scalability.

---

## 2. Entity Relationship Diagram (Text Version)

[User]──(1)───(N)──[Patient]
│
├──(1)───(N)──[Doctor]
│
└──(1)───(N)──[Appointment]──(1)───[Prescription]
│
└──(1)───[Invoice]

---

## 3. Table Definitions

### 🧍 users
Stores authentication and role information.

```sql
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  full_name VARCHAR(200),
  email VARCHAR(150) UNIQUE,
  role ENUM('ADMIN', 'DOCTOR', 'RECEPTIONIST', 'PATIENT') NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE doctors (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  specialization VARCHAR(100),
  qualification VARCHAR(100),
  consultation_fee DECIMAL(10,2),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE patients (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  gender ENUM('MALE','FEMALE','OTHER'),
  dob DATE,
  phone VARCHAR(20),
  address TEXT,
  blood_group VARCHAR(10),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);
CREATE TABLE appointments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT NOT NULL,
  doctor_id INT NOT NULL,
  appointment_date DATETIME NOT NULL,
  status ENUM('BOOKED','CANCELLED','COMPLETED') DEFAULT 'BOOKED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
  FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);
CREATE TABLE prescriptions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  appointment_id INT NOT NULL,
  notes TEXT,
  prescribed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);
CREATE TABLE invoices (
  id INT AUTO_INCREMENT PRIMARY KEY,
  appointment_id INT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  payment_status ENUM('PENDING','PAID','CANCELLED') DEFAULT 'PENDING',
  issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);
CREATE TABLE medications (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  stock_quantity INT DEFAULT 0,
  price_per_unit DECIMAL(10,2),
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


