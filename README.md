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

## Thinks to note

### Regenerate OpenAPI.json schema

The default should be fine, but if you change anything about an endpoint, delete one or add a new one you are forced to regenerate the schema so webapp can work properly 

- When you start up your API project go to /api project and run this
```bash
mvn springdoc-openapi:generate
```

