# CapiFIT — especificação e progresso

Documento vivo: o que já está feito, o que vem a seguir e as regras que o projeto
segue. Atualizar a cada tela entregue.

- **Fonte da verdade do visual:** os SVGs em `app/assets/` (`00-estilos.svg` define a
  paleta e a tipografia; `01` a `06` são as telas). O código anterior a 15/10/2026 era
  protótipo e está sendo substituído tela a tela.
- **Última atualização:** 22/10/2026 — as seis telas do desenho entregues (início, nova
  atividade, histórico e configurações só visuais).

---

## 1. Produto

CapiFIT é um app Android de registro de trilhas e treinos (corrida, trail e montanha),
com mascote capivara. Navegação prevista no desenho (`app/assets/LEIA-ME.md`):

| Origem | Gatilho | Destino |
| --- | --- | --- |
| Login | **Entrar** e **Continuar como convidado** | Perfil *(decidido pelo time; o LEIA-ME original dizia Início)* |
| Qualquer tela | casinha do cabeçalho | Início |
| Qualquer tela | botão **A** do cabeçalho | Configurações |
| Barra inferior | **Histórico** / **Logar** / **Perfil** | Histórico / Nova atividade / Perfil |
| Nova atividade | **Salvar atividade** | Histórico |
| Configurações | **Sair da conta** | Login |

## 2. Regras do projeto

