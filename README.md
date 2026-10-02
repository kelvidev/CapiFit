# Fitvara

App Android nativo em Kotlin + Jetpack Compose (Material 3).

| Item | Valor |
| --- | --- |
| Gradle | 8.10.2 (wrapper incluso) |
| Android Gradle Plugin | 8.7.2 |
| Kotlin | 2.0.21 |
| compileSdk / targetSdk | 35 |
| minSdk | 24 |
| applicationId | `com.fitvara.app` |

## Primeiro clone

O repositorio nao carrega nada que dependa da maquina de quem clonou:

1. **Android Studio** (Ladybug ou mais novo): *File > Open* na raiz do projeto e
   aceite o sync do Gradle. O Studio cria o `local.properties` apontando pro SDK
   dele e usa o proprio JDK embutido (*Settings > Build Tools > Gradle > Gradle JDK*,
   precisa ser **JDK 17+**; o projeto compila com JDK 21).
2. **Terminal**: precisa de um JDK 21 visivel pro Gradle, seja via `JAVA_HOME`,
   seja via `org.gradle.java.home` no seu `~/.gradle/gradle.properties` (arquivo
   pessoal, fora do repo). Depois e so `./gradlew assembleDebug`.

`local.properties`, `.idea/`, `build/` e `.gradle/` sao gerados localmente e estao
no `.gitignore` de proposito — nao commite nenhum deles.

> O `.vscode/` versionado tem caminhos absolutos da maquina original
> (`/home/study/...`). Quem usa Android Studio pode ignorar essa pasta; quem for
> usar VS Code precisa ajustar os caminhos de `ANDROID_HOME` e `JAVA_HOME` ali.

## Rodar no emulador

Pelo VS Code (`Ctrl+Shift+P` > *Tasks: Run Task*):

1. **fitvara: subir emulador** — cria o AVD `fitvara_api35` na primeira vez e inicia
2. **fitvara: rodar no emulador** — compila, instala e abre o app

`Ctrl+Shift+B` compila o APK debug. `F5` roda com breakpoints (precisa da extensao
`adelphes.android-dev-ext`, ja instalada).

Pelo terminal:

```bash
./scripts/emulator.sh     # sobe o emulador (--headless pra sem janela)
./scripts/run.sh          # compila, instala e abre o app
```

O emulador usa a GPU do host (`-gpu host`). Com `swiftshader` (render por software)
o SystemUI dava ANR nesta maquina. Se a GPU do host falhar um dia:
`FITVARA_GPU=swiftshader_indirect ./scripts/emulator.sh`.

## Ambiente desta maquina

O setup abaixo foi instalado junto com o projeto:

- **Android SDK**: `~/Android/Sdk` (platform-tools, platforms;android-35,
  build-tools 34.0.0 e 35.0.0, emulator 37.3.2, system image `android-35;default;x86_64`)
- **AVD**: `fitvara_api35` (Pixel 7, Android 15, x86_64, KVM acelerado)
- **JDK**: `~/.jdks/temurin-21` — a maquina tinha somente JREs (sem `javac`),
  entao o Temurin JDK 21 foi baixado. O caminho fica em `~/.gradle/gradle.properties`
  (`org.gradle.java.home`) e no `.vscode/settings.json`. Ele **nao** entra no
  `gradle.properties` do projeto: esse arquivo e versionado, e um caminho desta
  maquina quebraria o build de todo mundo que clonasse.
- `ANDROID_HOME` e o PATH do SDK foram adicionados ao `~/.bashrc`.
- `local.properties` aponta para o SDK (arquivo local, fora do versionamento).

Se o SDK ou o JDK mudarem de lugar, ajuste `~/.gradle/gradle.properties`,
`local.properties` e `.vscode/settings.json` — todos fora do versionamento ou
locais a esta maquina.

## Estrutura

```
app/src/main/java/com/fitvara/app/
  MainActivity.kt          tela inicial em Compose
  ui/theme/                Color.kt, Theme.kt, Type.kt (Material 3 + dynamic color)
app/src/test/              testes unitarios JVM
gradle/libs.versions.toml  versoes centralizadas (version catalog)
scripts/emulator.sh        cria e sobe o AVD
scripts/run.sh             build + install + launch
```
