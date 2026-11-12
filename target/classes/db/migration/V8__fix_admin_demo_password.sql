UPDATE users
SET password_hash = '$2a$10$Ofc2ayyytKaorz3kvyBPUOnOYbeyYlWsfzMatSAAoz5MAv4gV.d82'
WHERE email = 'admin@demo.hiretrack.local' AND role = 'ADMIN';
