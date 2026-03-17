# Nexus RFID

## Visao Geral
Aplicativo Android nativo para operacoes de RFID e estoque. O projeto nasce em um app Android Studio ja configurado com Kotlin e Jetpack Compose, mas ainda em estado inicial. O primeiro marco do produto e reproduzir com alta fidelidade visual o sistema legado mostrado nas screenshots, preservando o estilo corporativo claro, denso e funcional. A integracao real com leitores RFID C72 e UR6/R6 ficara para fases posteriores, usando como referencia o material legado presente na pasta `Nexus RFID/RFID R6 and C72`.

## Valor Central
Migrar o app legado para uma base Android moderna sem perder a aparencia operacional que os usuarios ja conhecem, reduzindo risco visual agora e risco de integracao depois.

## Objetivos do Marco Atual
- Recriar em Compose as telas principais do app legado com dados mockados.
- Estruturar um design system interno simples, feito para preservar a estrutura do legado e aplicar a identidade visual Nexus em variante light.
- Organizar navegacao, componentes, mocks e previews para acelerar as proximas entregas.
- Deixar a futura integracao do hardware documentada e isolada desde o inicio.

## Fora do Escopo do Marco Atual
- RFID real.
- Backend real.
- Login real.
- Banco de dados real.
- Persistencia local definitiva.
- Camera e leitura real de codigo de barras.
- Regras complexas de negocio.

## Fontes de Contexto Confirmadas
- `Nexus RFID/Prompt.md`: escopo funcional, ordem de entrega, restricoes visuais e estrutura sugerida.
- `Nexus RFID/Fotos-Refecenia/`: 23 screenshots do app legado, usadas como fonte principal de fidelidade visual.
- `Nexus RFID/pipeline-dev-software.html`: referencia de organizacao de tokens, consistencia estrutural e componentizacao.
- `Nexus RFID/RFID R6 and C72/demo-uhf_bluetooth_as`: demo Android legado com `cw-deviceapi.jar`, bibliotecas nativas, permissoes e telas de leitor para futura integracao C72/UR6.
- `Nexus Service Desk/`: referencia de linguagem visual da marca Nexus, usada como base para a nova variante light do RFID.
- `.planning/VISUAL-IDENTITY.md`: documento vivo da nova identidade visual do produto.

## Contexto Tecnico Atual
- Projeto Android Studio raiz: `Nexus RFID`.
- Stack ja presente: Kotlin + Jetpack Compose + Material 3 como base tecnica.
- Namespace atual: `com.example.nexusrfid`.
- `minSdk`: 23.
- `targetSdk`: 36.
- Estado atual do app: base Compose da Fase 1 concluida, com `NexusRfidApp`, `AppNavHost`, tema proprio, componentes base e mocks centralizados.
- Referencia de hardware: projeto Android antigo com `compileSdkVersion 28`, `minSdkVersion 18`, permissoes de Bluetooth, localizacao, NFC e dependencia `libs/cw-deviceapi.jar`.

## Direcao de Produto
1. Primeiro copiar a interface e a navegacao com fidelidade.
2. Depois estabilizar componentes, mocks e estados visuais.
3. So entao iniciar a camada de integracao com o leitor RFID real.
4. Encapsular detalhes do SDK legado para nao contaminar a UI Compose.

## Diretrizes Visuais Obrigatorias
- Fundo claro ou cinza muito claro.
- Barra superior em azul Nexus escuro com titulo claro.
- Botao principal em azul Nexus.
- Acoes positivas em verde quando isso aparecer na referencia.
- Cartoes opacos, limpos e com borda leve.
- Listas densas, objetivas e com divisores finos.
- Tipografia limpa, sem exagero de contraste.
- Nada de glow, futurismo exagerado ou visual gamer.
- Textos em portugues, preservando nomes e truncamentos vistos nas imagens.
- O legado define a estrutura operacional; a familia Nexus define a identidade visual final.

## Telas Mapeadas
1. Login.
2. Departamentos.
3. Inventario.
4. Drawer lateral.
5. Produtos.
6. Busca Global.
7. Dialogo de adicionar codigo reduzido.
8. Dialogo de adicionar EAN-13.
9. Dialogo de adicionar Tag.
10. Buscar Global Produto.
11. Associar Tags.
12. Nota Fiscal.
13. Movimentacao com Conferencia.
14. Configuracoes.
15. Modal de selecao de leitor/dispositivo.
16. Modal de configuracao automatica/manual.
17. Dialogo de permissao de localizacao/dispositivo.
18. Leitura Livre.
19. Bottom sheet ou tela de dispositivos.

