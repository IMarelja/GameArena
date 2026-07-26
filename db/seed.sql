-- Dev/test seed data for the users table.
-- password_hash/password_salt are placeholders (no real auth hashing is wired up yet)
-- and are NOT valid credentials for logging in.

INSERT INTO users (username, email, password_hash, password_salt, is_active, created_at) VALUES
	-- Admin user
	('admin',
	'admin@gamearena.hr',
	'8d6602915c18ffec80a48d15a2c7c1c391406cd336553cb7a2e71b8ee0f14672', -- password
	'cd7cbc9f2abd7cefec5c22a9f2fe1373',
	TRUE,
	CURRENT_TIMESTAMP - INTERVAL '5 days'),
	-- First regular user account
	('ivan_m',
	'ivan.marelja@hello.hr',
	'0b0ae26ba604ed3121dbe75202de14efc1a586a9eec82525e815a7266c224312', -- helloworld
	'1a51f517df9e1e639559e29d63ab8346',
	TRUE,
	CURRENT_TIMESTAMP - INTERVAL '60 days'),
	-- Second regular user account
	('super_gamer',
	'tom.fynder@gmail.com',
	'68e26bfd9a7e9bb56db9275ad85d51ad8bc643a95544494b435d6c4d26edaaaf', -- 1passworld2
	'0de7d7410e404cf59fa375defec8070b',
	TRUE,
	CURRENT_TIMESTAMP - INTERVAL '45 days'),
	-- Suspended account
	('evil_gamer',
	'burner.email@crocofile.com',
	'baea65b797361c8665d940c9ce45146c4f10e2e49ec7bec2055f5b3ab2778523', -- evilhello
	'dad660e9c4f09da2d877495c2de0c200',
	FALSE,
	CURRENT_TIMESTAMP - INTERVAL '5 days'),
	-- Seed dummy data
	('mvidovic',	'mvidovic@example.com',   'seed$hash$0001', 'seed$salt$0001', TRUE,  CURRENT_TIMESTAMP - INTERVAL '90 days'),
	('ana.horvat',	'ana.horvat@example.com', 'seed$hash$0002', 'seed$salt$0002', TRUE,  CURRENT_TIMESTAMP - INTERVAL '75 days'),
	('ivan_k',	'ivan.k@example.com',     'seed$hash$0003', 'seed$salt$0003', TRUE,  CURRENT_TIMESTAMP - INTERVAL '60 days'),
	('petra99',	'petra99@example.com',    'seed$hash$0004', 'seed$salt$0004', TRUE,  CURRENT_TIMESTAMP - INTERVAL '45 days'),
	('domagoj',	'domagoj@example.com',    'seed$hash$0005', 'seed$salt$0005', FALSE, CURRENT_TIMESTAMP - INTERVAL '30 days'),
	('luka.b',	'luka.b@example.com',     'seed$hash$0006', 'seed$salt$0006', TRUE,  CURRENT_TIMESTAMP - INTERVAL '20 days'),
	('nikolina',	'nikolina@example.com',   'seed$hash$0007', 'seed$salt$0007', TRUE,  CURRENT_TIMESTAMP - INTERVAL '10 days');
	
