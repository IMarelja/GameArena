-- Dev/test seed data for the users table.
-- password_hash/password_salt are placeholders (no real auth hashing is wired up yet)
-- and are NOT valid credentials for logging in.

INSERT INTO users (username, email, password_hash, password_salt, is_active, created_at) VALUES
	('mvidovic',   'mvidovic@example.com',   'seed$hash$0001', 'seed$salt$0001', TRUE,  CURRENT_TIMESTAMP - INTERVAL '90 days'),
	('ana.horvat',  'ana.horvat@example.com', 'seed$hash$0002', 'seed$salt$0002', TRUE,  CURRENT_TIMESTAMP - INTERVAL '75 days'),
	('ivan_k',      'ivan.k@example.com',     'seed$hash$0003', 'seed$salt$0003', TRUE,  CURRENT_TIMESTAMP - INTERVAL '60 days'),
	('petra99',     'petra99@example.com',    'seed$hash$0004', 'seed$salt$0004', TRUE,  CURRENT_TIMESTAMP - INTERVAL '45 days'),
	('domagoj',     'domagoj@example.com',    'seed$hash$0005', 'seed$salt$0005', FALSE, CURRENT_TIMESTAMP - INTERVAL '30 days'),
	('luka.b',      'luka.b@example.com',     'seed$hash$0006', 'seed$salt$0006', TRUE,  CURRENT_TIMESTAMP - INTERVAL '20 days'),
	('nikolina',    'nikolina@example.com',   'seed$hash$0007', 'seed$salt$0007', TRUE,  CURRENT_TIMESTAMP - INTERVAL '10 days'),
	('admin',       'admin@gamearena.hr',     'seed$hash$0008', 'seed$salt$0008', TRUE,  CURRENT_TIMESTAMP - INTERVAL '5 days');
