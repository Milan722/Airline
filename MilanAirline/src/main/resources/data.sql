-- Insert default roles if they don't exist (PostgreSQL compatible)
INSERT INTO roles (name) VALUES ('CUSTOMER') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ADMIN') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('PILOT') ON CONFLICT (name) DO NOTHING;

