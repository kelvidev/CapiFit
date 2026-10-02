#!/usr/bin/env bash
# Compila, instala e abre o Fitvara no device/emulador conectado.
set -euo pipefail

cd "$(dirname "$0")/.."

APP_ID="com.fitvara.app"
ACTIVITY="$APP_ID/.MainActivity"

if ! command -v adb >/dev/null 2>&1; then
  echo "ERRO: adb nao encontrado no PATH. Instale o Android SDK platform-tools." >&2
  exit 1
fi

echo "==> Aguardando device/emulador..."
adb start-server >/dev/null 2>&1 || true
if [ -z "$(adb devices | awk 'NR>1 && $2=="device"')" ]; then
  echo "Nenhum device pronto. Conecte o emulador (adb devices) e rode de novo." >&2
  echo "Aguardando ate 60s..."
  timeout 60 adb wait-for-device || { echo "Timeout esperando device." >&2; exit 1; }
fi
adb shell 'while [[ -z $(getprop sys.boot_completed) ]]; do sleep 1; done' >/dev/null 2>&1 || true

echo "==> Instalando debug APK..."
./gradlew installDebug

echo "==> Abrindo $ACTIVITY ..."
adb shell am start -n "$ACTIVITY" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER

echo "==> Pronto. Logcat:  adb logcat --pid=\$(adb shell pidof -s $APP_ID)"
