# Topic 8: GameArena – gaming tournament organization
A platform for registering for esports tournaments and tracking results and leaderboards.
## PLAYER
- An anonymous user can browse tournaments, leaderboards, and results by game (anonymous)
- A player registers for tournaments individually or creates a team and invites other players (authenticated - player)
- A player views their statistics (matches played, wins, ranking points) and tournament history (authenticated - player)
- A player receives asynchronous notifications regarding their match schedule (authenticated - player)
- A player must pay for participation via PayPal (authenticated - player)
## ADMINISTRATOR
- An administrator manages games, users, and organizers, and approves public tournaments (authenticated - admin)
- An administrator views platform activity reports with filters by game and time period (authenticated - admin)
- An administrator views a historical log of system logins (user, time, IP address) (authenticated - admin)

# Setup

Create your `.env` file from the example and pick one of the ways to run the project

```bash
cp .env.example .env
```

## Standard way

Runs the database, API and webapp in a standard way.

Make sure `API_URL` in `.env` points to the API container:

```env
API_URL=http://api:5762
```

Build and run the project

```bash
docker compose -f docker-compose.yaml up -d --build
```

- Webapp: http://localhost:5763
- API: http://localhost:5762
- Database: localhost:5432

Rerun the project

```bash
docker compose -f docker-compose.yaml up -d
```

Turn down the project

```bash
docker compose -f docker-compose.yaml down 
```

Full remove everything (including the database volume)

```bash
docker compose -f docker-compose.yaml down -v
```

## Ngrok way

Exposes the API and webapp publicly through ngrok tunnels.

Fill in all the empty references in `.env`: the ngrok auth tokens and the assigned URLs (`NGROK_URL_API`, `NGROK_AUTHTOKEN_API`, `NGROK_URL_WEBAPP`, `NGROK_AUTHTOKEN_WEBAPP`), and set `API_URL` to the same value as `NGROK_URL_API`.

Build and run the project

```bash
docker compose -f docker-compose.ngrok.yml up -d --build
```

Rerun the project

```bash
docker compose -f docker-compose.ngrok.yml up -d
```

Turn down the project

```bash
docker compose -f docker-compose.ngrok.yml down
```

Full remove everything

```bash
docker compose -f docker-compose.ngrok.yml down -v
```

## SonarQube

Follow those [instructions](SonarQube/README.md)

## Thinks to note

### Regenerate OpenAPI.json schema

The default should be fine, but if you change anything about an endpoint, delete one or add a new one you are forced to regenerate the schema so webapp can work properly 

- When you start up your API project go to /api project and run this
```bash
mvn springdoc-openapi:generate
```

