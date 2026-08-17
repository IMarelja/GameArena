-- Dev/test seed data for the users table.
-- password_hash/password_salt are placeholders (no real auth hashing is wired up yet)
-- and are NOT valid credentials for logging in.



DO $$
DECLARE
	-- Users
	admin_id	BIGINT;
	ivan_m_id	BIGINT;
	super_gamer_id	BIGINT;
	evil_gamer_id	BIGINT;
	mvidovic_id	BIGINT;
	ana_horvat_id	BIGINT;
	ivan_k_id	BIGINT;
	petra99_id	BIGINT;
	domagoj_id	BIGINT;
	luka_b_id	BIGINT;
	nikolina_id	BIGINT;

	-- Games
	smbm_id		BIGINT;
	tf2_id		BIGINT;
	fortnite_id	BIGINT;

	-- Team
	legion_team_id	BIGINT;
BEGIN
	-- ------------
	-- 👤 USERS
	-- ------------
	-- Admin user
	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('admin',
		'admin@gamearena.hr',
		'8d6602915c18ffec80a48d15a2c7c1c391406cd336553cb7a2e71b8ee0f14672', -- password
		'cd7cbc9f2abd7cefec5c22a9f2fe1373',
		'ADMIN',
		TRUE,
		CURRENT_TIMESTAMP - INTERVAL '5 days')
	RETURNING id INTO admin_id;

	-- First regular user account
	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('ivan_m',
		'ivan.marelja@hello.hr',
		'0b0ae26ba604ed3121dbe75202de14efc1a586a9eec82525e815a7266c224312', -- helloworld
		'1a51f517df9e1e639559e29d63ab8346',
		'USER',
		TRUE,
		CURRENT_TIMESTAMP - INTERVAL '60 days')
	RETURNING id INTO ivan_m_id;

	-- Second regular user account
	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('super_gamer',
		'tom.fynder@gmail.com',
		'68e26bfd9a7e9bb56db9275ad85d51ad8bc643a95544494b435d6c4d26edaaaf', -- 1passworld2
		'0de7d7410e404cf59fa375defec8070b',
		'USER',
		TRUE,
		CURRENT_TIMESTAMP - INTERVAL '45 days')
	RETURNING id INTO super_gamer_id;

	-- Suspended account
	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('evil_gamer',
		'burner.email@crocofile.com',
		'baea65b797361c8665d940c9ce45146c4f10e2e49ec7bec2055f5b3ab2778523', -- evilhello
		'dad660e9c4f09da2d877495c2de0c200',
		'USER',
		FALSE,
		CURRENT_TIMESTAMP - INTERVAL '5 days')
	RETURNING id INTO evil_gamer_id;

	-- Seed dummy data
	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('mvidovic', 'mvidovic@example.com', 'seed$hash$0001', 'seed$salt$0001', 'USER', TRUE, CURRENT_TIMESTAMP - INTERVAL '90 days')
	RETURNING id INTO mvidovic_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('ana.horvat', 'ana.horvat@example.com', 'seVLUESed$hash$0002', 'seed$salt$0002', 'USER', TRUE, CURRENT_TIMESTAMP - INTERVAL '75 days')
	RETURNING id INTO ana_horvat_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('ivan_k', 'ivan.k@example.com', 'seed$hash$0003', 'seed$salt$0003', 'USER', TRUE, CURRENT_TIMESTAMP - INTERVAL '60 days')
	RETURNING id INTO ivan_k_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('petra99', 'petra99@example.com', 'seed$hash$0004', 'seed$salt$0004', 'USER', TRUE, CURRENT_TIMESTAMP - INTERVAL '45 days')
	RETURNING id INTO petra99_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('domagoj', 'domagoj@example.com', 'seed$hash$0005', 'seed$salt$0005', 'USER', FALSE, CURRENT_TIMESTAMP - INTERVAL '30 days')
	RETURNING id INTO domagoj_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('luka.b', 'luka.b@example.com', 'seed$hash$0006', 'seed$salt$0006', 'USER', TRUE, CURRENT_TIMESTAMP - INTERVAL '20 days')
	RETURNING id INTO luka_b_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('nikolina', 'nikolina@example.com', 'seed$hash$0007', 'seed$salt$0007', 'USER', TRUE, CURRENT_TIMESTAMP - INTERVAL '10 days')
	RETURNING id INTO nikolina_id;

	-- -----------
	-- 🎮 GAMES
	-- -----------
	INSERT INTO games (name, description, is_active, created_at) VALUES
	('Super Smash Bros Melee',	NULL,						TRUE,	CURRENT_TIMESTAMP - INTERVAL '100 days')
	RETURNING id INTO smbm_id;
	
	INSERT INTO games (name, description, is_active, created_at) VALUES
	('Team Fortress 2 MGE',		'1v1 Hatconomy first person shooter',		TRUE,	CURRENT_TIMESTAMP - INTERVAL '50 days')
	RETURNING id INTO tf2_id;
	
	INSERT INTO games (name, description, is_active, created_at) VALUES
	('Fortnite 1v1',		'Battle royal FPS',				FALSE,	CURRENT_TIMESTAMP - INTERVAL '10 days')
	RETURNING id INTO fortnite_id;

	-- ----------
	-- 👥 TEAM
	-- ----------
	-- Team: The Legion (captain: ivan_m)
	INSERT INTO teams (name, game_id, created_at)
	VALUES ('The Legion',
		smbm_id,
		CURRENT_TIMESTAMP - INTERVAL '10 days')
	RETURNING id INTO legion_team_id;

	INSERT INTO team_members (team_id, user_id, role, joined_at) VALUES
		(legion_team_id, ivan_m_id, 'CAPTAIN', CURRENT_TIMESTAMP - INTERVAL '10 days'),
		(legion_team_id, nikolina_id, 'REGULAR', CURRENT_TIMESTAMP - INTERVAL '8 days'),
		(legion_team_id, mvidovic_id, 'REGULAR', CURRENT_TIMESTAMP - INTERVAL '8 days'),
		(legion_team_id, domagoj_id, 'REGULAR', CURRENT_TIMESTAMP - INTERVAL '3 days');
		
		
END $$;