1. **Uma tela por vez.** Entregar a tela da vez completa antes de começar a próxima.
2. **Por enquanto, só visual.** Desde 22/10/2026 as telas novas copiam o SVG com textos
   e números fixos. Estado só para aparência (chip selecionado, campo em foco) e a
   navegação entre telas. Filtros não trocam dados; nada de cálculo, agregação ou
   dados de exemplo derivados. Lógica e regra de negócio entram quando forem pedidas.
   Login e Perfil foram feitos antes, sob a regra anterior ("telas funcionais, sem
   regra de negócio"), e por isso validam formato e abrem diálogos.
3. **Gradle congelado.** AGP 8.7.2, Kotlin 2.0.21, Gradle 8.10.2, Compose BOM
   2024.10.01, compileSdk/targetSdk 35, minSdk 24. Sem dependência nova: sem
   navigation-compose, sem Room/DataStore, sem material-icons-extended, sem
   ui-text-google-fonts.
4. **Arquitetura simples.** Navegação por estado em `MainActivity`; estado de tela com
   `remember`/`rememberSaveable`; sem ViewModel, DI ou camada de dados por enquanto.
5. **Código em português**, no mesmo tom do que já existe (nomes e comentários curtos
   explicando o porquê).
6. **Fidelidade ao desenho**: medidas, cores e textos saem do SVG correspondente.

## 3. Design system (implementado)

### Paleta — `ui/theme/Color.kt`

| Token | Hex | Uso |
| --- | --- | --- |
| `Areia` | `#E7DCC4` | fundo das telas |
| `Creme` | `#F7F1E3` | texto sobre verde, barra inferior, diálogos |
| `Branco` | `#FFFFFF` | campos e cartões (alpha 0.62–0.68 nos cartões) |
| `Duna` | `#EADFC7` | botão casinha, chips |
| `ChipCreme` | `#F4EEDD` | chip não selecionado |
| `Folha` | `#3E7C4F` | ação principal, item selecionado |
| `FolhaEscura` | `#2E603D` | ícones e textos verdes sobre claro |
| `Sol` | `#F2A93B` | avatar, ação secundária |
| `Barro` | `#E0603C` | destrutivo e erro |
| `SolEscuro` | `#BD741D` | selo "Fácil" do histórico |
| `FolhaClara` | `#DDEBDF` | fundo do botão Editar do histórico |
| `BarroClaro` | `#F7DED7` | fundo do botão Excluir do histórico |
| `Tinta` | `#2A241C` | texto principal |
| `TintaSuave` | `#756E64` | texto secundário |
| `TintaFantasma` | `#9A9287` | placeholder de campo |
| `Borda` | `#D7CDB8` | contornos e divisores |

Mascote: `CapivaraClara`, `Capivara`, `CapivaraPata`, `CapivaraEscura`, `Coral`,
`CoralClaro`.

### Tipografia — `ui/theme/Type.kt`

Fredoka e DM Sans não estão no projeto; a escala usa a fonte do sistema com os
tamanhos e pesos do desenho: `headlineSmall` 20/SemiBold, `titleLarge` 19/SemiBold,
`titleMedium` 16/SemiBold, `titleSmall` 14/SemiBold, `bodyLarge` 13, `bodyMedium` 12,
`bodySmall` 11, `labelLarge` 12/Medium, `labelSmall` 10/Medium.

### Tema — `ui/theme/Theme.kt`

Um único `lightColorScheme`. Sem tema escuro e sem dynamic color: o desenho só existe
em claro. `MainActivity` força ícones escuros nas barras do sistema.

### Componentes reutilizáveis

| Componente | Arquivo | Onde já é usado |
| --- | --- | --- |
| `CampoTexto` | `ui/components/Controles.kt` | login, diálogo de editar perfil, nova atividade (inclusive `multilinha` nas notas) |
| `ChipFiltro` | `ui/components/Controles.kt` | início (filtros), nova atividade (tipo e dificuldade, com `weight(1f)`) |
| `BotaoPrincipal` | `ui/components/Controles.kt` | login, nova atividade (em `Barro`) |
| `CabecalhoTopo` | `ui/components/Navegacao.kt` | início, nova atividade, histórico, perfil (título em 18, como em 02–05; configurações não usa o título dele) |
| `BarraNavegacao` | `ui/components/Navegacao.kt` | início, nova atividade, histórico, perfil, configurações |
| `EsqueletoTela` | `ui/components/Navegacao.kt` | início, nova atividade, histórico, perfil, configurações |
| `Navegacao` (callbacks) e `ItemBarra` | `ui/components/Navegacao.kt` | `MainActivity` |
| `CapivaraMascote` | `ui/components/CapivaraMascote.kt` | login, avatar do perfil |

Ícones (casinha, relógio, pessoa, olho e "+") são desenhados em `Canvas` seguindo os
traços do SVG — o projeto não tem biblioteca de ícones estendida.

## 4. Estado das telas

| # | Tela | Desenho | Status | Arquivo |
| --- | --- | --- | --- | --- |
| 1 | Login | `01-login.svg` | ✅ feito | `ui/login/LoginScreen.kt` |
| 2 | Início / progresso | `02-inicio-progresso.svg` | ✅ feito (só visual) | `ui/inicio/InicioScreen.kt` |
| 3 | Nova atividade | `03-nova-atividade.svg` | ✅ feito (só visual) | `ui/atividade/NovaAtividadeScreen.kt` |
| 4 | Histórico | `04-historico.svg` | ✅ feito (só visual) | `ui/historico/HistoricoScreen.kt` |
| 5 | Perfil | `05-perfil.svg` | ✅ feito | `ui/perfil/PerfilScreen.kt` |
| 6 | Configurações | `06-configuracoes.svg` | ✅ feito (só visual) | `ui/configuracoes/ConfiguracoesScreen.kt` |

## 5. O que já está feito

### 5.1 Login — `ui/login/LoginScreen.kt`

Layout do `01-login.svg`: marca "C · CapiFIT" com tagline, capivara no círculo Sol,
"Bem-vindo de volta!", campos E-mail e Senha, botão **Entrar**, divisor "ou", botão
**Continuar como convidado** e rodapé fixo.

Funciona:

- campos em `rememberSaveable`, teclado com `Next`/`Done`, `imePadding` e rolagem em
  telas baixas;
- validação de formato em `ui/login/ValidacaoLogin.kt` (`emailValido`, `senhaValida`,
  `SENHA_MINIMA = 6`), erro inline em `Barro` e borda vermelha; o erro some ao digitar;
- botão de olho (mostrar/ocultar senha) — único controle fora do desenho;
- a capivara tapa os olhos quando a senha está em foco e levanta halter no envio;
- **Entrar** valida, mostra estado de carregando e vai para o perfil;
  **Continuar como convidado** vai direto;
- testes JVM em `app/src/test/java/com/fitvara/app/ui/login/ValidacaoLoginTest.kt`.

### 5.2 Perfil — `ui/perfil/PerfilScreen.kt`

Layout do `05-perfil.svg`: cabeçalho com casinha e botão "A", avatar da capivara em
círculo Sol, nome e legenda, três cartões de número (48 trilhas / 312 km / 9,4 km
rec.), bloco **CONQUISTAS** com três linhas e as ações **Editar perfil** e
**Excluir conta**, sobre a barra inferior.

Funciona:

- **Editar perfil** abre diálogo com Nome e Legenda (usa `CampoTexto`); Salvar atualiza
  o topo da tela na hora e fica desabilitado com nome vazio;
- **Excluir conta** abre confirmação; confirmar volta para o login;
- barra inferior navega e marca o item atual em verde (destaque não existe no desenho,
  foi adicionado por usabilidade);
- números e conquistas são **dados de exemplo** fixos no arquivo, iguais aos do SVG.

### 5.3 Início — `ui/inicio/InicioScreen.kt`

Layout do `02-inicio-progresso.svg`: cabeçalho "Seu progresso," + nome, chips Todos /
Run / Trail / Mountain, cartão grande ("Últimas 12 semanas", "Todos os treinos", selo
"8 atividades", Tempo 23h 30min, Distância 52,0 km, gráfico de linha com área, guias,
pontos e meses JUL–OUT) e os cartões Sequência (12 dias seguidos) e Desnível no mês
(320 metros acumulados), sobre a barra inferior.

Só visual:

- tocar num chip muda apenas qual fica verde; números e gráfico não mudam;
- todos os textos e números são constantes copiadas do SVG; o gráfico usa as alturas
  dos pontos medidas no desenho (`PontosDoGrafico`);
- cabeçalho e barra inferior navegam como nas outras telas; nenhum item da barra fica
  destacado, porque Início não é destino dela.

### 5.4 Nova atividade — `ui/atividade/NovaAtividadeScreen.kt`

Layout do `03-nova-atividade.svg`: cabeçalho "Nova atividade" / "Registre como foi o
treino de hoje", campo Nome da atividade, chips Tipo de treino (Run / Trail /
Mountain), Data e Duração lado a lado, Distância (km) e Desnível (m) lado a lado, chips
Dificuldade (Fácil / Média / Alta), Notas (campo alto) e **Salvar atividade** em
`Barro`, sobre a barra inferior com **Logar** destacado.

