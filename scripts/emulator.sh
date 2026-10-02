#!/usr/bin/env bash
# Sobe o AVD do Fitvara. Cria o AVD na primeira execucao.
#   ./scripts/emulator.sh            -> janela normal
#   ./scripts/emulator.sh --headless -> sem janela (CI / so adb)
# FITVARA_GPU=swiftshader_indirect forca render por software, se a GPU do host falhar.
set -euo pipefail

export ANDROID_HOME="${ANDROID_HOME:-$HOME/Android/Sdk}"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
export JAVA_HOME="${JAVA_HOME:-$HOME/.jdks/temurin-21}"
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$ANDROID_HOME/cmdline-tools/latest/bin:$PATH"

AVD=fitvara_api35
IMAGE="system-images;android-35;default;x86_64"

if ! avdmanager list avd 2>/dev/null | grep -q "Name: $AVD"; then
  echo "==> Criando AVD $AVD"
  echo "no" | avdmanager create avd -n "$AVD" -k "$IMAGE" -d pixel_7 --force
  CFG="$HOME/.android/avd/$AVD.avd/config.ini"
  {
    echo "hw.ramSize=2048"
    echo "hw.lcd.density=440"
    echo "hw.keyboard=yes"
    echo "disk.dataPartition.size=4G"
  } >> "$CFG"
fi

ARGS=(-avd "$AVD" -no-boot-anim -no-snapshot-save -gpu "${FITVARA_GPU:-host}" -cores 4 -memory 3072)
[ "${1:-}" = "--headless" ] && ARGS+=(-no-window)

echo "==> Subindo $AVD"
exec emulator "${ARGS[@]}"
