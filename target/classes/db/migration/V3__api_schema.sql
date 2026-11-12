CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY, email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, role VARCHAR(30) NOT NULL DEFAULT 'CANDIDATE',
    candidate_id BIGINT REFERENCES candidates(id), recruiter_id BIGINT REFERENCES recruiters(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TABLE IF NOT EXISTS profiles (
    id BIGSERIAL PRIMARY KEY, user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    headline VARCHAR(200), bio TEXT, resume_url VARCHAR(500), skills TEXT, updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY, user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL, message TEXT NOT NULL, read_at TIMESTAMPTZ, created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TABLE IF NOT EXISTS interviews (
    id BIGSERIAL PRIMARY KEY, application_id BIGINT NOT NULL REFERENCES applications(id) ON DELETE CASCADE,
    scheduled_at TIMESTAMPTZ NOT NULL, mode VARCHAR(50) NOT NULL DEFAULT 'Video', location VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'scheduled', notes TEXT
);
CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_interviews_application ON interviews(application_id);