Só visual:

- os campos aceitam digitação e começam vazios; os exemplos do desenho (01/10/2026,
  2h 30min, 5,1, 280) aparecem como dica cinza. Data, distância e desnível abrem
  teclado numérico; nada é validado;
- os chips só mudam o destaque e começam em Trail e Média, como no desenho;
- **Salvar atividade** só navega para o Histórico; nada é gravado.

### 5.5 Histórico — `ui/historico/HistoricoScreen.kt`

Layout do `04-historico.svg`: cabeçalho "Histórico" / "8 atividades registradas" e os
quatro cartões do desenho (Trilha do Mirante, Caminho da Serra, Pico da Neblina,
Trilha das Cachoeiras), cada um com nome, selo de dificuldade, tipo · data, métricas,
divisor e os botões **Editar** e **Excluir**, sobre a barra inferior com
**Histórico** destacado.

Só visual (decidido com o time em 22/10/2026):

- a lista e o subtítulo são fixos, copiados do SVG. O desenho diz 8 atividades, mas só
  mostra 4, e a tela mostra essas 4;
- **Editar** só navega para a nova atividade, que abre vazia;
- **Excluir** abre a confirmação com o nome da atividade; confirmar ou cancelar só
  fecha o diálogo, e o cartão continua na lista.

### 5.6 Configurações — `ui/configuracoes/ConfiguracoesScreen.kt`

Layout do `06-configuracoes.svg`: tela longa e rolável, com os botões do cabeçalho
sozinhos no topo e o título "Configurações" (20) / "Ajuste o CapiFIT do seu jeito"
abaixo deles. Seções CONTA, PREFERÊNCIAS, TRILHAS, PRIVACIDADE e SOBRE, linhas
claras de 48dp e **Sair da conta** em `Barro` no fim, sobre a barra inferior.

Só visual:

- linhas com seta (`LinhaConfig`) mostram o valor do desenho (Google · e-mail, Claro,
  km · m, Português (BR), 1.0.0) e não abrem nada;
