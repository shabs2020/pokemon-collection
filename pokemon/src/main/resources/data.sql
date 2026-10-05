-- Seed trainers with BCrypt hashes for 'pikachu123' and 'starmie123'
MERGE INTO trainer (username, password_hash) KEY (username) VALUES
('ash', '$2a$10$fvX2BZv.pwfrBfY4Y1p.E.6E9EClvoQ.Ys6gWBAQVFosj85KiUy4W'),
('misty', '$2a$10$zn13jeP1WNK4I2q9/fZVBe5ub1YMHTyJmwofgVkft/NIF8/8TnETe');
