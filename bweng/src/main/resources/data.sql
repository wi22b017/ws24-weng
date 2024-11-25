INSERT INTO airline (id, name) VALUES
(UNHEX(REPLACE(UUID(),'-','')), 'Lufthansa'),
(UNHEX(REPLACE(UUID(),'-','')), 'Air France'),
(UNHEX(REPLACE(UUID(),'-','')), 'British Airways');

INSERT INTO aircraft (id, manufacturer, model, capacity, airline_id, serial_number) VALUES
(UNHEX(REPLACE(UUID(),'-','')), 'Airbus', 'A320', 180, (SELECT id FROM airline WHERE name='Lufthansa'), 'LH12345'),
(UNHEX(REPLACE(UUID(),'-','')), 'Boeing', '747', 400, (SELECT id FROM airline WHERE name='Lufthansa'), 'LH54321'),
(UNHEX(REPLACE(UUID(),'-','')), 'Airbus', 'A321', 220, (SELECT id FROM airline WHERE name='Air France'), 'AF12345'),
(UNHEX(REPLACE(UUID(),'-','')), 'Boeing', '777', 350, (SELECT id FROM airline WHERE name='Air France'), 'AF54321'),
(UNHEX(REPLACE(UUID(),'-','')), 'Airbus', 'A330', 250, (SELECT id FROM airline WHERE name='British Airways'), 'BA12345'),
(UNHEX(REPLACE(UUID(),'-','')), 'Boeing', '787', 300, (SELECT id FROM airline WHERE name='British Airways'), 'BA54321');

INSERT INTO airport (id, code, name) VALUES
(UNHEX(REPLACE(UUID(),'-','')),'FRA', 'Frankfurt Airport'),
(UNHEX(REPLACE(UUID(),'-','')),'CDG', 'Charles de Gaulle Airport'),
(UNHEX(REPLACE(UUID(),'-','')),'LHR', 'Heathrow Airport'),
(UNHEX(REPLACE(UUID(),'-','')),'AMS', 'Schiphol Airport'),
(UNHEX(REPLACE(UUID(),'-','')),'VIE', 'Vienna International Airport'),
(UNHEX(REPLACE(UUID(),'-','')),'ZHR', 'Zurich Airport');

INSERT INTO flight (id, flight_number, departure_time, arrival_time, flight_origin_id, flight_destination_id, aircraft_id) VALUES
(UNHEX(REPLACE(UUID(),'-','')), 'LH1001', '2024-11-25 08:00:00', '2024-11-25 10:00:00',
(SELECT id FROM airport WHERE code='FRA'),
(SELECT id FROM airport WHERE code='VIE'),
(SELECT id FROM aircraft WHERE serial_number='LH12345')),
(UNHEX(REPLACE(UUID(),'-','')), 'LH2002', '2024-11-25 12:00:00', '2024-11-25 14:00:00',
(SELECT id FROM airport WHERE code='VIE'),
(SELECT id FROM airport WHERE code='FRA'),
(SELECT id FROM aircraft WHERE serial_number='LH54321')),
(UNHEX(REPLACE(UUID(),'-','')), 'AF3003', '2024-11-26 09:00:00', '2024-11-26 11:30:00',
(SELECT id FROM airport WHERE code='CDG'),
(SELECT id FROM airport WHERE code='ZHR'),
(SELECT id FROM aircraft WHERE serial_number='AF12345')),
(UNHEX(REPLACE(UUID(),'-','')), 'AF4004', '2024-11-26 15:00:00', '2024-11-26 17:00:00',
(SELECT id FROM airport WHERE code='ZHR'),
(SELECT id FROM airport WHERE code='CDG'),
(SELECT id FROM aircraft WHERE serial_number='AF54321')),
(UNHEX(REPLACE(UUID(),'-','')), 'BA5005', '2024-11-27 07:00:00', '2024-11-27 09:00:00',
(SELECT id FROM airport WHERE code='LHR'),
(SELECT id FROM airport WHERE code='AMS'),
(SELECT id FROM aircraft WHERE serial_number='BA12345')),
(UNHEX(REPLACE(UUID(),'-','')), 'BA6006', '2024-11-27 13:00:00', '2024-11-27 15:30:00',
(SELECT id FROM airport WHERE code='AMS'),
(SELECT id FROM airport WHERE code='LHR'),
(SELECT id FROM aircraft WHERE serial_number='BA54321'));