## Componentes Reutilizaveis Esperados
- `AppTopBar`
- `DrawerMenu`
- `InventoryCard`
- `SimpleListRow`
- `SearchHeader`
- `CounterBar`
- `ActionButtonPrimary`
- `ActionButtonOutline`
- `EmptyStateBox`
- `AppDialog`
- `AppBottomSheet`
- `SettingRow`
- `ToggleRow`
- `SetGetRow`

## Estrutura Inicial Proposta
- `app/src/main/java/.../ui/theme`
- `app/src/main/java/.../ui/components`
- `app/src/main/java/.../ui/navigation`
- `app/src/main/java/.../ui/screens/login`
- `app/src/main/java/.../ui/screens/departamentos`
- `app/src/main/java/.../ui/screens/inventario`
- `app/src/main/java/.../ui/screens/produtos`
- `app/src/main/java/.../ui/screens/busca_global`
- `app/src/main/java/.../ui/screens/associar_tag`
- `app/src/main/java/.../ui/screens/nota_fiscal`
- `app/src/main/java/.../ui/screens/movimentacao`
- `app/src/main/java/.../ui/screens/configuracoes`
- `app/src/main/java/.../ui/screens/leitura_livre`
- `app/src/main/java/.../data/mock`
- `app/src/main/java/.../domain`
- `app/src/main/java/.../rfid` para a futura integracao real

## Estrategia de Integracao RFID Futura
- Tratar a pasta `demo-uhf_bluetooth_as` como fonte tecnica de protocolo, permissoes, fluxo de conexao e opcoes do leitor.
- Extrair comportamento relevante dos fragmentos legados (`UHFSetFragment`, `UHFReadTagFragment`, `BarcodeFragment` e afins) sem copiar a UI XML antiga.
- Criar adaptadores proprios para permissao, descoberta de dispositivo, conexao e comandos do leitor.
- Preservar a UI Compose separada da camada de hardware para permitir simulacao e testes.

## Decisoes Iniciais
| Data | Decisao | Motivo |
|------|---------|--------|
| 2026-03-16 | Usar Compose com Material 3 apenas como base tecnica | Permite modernizar a implementacao sem modernizar demais o visual |
| 2026-03-16 | Criar tema proprio com tokens simples | Necessario para aproximar a aparencia do legado |
| 2026-03-16 | Separar fase visual da fase de hardware | Evita misturar risco de UI com risco de SDK legado |
| 2026-03-16 | Manter a interface em portugues | Exigencia explicita do material de referencia |
| 2026-03-16 | Reservar uma camada `rfid` para integracao futura | Reduz acoplamento com `cw-deviceapi.jar` e bibliotecas nativas |
| 2026-03-16 | Centralizar a leitura das screenshots em `01-REFERENCE-MAP.md` | Torna a fidelidade visual consultavel e reaproveitavel nas proximas fases |
| 2026-03-16 | Estruturar a base do app com `NexusRfidApp` e `AppNavHost` | Remove o template inicial e prepara a navegacao local para os fluxos reais |
| 2026-03-16 | Fixar o tema em modo claro sem cor dinamica | Preserva a identidade corporativa do legado e evita variacao automatica de cor |
| 2026-03-16 | Tratar a antiga tela `Unidades` como `Departamentos` a partir da Fase 2 | Alinha a nomenclatura com o fluxo aprovado para depois do login |
| 2026-03-16 | Fazer `Login` ser a entrada principal real do app, sem tela tecnica intermediaria | Mantem o fluxo operacional mais proximo do uso esperado pelo usuario |
| 2026-03-16 | Adotar uma identidade Nexus light inspirada no `Nexus Service Desk`, mantendo as screenshots do RFID como fonte de estrutura | Separa melhor layout operacional de linguagem visual de marca |

## Riscos e Atencoes
- As screenshots ainda nao tem nomes semanticamente claros; o mapeamento fino de cada imagem para cada tela pode exigir ajuste durante a implementacao.
- O SDK legado de RFID usa stack Android antiga e pode exigir adaptacao para Android atual, principalmente em permissoes e compatibilidade de bibliotecas nativas.
- O namespace atual ainda e generico (`com.example.nexusrfid`) e pode precisar ser renomeado quando a base estiver estavel.
- A fidelidade visual depende de comparar continuamente a implementacao com as 23 imagens, nao apenas com o prompt textual.
