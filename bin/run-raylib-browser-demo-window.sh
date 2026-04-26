#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
HOST="127.0.0.1"
PORT="${VYBE_RAYLIB_BROWSER_PORT:-8788}"
DEBUG_PORT="${VYBE_RAYLIB_BROWSER_DEBUG_PORT:-9228}"
CHROME_PROFILE="$ROOT/target/raylib-browser-demo-chrome-profile"
URL="http://$HOST:$PORT/vybe/wasm/browser-demo/raylib-demo.html"
LOG="$ROOT/target/raylib-browser-demo-window.log"
PID_FILE="$ROOT/target/raylib-browser-demo-window.pid"

if [[ ! -f "$ROOT/resources/vybe/wasm/browser-demo/raylib-demo.js" || ! -f "$ROOT/resources/vybe/wasm/browser-demo/raylib-demo.wasm" ]]; then
  "$ROOT/bin/build-raylib-browser-demo.sh"
fi

mkdir -p "$ROOT/target"
if [[ -f "$PID_FILE" ]] && kill -0 "$(cat "$PID_FILE")" 2>/dev/null; then
  kill "$(cat "$PID_FILE")" 2>/dev/null || true
fi

cd "$ROOT/resources"
nohup python3 -m http.server "$PORT" --bind "$HOST" >"$LOG" 2>&1 &
printf '%s\n' "$!" >"$PID_FILE"
cd "$ROOT"
sleep 0.5

if command -v osascript >/dev/null 2>&1 && [[ -d "/Applications/Google Chrome.app" ]]; then
  open -na "Google Chrome" --args --user-data-dir="$CHROME_PROFILE" --remote-debugging-port="$DEBUG_PORT" --app="$URL" --new-window
elif command -v open >/dev/null 2>&1; then
  open "$URL"
elif command -v xdg-open >/dev/null 2>&1; then
  xdg-open "$URL"
else
  printf 'Open %s\n' "$URL"
fi

printf 'Raylib browser demo window: %s\nHTTP server pid: %s\nChrome debug: http://127.0.0.1:%s/json\nLog: %s\n' "$URL" "$(cat "$PID_FILE")" "$DEBUG_PORT" "$LOG"
