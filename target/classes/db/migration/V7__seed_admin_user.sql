INSERT INTO users(email,password_hash,role)
SELECT 'admin@demo.hiretrack.local',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@demo.hiretrack.local');
