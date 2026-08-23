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
	hampterboy7_id	BIGINT;
	david2014_id	BIGINT;
	ultra_marine_id	BIGINT;
	kissermaxxer_id	BIGINT;
	girl_cooler_id	BIGINT;
	basilplayer_id	BIGINT;
	lubantrg_id	BIGINT;

	-- Games
	smbm_id		BIGINT;
	tf2_id		BIGINT;
	fortnite_id	BIGINT;

	-- Team
	legion_team_id	BIGINT;

	-- Tournament
	lime_tournament_id		BIGINT;
	ballbrothers_tournament_id	BIGINT;

	-- Dates
	specific_date_time	TIMESTAMPTZ := TIMESTAMPTZ '2026-08-01 14:00:00+00';
	now_utc			TIMESTAMPTZ := CURRENT_TIMESTAMP;

	--
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
		specific_date_time - INTERVAL '110 days')
	RETURNING id INTO admin_id;

	-- First regular user account
	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('ivan_m',
		'ivan.marelja@hello.hr',
		'0b0ae26ba604ed3121dbe75202de14efc1a586a9eec82525e815a7266c224312', -- helloworld
		'1a51f517df9e1e639559e29d63ab8346',
		'USER',
		TRUE,
		specific_date_time - INTERVAL '100 days')
	RETURNING id INTO ivan_m_id;

	-- Second regular user account
	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('super_gamer',
		'tom.fynder@gmail.com',
		'68e26bfd9a7e9bb56db9275ad85d51ad8bc643a95544494b435d6c4d26edaaaf', -- 1passworld2
		'0de7d7410e404cf59fa375defec8070b',
		'USER',
		TRUE,
		specific_date_time - INTERVAL '90 days')
	RETURNING id INTO super_gamer_id;

	-- Suspended account
	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('evil_gamer',
		'burner.email@crocofile.com',
		'baea65b797361c8665d940c9ce45146c4f10e2e49ec7bec2055f5b3ab2778523', -- evilhello
		'dad660e9c4f09da2d877495c2de0c200',
		'USER',
		FALSE,
		specific_date_time - INTERVAL '80 days')
	RETURNING id INTO evil_gamer_id;

	-- Seed dummy data
	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('hampterboy7', 'keepitreal@mail.com', 'seed$hash$0001', 'seed$salt$0001', 'USER', TRUE, specific_date_time - INTERVAL '70 days')
	RETURNING id INTO hampterboy7_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('david2014', 'david.tettersen@icloud.com', 'seVLUESed$hash$0002', 'seed$salt$0002', 'USER', TRUE, specific_date_time - INTERVAL '60 days')
	RETURNING id INTO david2014_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('UltraMarine', 'alient.death@hotmail.com', 'seed$hash$0003', 'seed$salt$0003', 'USER', TRUE, specific_date_time - INTERVAL '50 days')
	RETURNING id INTO ultra_marine_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('kissermaxxer', 'jugio.killer@gmail.com', 'seed$hash$0004', 'seed$salt$0004', 'USER', TRUE, specific_date_time - INTERVAL '40 days')
	RETURNING id INTO kissermaxxer_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('girl_cooler', 'trans.supporter889@trans_move.com', 'seed$hash$0005', 'seed$salt$0005', 'USER', FALSE, specific_date_time - INTERVAL '30 days')
	RETURNING id INTO girl_cooler_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('BasilPrayer', 'marte_tranic@gmail.com', 'seed$hash$0006', 'seed$salt$0006', 'USER', TRUE, specific_date_time - INTERVAL '20 days')
	RETURNING id INTO basilplayer_id;

	INSERT INTO users (username, email, password_hash, password_salt, role, is_active, created_at)
	VALUES ('lubantrg54', '798hgha541jg678af@crocmail.com', 'seed$hash$0007', 'seed$salt$0007', 'USER', TRUE, specific_date_time - INTERVAL '10 days')
	RETURNING id INTO lubantrg_id;

	-- -----------
	-- 🎮 GAMES
	-- -----------
	INSERT INTO games (name, description, is_active, created_at) VALUES
	('Super Smash Bros Melee',	NULL,						TRUE,	specific_date_time - INTERVAL '100 days')
	RETURNING id INTO smbm_id;
	
	INSERT INTO games (name, description, is_active, created_at) VALUES
	('Team Fortress 2 MGE',		'1v1 Hatconomy first person shooter',		TRUE,	specific_date_time - INTERVAL '50 days')
	RETURNING id INTO tf2_id;
	
	INSERT INTO games (name, description, is_active, created_at) VALUES
	('Fortnite 1v1',		'Battle royal FPS',				FALSE,	specific_date_time - INTERVAL '10 days')
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
		(legion_team_id, ivan_m_id, 'CAPTAIN', specific_date_time - INTERVAL '10 days'),
		(legion_team_id, hampterboy7_id, 'REGULAR', specific_date_time - INTERVAL '8 days'),
		(legion_team_id, girl_cooler_id, 'REGULAR', specific_date_time - INTERVAL '8 days'),
		(legion_team_id, ultra_marine_id, 'REGULAR', specific_date_time - INTERVAL '3 days');

	-- -----------------
	-- 🏆 TOURNAMENT
	-- -----------------
	-- Tournament: Lime tournament 2026 (organizer: admin)
	INSERT INTO tournaments (name, description, game_id, status, price_solo, price_group, currency, starts_at, ends_at)
	VALUES ('Lime tournament 2026',
		'Yearly TF2 MGE tournament leaderboard',
		tf2_id,
		'ENDED',
		10.00,
		8.00,
		'EUR',
		TIMESTAMPTZ '2026-08-11 12:00:00+00',
		TIMESTAMPTZ '2026-08-15 18:00:00+00')
	RETURNING id INTO lime_tournament_id;

	INSERT INTO tournament_member (tournament_id, user_id, role, joined_at, confirmed)
	VALUES (lime_tournament_id, admin_id, 'ORGANIZER', CURRENT_TIMESTAMP, TRUE);

END $$;

