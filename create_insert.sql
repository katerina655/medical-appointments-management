-- Δημιουργία χρήστη βάσης 
GRANT ALL PRIVILEGES ON DATABASE medicaldb TO medical_user;

-- Πίνακας Users 
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    password_salt VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    role VARCHAR(20) NOT NULL -- PATIENT / DOCTOR / ADMIN
);

-- Ασθενείς 
CREATE TABLE patients (
    patient_id SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
    amka CHAR(11) UNIQUE NOT NULL,
    address VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL
);

-- Γιατροί 
CREATE TABLE doctors (
    doctor_id SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
    specialty VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    office VARCHAR(100) NOT NULL
);

-- Διαχειριστές 
CREATE TABLE admins (
    admin_id SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
    role_description VARCHAR(100) NOT NULL
);

-- Διαθεσιμότητες γιατρών
CREATE TABLE availabilities (
    availability_id SERIAL PRIMARY KEY,
    doctor_id INT REFERENCES doctors(doctor_id) ON DELETE CASCADE,
    available_date_time TIMESTAMP NOT NULL
);

-- Ραντεβού
CREATE TABLE appointments (
    appointment_id SERIAL PRIMARY KEY,
    patient_id INT REFERENCES patients(patient_id) ON DELETE CASCADE,
    doctor_id INT REFERENCES doctors(doctor_id) ON DELETE CASCADE,
    date_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, COMPLETED, CANCELED
    created_at TIMESTAMP DEFAULT NOW()
);



-- Χρήστες δοκιμαστικοί
INSERT INTO users (username, password_hash, password_salt, name, surname, gender, role)
VALUES
  ('john.p',  'plain1234', 'salt', 'John',  'Papadopoulos', 'MALE',   'PATIENT'),
  ('maria.k', 'plain1234', 'salt', 'Maria', 'Kostopoulou',  'FEMALE', 'PATIENT'),
  ('dr.h',    'plain1234', 'salt', 'Helen', 'James',        'FEMALE', 'DOCTOR'),
  ('dr.n',    'plain1234', 'salt', 'Nick',  'Georgiou',     'MALE',   'DOCTOR'),
  ('admin1',  'plainadmin','salt', 'Mary',  'Smith',        'FEMALE', 'ADMIN');

-- Ασθενείς
INSERT INTO patients (user_id, amka, address, phone)
VALUES
  ((SELECT user_id FROM users WHERE username='john.p'),  '12345678910', 'Athens, Greece', '+30 2100000001'),
  ((SELECT user_id FROM users WHERE username='maria.k'), '12345678911', 'Athens, Greece', '+30 2100000002');

-- Γιατροί
INSERT INTO doctors (user_id, specialty, phone, office)
VALUES
  ((SELECT user_id FROM users WHERE username='dr.h'), 'Pathology',  '+30 2101001001', 'Clinic A, 1st floor'),
  ((SELECT user_id FROM users WHERE username='dr.n'), 'Cardiology', '+30 2101001002', 'Clinic B, 2nd floor');

-- Διαχειριστές
INSERT INTO admins (user_id, role_description)
VALUES
  ((SELECT user_id FROM users WHERE username='admin1'), 'System administrator');

-- Διαθεσιμότητες
INSERT INTO availabilities (doctor_id, available_date_time)
VALUES
  ((SELECT doctor_id FROM doctors d JOIN users u ON d.user_id=u.user_id WHERE u.username='dr.h'), NOW() + INTERVAL '5 days'),
  ((SELECT doctor_id FROM doctors d JOIN users u ON d.user_id=u.user_id WHERE u.username='dr.h'), NOW() + INTERVAL '6 days'),
  ((SELECT doctor_id FROM doctors d JOIN users u ON d.user_id=u.user_id WHERE u.username='dr.n'), NOW() + INTERVAL '7 days');

-- Ραντεβού 
INSERT INTO appointments (patient_id, doctor_id, date_time, status)
VALUES
 ((SELECT patient_id FROM patients p JOIN users u ON p.user_id=u.user_id WHERE u.username='john.p'),
  (SELECT doctor_id FROM doctors d JOIN users u ON d.user_id=u.user_id WHERE u.username='dr.h'),
  NOW() - INTERVAL '30 days', 'COMPLETED');

INSERT INTO appointments (patient_id, doctor_id, date_time, status)
VALUES
 ((SELECT patient_id FROM patients p JOIN users u ON p.user_id=u.user_id WHERE u.username='maria.k'),
  (SELECT doctor_id FROM doctors d JOIN users u ON d.user_id=u.user_id WHERE u.username='dr.n'),
  NOW() + INTERVAL '7 days', 'SCHEDULED');

SELECT * FROM users;
SELECT * FROM patients;
SELECT * FROM doctors;
SELECT * FROM admins;
SELECT * FROM appointments;
SELECT * FROM availabilities;


UPDATE users
SET password_hash = 'iCjv0uUzqYrAVZ7K0vQ+8HRVgjJoo67AA00WzdrHEuU=',
    password_salt = 'KxjzjjyFIJ3fbf7hGnhAiA=='
WHERE username = 'john.p';


SELECT * FROM users LIMIT 10;
SELECT * FROM doctors;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO medical_user;
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA public TO medical_user;

ALTER TABLE appointments
ADD COLUMN payment_method VARCHAR(20) NOT NULL DEFAULT 'CASH';


DELETE FROM users WHERE role = 'PATIENT';

