#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
HOST="127.0.0.1"
PORT="${VYBE_RAYLIB_BROWSER_PORT:-8787}"
DEBUG_PORT="${VYBE_RAYLIB_BROWSER_DEBUG_PORT:-9227}"
CHROME_PROFILE="$ROOT/target/raylib-browser-chrome-profile"
URL="http://$HOST:$PORT/vybe/wasm/browser/raylib-host.html"
LOG="$ROOT/target/raylib-browser-window.log"
PID_FILE="$ROOT/target/raylib-browser-window.pid"
CHROME_PID_FILE="$ROOT/target/raylib-browser-window.chrome.pid"

if [[ ! -f "$ROOT/resources/vybe/wasm/browser/raylib.js" || ! -f "$ROOT/resources/vybe/wasm/browser/raylib.wasm" ]]; then
  "$ROOT/bin/build-raylib-browser-wasm.sh"
fi

mkdir -p "$ROOT/target"
if [[ -f "$PID_FILE" ]] && kill -0 "$(cat "$PID_FILE")" 2>/dev/null; then
  kill "$(cat "$PID_FILE")" 2>/dev/null || true
fi
if [[ -f "$CHROME_PID_FILE" ]] && kill -0 "$(cat "$CHROME_PID_FILE")" 2>/dev/null; then
  kill "$(cat "$CHROME_PID_FILE")" 2>/dev/null || true
fi
pkill -f "$CHROME_PROFILE" 2>/dev/null || true
rm -rf "$CHROME_PROFILE"

cd "$ROOT/resources"
nohup python3 -m http.server "$PORT" --bind "$HOST" >"$LOG" 2>&1 &
printf '%s\n' "$!" >"$PID_FILE"
cd "$ROOT"
sleep 0.5

if [[ -x "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" ]]; then
  nohup "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" \
    --user-data-dir="$CHROME_PROFILE" \
    --remote-debugging-port="$DEBUG_PORT" \
    --disable-http-cache \
    --disable-application-cache \
    --disk-cache-size=1 \
    --media-cache-size=1 \
    --app="$URL" \
    --new-window \
    >"$ROOT/target/raylib-browser-chrome.log" 2>&1 &
  printf '%s\n' "$!" >"$CHROME_PID_FILE"
elif [[ -x "/Applications/Chromium.app/Contents/MacOS/Chromium" ]]; then
  nohup "/Applications/Chromium.app/Contents/MacOS/Chromium" \
    --user-data-dir="$CHROME_PROFILE" \
    --remote-debugging-port="$DEBUG_PORT" \
    --disable-http-cache \
    --disable-application-cache \
    --disk-cache-size=1 \
    --media-cache-size=1 \
    --app="$URL" \
    --new-window \
    >"$ROOT/target/raylib-browser-chrome.log" 2>&1 &
  printf '%s\n' "$!" >"$CHROME_PID_FILE"
elif command -v open >/dev/null 2>&1; then
  open "$URL"
elif command -v xdg-open >/dev/null 2>&1; then
  xdg-open "$URL"
else
  printf 'Open %s\n' "$URL"
fi

printf 'Raylib browser window: %s\nHTTP server pid: %s\nChrome pid: %s\nChrome debug: http://127.0.0.1:%s/json\nLog: %s\n' "$URL" "$(cat "$PID_FILE")" "$(cat "$CHROME_PID_FILE" 2>/dev/null || true)" "$DEBUG_PORT" "$LOG"
