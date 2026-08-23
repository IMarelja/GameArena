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
	ballsmash_tournament_id		BIGINT;

	-- Dates
	specific_date_time	TIMESTAMPTZ := TIMESTAMPTZ '2026-08-01 14:00:00+00';
	now_utc			TIMESTAMPTZ := CURRENT_TIMESTAMP;
	tournament_lime_date	TIMESTAMPTZ := TIMESTAMPTZ '2026-08-11 12:00:00+00';

	-- Scratch vars, reused per Lime tournament participant
	new_billing_info_id	BIGINT;
	new_payment_id		BIGINT;

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
	-- 🏆 TOURNAMENT (LIME)
	-- -----------------
	-- Tournament: Lime tournament 2026 (organizer: admin)
	INSERT INTO tournaments (name, description, game_id, status, price_solo, price_group, currency, starts_at, ends_at, created_at)
	VALUES ('Lime tournament 2026',
		'Yearly TF2 MGE tournament leaderboard',
		tf2_id,
		'ENDED',
		10.00,
		8.00,
		'EUR',
		tournament_lime_date,
		tournament_lime_date + INTERVAL '6 hours',
		tournament_lime_date - INTERVAL '30 days')
	RETURNING id INTO lime_tournament_id;

	INSERT INTO tournament_member (tournament_id, user_id, role, joined_at, confirmed)
	VALUES (lime_tournament_id, admin_id, 'ORGANIZER', tournament_lime_date - INTERVAL '30 days', TRUE);

	-- 📝🤚 Lime tournament participants (each: billing info, payment, PayPal payment, invoice and tournament member)

	-- Participant: ivan_m
	INSERT INTO billing_info (full_name, email, address_line, city, state, zip_code, country, created_at)
	VALUES ('Ivan Marelja', 'ivan.marelja@hello.hr', 'Ilica 10', 'Zagreb', NULL, '10000', 'HRV', tournament_lime_date - INTERVAL '15 days')
	RETURNING id INTO new_billing_info_id;

	INSERT INTO payments (status, amount, currency, created_at)
	VALUES ('PAID', 10.00, 'EUR', tournament_lime_date - INTERVAL '15 days')
	RETURNING id INTO new_payment_id;

	INSERT INTO paypal_payments (payment_id, paypal_order_id, paypal_payer_id, capture_id, status, created_at, updated_at)
	VALUES (new_payment_id, 'PAYPAL-ORDER-IVANM-0001', 'PAYPAL-PAYER-IVANM-0001', 'PAYPAL-CAPTURE-IVANM-0001', 'COMPLETED', tournament_lime_date - INTERVAL '15 days', tournament_lime_date - INTERVAL '15 days');

	INSERT INTO invoices (user_id, billing_info_id, payment_id, created_at)
	VALUES (ivan_m_id, new_billing_info_id, new_payment_id, tournament_lime_date - INTERVAL '15 days');

	INSERT INTO tournament_member (tournament_id, user_id, role, joined_at, payer_id, group_id, payment_id, confirmed)
	VALUES (lime_tournament_id, ivan_m_id, 'PARTICIPANTS', tournament_lime_date - INTERVAL '15 days', NULL, NULL, new_payment_id, TRUE);

	-- Participant: super_gamer
	INSERT INTO billing_info (full_name, email, address_line, city, state, zip_code, country, created_at)
	VALUES ('Tom Fynder', 'tom.fynder@gmail.com', 'Sunset Blvd 22', 'Los Angeles', 'CA', '90001', 'USA', tournament_lime_date - INTERVAL '12 days')
	RETURNING id INTO new_billing_info_id;

	INSERT INTO payments (status, amount, currency, created_at)
	VALUES ('PAID', 10.00, 'EUR', tournament_lime_date - INTERVAL '12 days')
	RETURNING id INTO new_payment_id;

	INSERT INTO paypal_payments (payment_id, paypal_order_id, paypal_payer_id, capture_id, status, created_at, updated_at)
	VALUES (new_payment_id, 'PAYPAL-ORDER-SUPERGAMER-0002', 'PAYPAL-PAYER-SUPERGAMER-0002', 'PAYPAL-CAPTURE-SUPERGAMER-0002', 'COMPLETED', tournament_lime_date - INTERVAL '12 days', tournament_lime_date - INTERVAL '12 days');

	INSERT INTO invoices (user_id, billing_info_id, payment_id, created_at)
	VALUES (super_gamer_id, new_billing_info_id, new_payment_id, tournament_lime_date - INTERVAL '12 days');

	INSERT INTO tournament_member (tournament_id, user_id, role, joined_at, payer_id, group_id, payment_id, confirmed)
	VALUES (lime_tournament_id, super_gamer_id, 'PARTICIPANTS', tournament_lime_date - INTERVAL '12 days', NULL, NULL, new_payment_id, TRUE);

	-- Participant: hampterboy7
	INSERT INTO billing_info (full_name, email, address_line, city, state, zip_code, country, created_at)
	VALUES ('Hampter Boy', 'keepitreal@mail.com', 'Alexanderplatz 3', 'Berlin', NULL, '10178', 'DEU', tournament_lime_date - INTERVAL '10 days')
	RETURNING id INTO new_billing_info_id;

	INSERT INTO payments (status, amount, currency, created_at)
	VALUES ('PAID', 10.00, 'EUR', tournament_lime_date - INTERVAL '10 days')
	RETURNING id INTO new_payment_id;

	INSERT INTO paypal_payments (payment_id, paypal_order_id, paypal_payer_id, capture_id, status, created_at, updated_at)
	VALUES (new_payment_id, 'PAYPAL-ORDER-HAMPTERBOY7-0003', 'PAYPAL-PAYER-HAMPTERBOY7-0003', 'PAYPAL-CAPTURE-HAMPTERBOY7-0003', 'COMPLETED', tournament_lime_date - INTERVAL '10 days', tournament_lime_date - INTERVAL '10 days');

	INSERT INTO invoices (user_id, billing_info_id, payment_id, created_at)
	VALUES (hampterboy7_id, new_billing_info_id, new_payment_id, tournament_lime_date - INTERVAL '10 days');

	INSERT INTO tournament_member (tournament_id, user_id, role, joined_at, payer_id, group_id, payment_id, confirmed)
	VALUES (lime_tournament_id, hampterboy7_id, 'PARTICIPANTS', tournament_lime_date - INTERVAL '10 days', NULL, NULL, new_payment_id, TRUE);

	-- Participant: david2014
	INSERT INTO billing_info (full_name, email, address_line, city, state, zip_code, country, created_at)
	VALUES ('David Tettersen', 'david.tettersen@icloud.com', 'Bakkegata 5', 'Oslo', NULL, '0150', 'NOR', tournament_lime_date - INTERVAL '8 days')
	RETURNING id INTO new_billing_info_id;

	INSERT INTO payments (status, amount, currency, created_at)
	VALUES ('PAID', 10.00, 'EUR', tournament_lime_date - INTERVAL '8 days')
	RETURNING id INTO new_payment_id;

	INSERT INTO paypal_payments (payment_id, paypal_order_id, paypal_payer_id, capture_id, status, created_at, updated_at)
	VALUES (new_payment_id, 'PAYPAL-ORDER-DAVID2014-0004', 'PAYPAL-PAYER-DAVID2014-0004', 'PAYPAL-CAPTURE-DAVID2014-0004', 'COMPLETED', tournament_lime_date - INTERVAL '8 days', tournament_lime_date - INTERVAL '8 days');

	INSERT INTO invoices (user_id, billing_info_id, payment_id, created_at)
	VALUES (david2014_id, new_billing_info_id, new_payment_id, tournament_lime_date - INTERVAL '8 days');

	INSERT INTO tournament_member (tournament_id, user_id, role, joined_at, payer_id, group_id, payment_id, confirmed)
	VALUES (lime_tournament_id, david2014_id, 'PARTICIPANTS', tournament_lime_date - INTERVAL '8 days', NULL, NULL, new_payment_id, TRUE);

	-- > 👊 Matches 👊 < --
	
	-- ivan_m VS david2014
	INSERT INTO matches (
		tournament_id, 
		game_id, 
		player_one_id,
		player_two_id,
		player_one_score,
		player_two_score,
		winner_id,
		status,
		scheduled_at,
		played_at,
		created_at)
	VALUES(
		lime_tournament_id,
		tf2_id,
		david2014_id,
		ivan_m_id,
		4,
		7,
		ivan_m_id,
		'COMPLETED',
		tournament_lime_date + INTERVAL '1 hour',
		tournament_lime_date + INTERVAL '1 hour',
		tournament_lime_date - INTERVAL '5 days'
	);
	
	-- hampterboy7 VS super_gamer
	INSERT INTO matches (
		tournament_id, 
		game_id, 
		player_one_id,
		player_two_id,
		player_one_score,
		player_two_score,
		winner_id,
		status,
		scheduled_at,
		played_at,
		created_at)
	VALUES(
		lime_tournament_id,
		tf2_id,
		hampterboy7_id,
		super_gamer_id,
		8,
		6,
		hampterboy7_id,
		'COMPLETED',
		tournament_lime_date + INTERVAL '1 hour + 30 minutes',
		tournament_lime_date + INTERVAL '1 hour + 40 minutes',
		tournament_lime_date - INTERVAL '5 days'
	);
	
	-- ivan_m VS hampterboy7
	INSERT INTO matches (
		tournament_id, 
		game_id, 
		player_one_id,
		player_two_id,
		player_one_score,
		player_two_score,
		winner_id,
		status,
		scheduled_at,
		played_at,
		created_at)
	VALUES(
		lime_tournament_id,
		tf2_id,
		ivan_m_id,
		hampterboy7_id,
		10,
		3,
		ivan_m_id,
		'COMPLETED',
		tournament_lime_date + INTERVAL '3 hour',
		tournament_lime_date + INTERVAL '3 hour',
		tournament_lime_date - INTERVAL '5 days'
	);
	
	-- super_gamer VS david2014
	INSERT INTO matches (
		tournament_id, 
		game_id, 
		player_one_id,
		player_two_id,
		player_one_score,
		player_two_score,
		winner_id,
		status,
		scheduled_at,
		played_at,
		created_at)
	VALUES(
		lime_tournament_id,
		tf2_id,
		david2014_id,
		super_gamer_id,
		10,
		11,
		super_gamer_id,
		'COMPLETED',
		tournament_lime_date + INTERVAL '3 hour + 30 minutes',
		tournament_lime_date + INTERVAL '3 hour + 30 minutes',
		tournament_lime_date - INTERVAL '5 days'
	);
	
	-- super_gamer VS ivan_m
	INSERT INTO matches (
		tournament_id, 
		game_id, 
		player_one_id,
		player_two_id,
		player_one_score,
		player_two_score,
		winner_id,
		status,
		scheduled_at,
		played_at,
		created_at)
	VALUES(
		lime_tournament_id,
		tf2_id,
		ivan_m_id,
		super_gamer_id,
		15,
		15,
		NULL,
		'COMPLETED',
		tournament_lime_date + INTERVAL '5 hour',
		tournament_lime_date + INTERVAL '5 hour',
		tournament_lime_date - INTERVAL '5 days'
	);
	
	-- david2014 VS hampterboy7
	INSERT INTO matches (
		tournament_id, 
		game_id, 
		player_one_id,
		player_two_id,
		player_one_score,
		player_two_score,
		winner_id,
		status,
		scheduled_at,
		played_at,
		created_at)
	VALUES(
		lime_tournament_id,
		tf2_id,
		david2014_id,
		hampterboy7_id,
		8,
		10,
		hampterboy7_id,
		'COMPLETED',
		tournament_lime_date + INTERVAL '5 hour + 30 minutes',
		tournament_lime_date + INTERVAL '5 hour + 35 minutes',
		tournament_lime_date - INTERVAL '5 days'
	);
	
	-- -----------------
	-- 🏆 TOURNAMENT (BallSmash)
	-- -----------------
	
	-- Tournament: BallSmash tournament Summer (organizer: admin, super_gamer)
	INSERT INTO tournaments (name, description, game_id, status, price_solo, price_group, currency, starts_at, ends_at, created_at)
	VALUES ('BallSmash tournament Summer',
		'Summer Seasonal Super Smash Bros Melee mini tournament',
		smbm_id,
		'SCHEDULED',
		10.00,
		8.00,
		'EUR',
		now_utc + INTERVAL '2 days',
		now_utc + INTERVAL '2 days 10 hours',
		now_utc - INTERVAL '30 days')
	RETURNING id INTO ballsmash_tournament_id;
	
	INSERT INTO tournament_member (tournament_id, user_id, role, joined_at, confirmed)
	VALUES (ballsmash_tournament_id, admin_id, 'ORGANIZER', now_utc - INTERVAL '30 days', TRUE);
	INSERT INTO tournament_member (tournament_id, user_id, role, joined_at, confirmed)
	VALUES (ballsmash_tournament_id, super_gamer_id, 'ORGANIZER', now_utc - INTERVAL '29 days', TRUE);

	-- 📝🤚 BallSmash tournament participants (each: billing info, payment, PayPal payment, invoice and tournament member)

	-- Participant: kissermaxxer
	INSERT INTO billing_info (full_name, email, address_line, city, state, zip_code, country, created_at)
	VALUES ('Kisser Maxxer', 'jugio.killer@gmail.com', 'Karlova 15', 'Ljubljana', NULL, '1000', 'SVN', now_utc - INTERVAL '5 days')
	RETURNING id INTO new_billing_info_id;

	INSERT INTO payments (status, amount, currency, created_at)
	VALUES ('PAID', 10.00, 'EUR', now_utc - INTERVAL '5 days')
	RETURNING id INTO new_payment_id;

	INSERT INTO paypal_payments (payment_id, paypal_order_id, paypal_payer_id, capture_id, status, created_at, updated_at)
	VALUES (new_payment_id, 'PAYPAL-ORDER-KISSERMAXXER-0005', 'PAYPAL-PAYER-KISSERMAXXER-0005', 'PAYPAL-CAPTURE-KISSERMAXXER-0005', 'COMPLETED', now_utc - INTERVAL '5 days', now_utc - INTERVAL '5 days');

	INSERT INTO invoices (user_id, billing_info_id, payment_id, created_at)
	VALUES (kissermaxxer_id, new_billing_info_id, new_payment_id, now_utc - INTERVAL '5 days');

	INSERT INTO tournament_member (tournament_id, user_id, role, joined_at, payer_id, group_id, payment_id, confirmed)
	VALUES (ballsmash_tournament_id, kissermaxxer_id, 'PARTICIPANTS', now_utc - INTERVAL '5 days', NULL, NULL, new_payment_id, TRUE);

	-- Participant: basilplayer
	INSERT INTO billing_info (full_name, email, address_line, city, state, zip_code, country, created_at)
	VALUES ('Marte Tranic', 'marte_tranic@gmail.com', 'Via Roma 12', 'Milano', NULL, '20121', 'ITA', now_utc - INTERVAL '3 days')
	RETURNING id INTO new_billing_info_id;

	INSERT INTO payments (status, amount, currency, created_at)
	VALUES ('PAID', 10.00, 'EUR', now_utc - INTERVAL '3 days')
	RETURNING id INTO new_payment_id;

	INSERT INTO paypal_payments (payment_id, paypal_order_id, paypal_payer_id, capture_id, status, created_at, updated_at)
	VALUES (new_payment_id, 'PAYPAL-ORDER-BASILPLAYER-0006', 'PAYPAL-PAYER-BASILPLAYER-0006', 'PAYPAL-CAPTURE-BASILPLAYER-0006', 'COMPLETED', now_utc - INTERVAL '3 days', now_utc - INTERVAL '3 days');

	INSERT INTO invoices (user_id, billing_info_id, payment_id, created_at)
	VALUES (basilplayer_id, new_billing_info_id, new_payment_id, now_utc - INTERVAL '3 days');

	INSERT INTO tournament_member (tournament_id, user_id, role, joined_at, payer_id, group_id, payment_id, confirmed)
	VALUES (ballsmash_tournament_id, basilplayer_id, 'PARTICIPANTS', now_utc - INTERVAL '3 days', NULL, NULL, new_payment_id, TRUE);
	
	-- > 👊 Matches 👊 < --

	-- kissermaxxer VS basilplayer (scheduled, not yet played)
	-- INSERT INTO matches(tournament_id,game_id,player_one_id,player_two_id,player_one_score,player_two_score,winner_id,status,scheduled_at,played_at,created_at)
	--VALUES(
	--	ballsmash_tournament_id,
	--	smbm_id,
	--	kissermaxxer_id,
	--	basilplayer_id,
	--	NULL,
	--	NULL,
	--	NULL,
	--	'SCHEDULED',
	--	now_utc + INTERVAL '2 days 1 hours',
	--	NULL,
	--	now_utc - INTERVAL '1 days'
	--);*/

END $$;

