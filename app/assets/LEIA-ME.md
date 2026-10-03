# CapiFIT — pacote editável para Figma

## Como importar
1. No Figma, crie um arquivo novo.
2. Arraste os arquivos `.svg` deste pacote para a página.
3. Cada SVG entra como um grupo de camadas editáveis; renomeie o grupo como um frame.
4. Instale **Fredoka** e **DM Sans** ou aceite a substituição de fonte do Figma.
5. Use `00-estilos.svg` como referência para cores, tipografia e componentes.

## Ordem das telas
- `01-login.svg`
- `02-inicio-progresso.svg`
- `03-nova-atividade.svg`
- `04-historico.svg`
- `05-perfil.svg`
- `06-configuracoes.svg`

## Conexões no modo Protótipo
- Login: **Entrar** e **Continuar como convidado** → Início.
- Casinha do cabeçalho → Início.
- Botão **A** → Configurações.
- Navegação inferior: **Histórico** → Histórico; **Logar** → Nova atividade; **Perfil** → Perfil.
- Nova atividade: **Salvar atividade** → Histórico.
- Configurações: **Sair da conta** → Login.

## Observações
- Formas e textos são vetores editáveis.
- O mascote é uma imagem PNG incorporada nos SVGs.
- Configurações é uma tela longa para representar rolagem vertical; no Figma, coloque seu conteúdo dentro de um frame móvel e ative rolagem vertical no modo Protótipo.
- As interações e o gráfico representam o protótipo visual; dados e botões não têm lógica após a importação.
