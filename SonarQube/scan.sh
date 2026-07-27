#!/usr/bin/env bash
set -euo pipefail

SONAR_URL="http://localhost:9000"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"

# Load .env from the script's own directory
ENV_FILE="$SCRIPT_DIR/.env"
if [ -f "$ENV_FILE" ]; then
  set -a
  source "$ENV_FILE"
  set +a
fi

if [ -z "${SONAR_TOKEN:-}" ]; then
  echo "Error: SONAR_TOKEN is not set."
  echo "  1. Open $SONAR_URL and log in (admin / admin on first run)"
  echo "  2. Go to My Account > Security > Generate Token"
  echo "  3. Run:  export SONAR_TOKEN=<your-token>"
  exit 1
fi

echo "==> Scanning api..."
cd "$ROOT_DIR/api"
#mvn --batch-mode verify sonar:sonar -DskipTests \
#  -Dsonar.host.url="$SONAR_URL" \
#  -Dsonar.token="$SONAR_TOKEN" \
#  -Dsonar.projectKey=GameArenaApi \
#  -Dsonar.projectName="GameArena API"
mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
  -Dsonar.host.url="$SONAR_URL" \
  -Dsonar.token="$SONAR_TOKEN"

echo ""
echo "==> Scanning webapp..."
cd "$ROOT_DIR/webapp"
#mvn --batch-mode verify sonar:sonar -DskipTests \
#  -Dsonar.host.url="$SONAR_URL" \
#  -Dsonar.token="$SONAR_TOKEN" \
#  -Dsonar.projectKey=GameArenaWebapp \
#  -Dsonar.projectName="GameArena Webapp" \
#  -Dsonar.scm.exclusions.disabled=true
mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
  -Dsonar.host.url="$SONAR_URL" \
  -Dsonar.token="$SONAR_TOKEN"

echo ""
echo "==> Done. Results: $SONAR_URL/projects"