- os quatro switches (`LinhaSwitch` + `Interruptor` de 34×20, como no desenho)
  começam ligados e só mudam a própria aparência; desligado, o trilho fica cor de
  `Borda` (o SVG não mostra esse estado);
- **Sair da conta** volta para o login, sem confirmação.

`LinhaConfig`, `LinhaSwitch` e `Interruptor` ficam privados na tela: só ela os usa.

### 5.7 Navegação

`MainActivity` guarda `Tela { Login, Inicio, NovaAtividade, Historico, Perfil,
Configuracoes }` em `rememberSaveable` e distribui um `Navegacao` com os callbacks.
Todas as seis telas já existem; o `TelaEmConstrucao` que segurava o lugar delas foi
removido.

## 6. Próximos passos

As seis telas do desenho estão prontas. O que falta é lógica e regra de negócio, e
cada item abaixo só começa quando for pedido (regra 2).

### 6.1 Regra de negócio

- Decidir onde as atividades moram e quem as expõe às telas.
- Validação e gravação da nova atividade (formato de data, duração e números).
- Histórico lendo as atividades de verdade: contagem no subtítulo, Editar abrindo a
  nova atividade preenchida e Excluir tirando o cartão.
- Autenticação de verdade no lugar do `delay(700)` de `LoginScreen.entrar()`.
- Persistência (Room ou DataStore) — primeira decisão que exige mexer no Gradle.
- Cálculo real de progresso, sequência, desnível e conquistas; filtros do Início
  trocando os dados do cartão.
- Preferências com efeito: unidades, idioma, tema, notificações, GPS.

## 7. Pendências conhecidas

| Item | Situação |
| --- | --- |
| Fontes Fredoka e DM Sans | substituídas pela fonte do sistema; entram se os `.ttf` forem adicionados em `res/font` |
| Mascote | desenhado em Compose `Canvas`; o SVG usa um PNG |
| Botão de olho na senha | adicionado fora do desenho, por usabilidade |
| Item ativo na barra inferior | destaque verde não existe no desenho |
| `app_name` | passou de "FitVara" para "CapiFIT"; `applicationId` segue `com.fitvara.app` e o projeto Gradle, `Fitvara` |
| Tema escuro | removido; o desenho só tem versão clara |
| Dados de exemplo | nome, números e conquistas fixos no código das telas |
| Histórico | 4 cartões fixos com subtítulo "8 atividades" (como no SVG); Excluir não remove |
| Filtros do Início | só mudam o destaque; os dados do cartão são sempre os de "Todos" |
| Nome do usuário | Início mostra "Ana Souza" fixo; editar no Perfil não reflete ali |
| Configurações | linhas com seta não abrem nada; switches não são lembrados ao sair da tela de vez; Sair não pede confirmação |
| Login e Perfil | feitos antes da regra "só visual": validam formato e abrem diálogos; ficaram como estavam |
| Voltar (botão do sistema) | sem tratamento: sai do app em vez de voltar à tela anterior |

## 8. Como rodar e verificar

```bash
# build + testes (JDK 17+; nesta máquina, ~/.jdks/jbr-21.0.11)
./gradlew assembleDebug testDebugUnitTest

# instalar e abrir no emulador
./scripts/run.sh
```

No Android Studio, cada tela pronta tem `@Preview` 390×844 para comparar lado a lado
com o SVG correspondente. Build debug no emulador demora cerca de 1 min até o primeiro
frame.

## 9. Histórico

| Data | Entrega |
| --- | --- |
| 15/10/2026 | Paleta Trailside Pop, tipografia, tema claro, `CampoTexto` e `BotaoPrincipal`, tela de login, validação + testes, navegação por estado |
| 16/10/2026 | Cabeçalho, barra inferior, `EsqueletoTela`, tela de perfil com editar/excluir, placeholders das 4 telas restantes |
| 22/10/2026 | Regra 2 passa a ser "só visual"; `ChipFiltro`; título do cabeçalho em 18; tela de início; `CampoTexto` multilinha; tela de nova atividade; cores do histórico; tela de histórico; tela de configurações; `TelaEmConstrucao` removido |
