--
-- DATABASE DEFAULTS
--

ALTER DATABASE gamearenadb SET timezone TO 'UTC';
SET TIME ZONE 'UTC';

--
-- USERS
--

CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');

CREATE TABLE users (
	id			BIGINT 		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	username		VARCHAR(50)  	NOT NULL UNIQUE,
	email			VARCHAR(255) 	NOT NULL UNIQUE,
	password_hash		VARCHAR(255) 	NOT NULL,
	password_salt		VARCHAR(255) 	NOT NULL,
	role			user_role	NOT NULL DEFAULT 'USER',
	is_active		BOOLEAN		NOT NULL DEFAULT TRUE,
	created_at		TIMESTAMPTZ	NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON users (username);

CREATE INDEX idx_users_email ON users (email);

-- ---------------------------------------------------------------------
-- GAMES
-- ---------------------------------------------------------------------
 
CREATE TABLE games (
	id		BIGINT 		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name		VARCHAR(100)	NOT NULL UNIQUE,
	description	TEXT,
	is_active	BOOLEAN		NOT NULL DEFAULT TRUE,
	created_at	TIMESTAMPTZ	NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- TEAMS
-- ---------------------------------------------------------------------
 
CREATE TABLE teams (
	id		BIGINT 		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name		VARCHAR(100)	NOT NULL,
	game_id		BIGINT		NOT NULL REFERENCES games(id),
	created_at	TIMESTAMPTZ	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	UNIQUE (name, game_id)
);

CREATE TABLE team_members (
	team_member_id	BIGINT 		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	team_id		BIGINT		NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
	user_id		BIGINT		NOT NULL REFERENCES users(id),
	role		VARCHAR(20)	NOT NULL,
	joined_at	TIMESTAMPTZ	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	UNIQUE (team_id, user_id)
);

CREATE INDEX idx_team_members_user_id ON team_members (user_id);
 
CREATE TYPE invite_status AS ENUM(
	'PENDING',
	'ACCEPTED',
	'DECLINED',
	'CANCELLED'
);
 
-- A player creates a team and invites other players
CREATE TABLE team_invitations (
	invitation_id	BIGINT 		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	team_id		BIGINT		NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
	inviter_id	BIGINT		NOT NULL REFERENCES users(id),
	invitee_id	BIGINT		NOT NULL REFERENCES users(id),
	status		invite_status	NOT NULL DEFAULT 'PENDING',
	created_at	TIMESTAMPTZ	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	responded_at	TIMESTAMPTZ,
	CONSTRAINT chk_distict_inviter_invitee CHECK (
		inviter_id <> invitee_id
	)
);

-- ---------------------------------------------------------------------
-- LOGIN LOGGING
-- ---------------------------------------------------------------------

CREATE TYPE login_log_type AS ENUM (
	'SUCCESS',
	'BAD_PASSWORD',
	'BAD_CREDENTIALS',
	'DISABLED_ACCOUNT',
	'UNEXPECTED_FAILURE'
);

CREATE TABLE login_logs (
	id		BIGINT 		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	credential	VARCHAR(255) 	NOT NULL,
	ipv4 		VARCHAR(15),
	ipv6 		VARCHAR(45),
	type 		login_log_type 	NOT NULL,
	created_at 	TIMESTAMPTZ 	NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_login_logs_credentials ON login_logs (credential);

CREATE INDEX idx_login_logs_type ON login_logs (type);

-- ---------------------------------------------------------------------
-- TOURNAMENT
-- ---------------------------------------------------------------------

--CREATE TYPE tournament_status AS ENUM (
--	'SCHEDULED',
--	'LIVE',
--	'ENDED',
--	'CANCELED'
--);

CREATE TABLE tournaments (
	id 		BIGINT 			GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name 		VARCHAR(255) 		NOT NULL,
	description 	TEXT,
	game_id 	BIGINT 			NOT NULL REFERENCES games(id),
	status 		VARCHAR(20)		NOT NULL, --tournament_status 	NOT NULL,
	starts_at 	TIMESTAMPTZ 		NOT NULL,
	ends_at 	TIMESTAMPTZ,
	created_at 	TIMESTAMPTZ 		NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tournament_member (
	id		BIGINT		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	tournament_id	BIGINT		NOT NULL REFERENCES tournaments(id),
	user_id		BIGINT		NOT NULL REFERENCES users(id),
	role		VARCHAR(20)	NOT NULL,
	joined_at	TIMESTAMPTZ	NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- MATCH
-- ---------------------------------------------------------------------

CREATE TYPE match_status AS ENUM (
	'SCHEDULED',
	'IN_PROGRESS',
	'COMPLETED',
	'CANCELED'
);

CREATE TABLE matches (
	id 			BIGINT		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    	tournament_id		BIGINT		REFERENCES tournaments(id),
	game_id			BIGINT		NOT NULL REFERENCES games(id),
	player_one_id		BIGINT		NOT NULL REFERENCES users(id),
	player_two_id		BIGINT		NOT NULL REFERENCES users(id),
	player_one_score	INTEGER,
	player_two_score	INTEGER,
	winner_id		BIGINT		REFERENCES users(id),
	status			match_status	NOT NULL,
	scheduled_at		TIMESTAMPTZ,
	played_at		TIMESTAMPTZ,
	created_at		TIMESTAMPTZ	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT chk_matches_distinct_players CHECK (
		player_one_id <> player_two_id
	),
	CONSTRAINT chk_matches_winner_is_player CHECK (
		winner_id IS NULL OR winner_id IN (player_one_id, player_two_id)
	)
);

-- ---------------------------------------------------------------------
-- NOTIFICATION
-- ---------------------------------------------------------------------

CREATE TABLE notifications (
	id 			BIGINT		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	read_at			BOOL		NOT NULL,
	type			VARCHAR(50)	NOT NULL,
	recipient_user_id	BIGINT		NOT NULL REFERENCES users(id),
	reference_id		BIGINT,
	reference_type		VARCHAR(50),
	created_at		TIMESTAMPTZ	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT chk_null_or_nothing_reference CHECK (
		(reference_id IS NULL) = (reference_type IS NULL)
	)
);

CREATE INDEX idx_notifications_recipient_user_id ON notifications (recipient_user_id);

-- ---------------------------------------------------------------------
-- PAYMENT
-- ---------------------------------------------------------------------


CREATE TYPE payment_status AS ENUM (
	'PENDING',
	'PAID',
	'FAILED',
	'REFUNDED',
	'CANCELLED'
);

CREATE TABLE payments (
	id 			BIGINT 		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	type			payment_type	NOT NULL,
	status			payment_status	NOT NULL DEFAULT 'PENDING',
	amount 			NUMERIC(12, 2) 	NOT NULL CHECK (amount > 0),
	currency 		CHAR(3) 	NOT NULL,
	created_at		TIMESTAMPTZ	NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE paypal_payments (
	id 			BIGINT 		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	payment_id 		BIGINT 		NOT NULL UNIQUE REFERENCES payments (id),
	paypal_order_id 	VARCHAR(64) 	NOT NULL UNIQUE,
	paypal_payer_id 	VARCHAR(64),
	capture_id 		VARCHAR(64),
	status 			VARCHAR(20) 	NOT NULL,
	created_at 		TIMESTAMPTZ 	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at 		TIMESTAMPTZ
);

CREATE INDEX idx_paypal_payments_capture_id ON paypal_payments (capture_id);
CREATE INDEX idx_paypal_payments_paypal_payer_id ON paypal_payments (paypal_payer_id);

-- --------------------------------------------------------------------- 
-- BILLING INFORMATION
-- ---------------------------------------------------------------------

CREATE TABLE billing_info (
	id 			BIGINT 		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	invoice_id 		BIGINT 		NOT NULL UNIQUE REFERENCES invoices (id),
	full_name 		VARCHAR(150) 	NOT NULL,
	email 			VARCHAR(255) 	NOT NULL,
	address_line 		VARCHAR(255) 	NOT NULL,
	city 			VARCHAR(100) 	NOT NULL,
	state			VARCHAR(100),
	zip_code		VARCHAR(20)	NOT NULL,
	country 		CHAR(3) 	NOT NULL,
	created_at 		TIMESTAMPTZ 	NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- INVOICE
-- ---------------------------------------------------------------------

CREATE TABLE invoices (
	id 			BIGINT 		GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	user_id			BIGINT		NOT NULL REFERENCES users (id),
	billing_info_id		BIGINT		NOT NULL REFERENCES billing_info (id),
	--tournament_id 	BIGINT 		NOT NULL REFERENCES tournaments (id),
    	created_at 		TIMESTAMPTZ 	NOT NULL DEFAULT CURRENT_TIMESTAMP,
);

CREATE INDEX idx_invoices_user_id ON invoices (user_id);
CREATE INDEX idx_invoices_billing_info_id ON invoices (billing_info_id);
--CREATE INDEX idx_invoices_tournament_id ON invoices (tournament_id);