INSERT INTO baggage_type (id, name, fee) VALUES
(UNHEX(REPLACE(UUID(),'-','')), 'Hand Luggage', 0.00),
(UNHEX(REPLACE(UUID(),'-','')), 'Checked Baggage', 50.00),
(UNHEX(REPLACE(UUID(),'-','')), 'Oversize Baggage', 100.00);

INSERT INTO address (id, street, number, zip, city, country) VALUES
(UNHEX(REPLACE(UUID(),'-','')), 'Main Street', 123, '10115', 'Berlin', 'DE'),
(UNHEX(REPLACE(UUID(),'-','')), 'Queen Street', 45, '10506', 'London', 'GB'),
(UNHEX(REPLACE(UUID(),'-','')), 'Champs-Élysées', 99, '75008', 'Paris', 'FR'),
(UNHEX(REPLACE(UUID(),'-','')), 'Kaerntner Straße', 12, '1010', 'Vienna', 'AT');

INSERT INTO payment_method (id, name) VALUES
(UNHEX(REPLACE(UUID(),'-','')), 'Credit Card'),
(UNHEX(REPLACE(UUID(),'-','')), 'PayPal'),
(UNHEX(REPLACE(UUID(),'-','')), 'Bank Transfer'),
(UNHEX(REPLACE(UUID(),'-','')), 'Apple Pay');

INSERT INTO user (id, gender, first_name, last_name, username, password, email, date_of_birth, role, status, payment_method_id, address_id) VALUES
(UNHEX(REPLACE(UUID(),'-','')), 'male', 'John', 'Doe', 'johndoe', '$2y$10$FmgJoZP8UisX.21IPz.J6.i19FX87BdrBTgYMEKNhLH/YCs6GKsq.', 'john.doe@example.com', '1995-05-15', 'USER', 'ACTIVE',
(SELECT id FROM payment_method WHERE name='Credit Card'),
(SELECT id FROM address WHERE street='Main Street')),
(UNHEX(REPLACE(UUID(),'-','')), 'female', 'Jane', 'Smith', 'janesmith', '$2y$10$R8so6DZwQPmOM97Fsqvu2eq2zUlD2QGm5YU/KXKmzBD.6XpMglqGK', 'jane.smith@example.com', '1991-05-15', 'USER', 'ACTIVE',
(SELECT id FROM payment_method WHERE name='PayPal'),
(SELECT id FROM address WHERE street='Queen Street')),
(UNHEX(REPLACE(UUID(),'-','')), 'male', 'Admin', 'User', 'admin', '$2y$10$QqzzRscRDbrWBsXbfWxvaufOFk4m6B.A37JhuL6RGNsXc/s5mHpMC', 'admin@example.com','1985-06-15', 'ADMIN', 'ACTIVE',
(SELECT id FROM payment_method WHERE name='Bank Transfer'),
(SELECT id FROM address WHERE street='Champs-Élysées')),
(UNHEX(REPLACE(UUID(),'-','')), 'female', 'Emily', 'Johnson', 'emilyj', '$2y$10$69kexWDnfN/CztYPzdTp/.8bKd2nR174gOXNJUU8rIA7kpyoWvOlq', 'emily.johnson@example.com', '1989-05-15', 'USER', 'ACTIVE',
(SELECT id FROM payment_method WHERE name='Apple Pay'),
(SELECT id FROM address WHERE street='Kaerntner Straße'));

