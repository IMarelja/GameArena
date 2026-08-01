CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');

CREATE TABLE users (
	id			BIGSERIAL 	PRIMARY KEY,
	username		VARCHAR(50)  	NOT NULL UNIQUE,
	email			VARCHAR(255) 	NOT NULL UNIQUE,
	password_hash		VARCHAR(255) 	NOT NULL,
	password_salt		VARCHAR(255) 	NOT NULL,
	role			user_role	NOT NULL DEFAULT 'USER',
	is_active		BOOLEAN		NOT NULL DEFAULT TRUE,
	created_at		TIMESTAMP	NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE login_history (
	id		BIGSERIAL	PRIMARY KEY,
	login_at	TIMESTAMP	NOT NULL,
	ip_address_ipv4 VARCHAR(15),
	ip_address_ipv6 VARCHAR(45),
	success         BOOLEAN,
	user_id         BIGINT		REFERENCES users(id) ON DELETE SET NULL,
	email           VARCHAR(255)	NOT NULL,
	event_type      VARCHAR(20)	-- 'BAD_PASSWORD', 'USER_NOT_FOUND', 'ACCOUNT_LOCKED'
);

-- ---------------------------------------------------------------------
-- GAMES
-- ---------------------------------------------------------------------
 
CREATE TABLE games (
	id		BIGSERIAL	PRIMARY KEY,
	name		VARCHAR(100)	NOT NULL UNIQUE,
	description	TEXT,
	is_active	BOOLEAN		NOT NULL DEFAULT TRUE,
	created_at	TIMESTAMP	NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- TEAMS
-- ---------------------------------------------------------------------
 
CREATE TABLE teams (
	id		BIGSERIAL	PRIMARY KEY,
	name		VARCHAR(100)	NOT NULL,
	game_id		BIGINT		NOT NULL REFERENCES games(id),
	captain_id	BIGINT		NOT NULL REFERENCES users(id),
	-- logo_url	VARCHAR(255),
	created_at	TIMESTAMP	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	UNIQUE (name, game_id)
);

CREATE TABLE team_members (
	team_member_id	BIGSERIAL	PRIMARY KEY,
	team_id		BIGINT		NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
	user_id		BIGINT		NOT NULL REFERENCES users(id),
	joined_at	TIMESTAMP	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	UNIQUE (team_id, user_id)
);
 
-- A player creates a team and invites other players
CREATE TABLE team_invitations (
	invitation_id	BIGSERIAL	PRIMARY KEY,
	team_id		BIGINT		NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
	inviter_id	BIGINT		NOT NULL REFERENCES users(id),
	invitee_id	BIGINT		NOT NULL REFERENCES users(id),
	status		VARCHAR(20)	NOT NULL DEFAULT 'pending'
			CHECK (status IN ('pending', 'accepted', 'declined', 'cancelled')),
	created_at	TIMESTAMP	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	responded_at	TIMESTAMP
);

-- ---------------------------------------------------------------------
-- Tournament
-- ---------------------------------------------------------------------

