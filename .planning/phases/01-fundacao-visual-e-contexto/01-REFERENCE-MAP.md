# Phase 1 - Reference Map

## Objetivo

Consolidar a leitura visual do app legado para orientar a implementacao das fases seguintes sem reinterpretar screenshots a cada tela.

## Fontes Utilizadas

- `Nexus RFID/Prompt.md`
- `Nexus RFID/Fotos-Refecenia/`
- `Nexus RFID/pipeline-dev-software.html`

## Mapeamento de Screenshots

| Arquivo | Tela / Fluxo | Estado identificado | Elementos visuais principais | Observacoes de fidelidade |
|---|---|---|---|---|
| `WhatsApp Image 2026-03-16 at 19.10.52.jpeg` | Login | Estado padrao | Logo central grande, dois campos simples, botao azul escuro | Muito espacamento vertical, interface limpa e seca |
| `WhatsApp Image 2026-03-16 at 19.10.52 (1).jpeg` | Unidades | Lista carregada | Top bar azul escura, menu hamburguer, lista textual densa | Sem cards; linhas simples com divisores leves |
| `WhatsApp Image 2026-03-16 at 19.10.52 (2).jpeg` | Inventario | Lista carregada | Cards retangulares claros, titulos fortes, varios metadados por item | Um item aparece selecionado com cinza mais escuro |
| `WhatsApp Image 2026-03-16 at 19.10.52 (3).jpeg` | Drawer lateral sobre Inventario | Menu aberto | Drawer claro, icones lineares, item selecionado em cinza, versao no rodape | Overlay simples, sem blur sofisticado |
| `WhatsApp Image 2026-03-16 at 19.10.52 (4).jpeg` | Produtos | Lista de produtos | Busca no topo com campo + botao, lista com nome e codigo abaixo | Layout denso, sem chips, sem filtros visuais modernos |
| `WhatsApp Image 2026-03-16 at 19.10.52 (5).jpeg` | Busca Global | Estado vazio sem seletor aberto | Barra de acoes no topo do conteudo, contadores Lidas e Encontradas, mensagem vazia, botoes de targets | Barra de contadores usa preto para lidas e verde para encontradas |
| `WhatsApp Image 2026-03-16 at 19.10.53.jpeg` | Busca Global | Bottom sheet de tipo aberto | Mesmo topo da busca global + lista inferior com Produto, Reduzido, Ean-13, Tag | O seletor aparece como sheet simples branca ocupando a base da tela |
| `WhatsApp Image 2026-03-16 at 19.10.53 (1).jpeg` | Buscar Global Produto | Carregando | Campos de referencia/cor/tamanho, card de aguarde, botao verde esmaecido no rodape | Estrutura muito enxuta, sem ilustracoes nem loading moderno |
| `WhatsApp Image 2026-03-16 at 19.10.53 (2).jpeg` | Busca Global | Dialogo de codigo reduzido | Modal branco centralizado com input, cancelar textual e salvar azul escuro | Modal bem simples, retangular, com pouco raio |
| `WhatsApp Image 2026-03-16 at 19.10.53 (3).jpeg` | Busca Global | Dialogo de EAN-13 | Mesmo padrao do modal anterior | Variacao apenas no titulo |
| `WhatsApp Image 2026-03-16 at 19.10.53 (4).jpeg` | Busca Global | Dialogo de Tag | Mesmo padrao do modal anterior | Variacao apenas no titulo |
| `WhatsApp Image 2026-03-16 at 19.10.53 (5).jpeg` | Associar Tags | Carregando | Mesmo layout da busca de produto com titulo diferente | Deve reaproveitar a base visual e estrutural da tela de busca |
| `WhatsApp Image 2026-03-16 at 19.10.53 (6).jpeg` | Nota Fiscal | Estado vazio | Card branco com mensagem e botao verde contornado | Muito branco livre abaixo do card |
| `WhatsApp Image 2026-03-16 at 19.10.54.jpeg` | Nota Fiscal Conferencia | Estado vazio | Mesmo padrao da nota fiscal simples com titulo mais longo | Reforca necessidade de top bar suportar titulos maiores |
| `WhatsApp Image 2026-03-16 at 19.10.54 (1).jpeg` | Movimentacao com Conferencia | Estado vazio | Mesmo componente da nota fiscal, com texto da movimentacao | Reaproveitamento quase total do componente de empty state |
| `WhatsApp Image 2026-03-16 at 19.10.54 (2).jpeg` | Movimentacao com Conferencia | Estado vazio (captura repetida) | Mesma estrutura da imagem anterior | Pode ser tratada como captura duplicada do mesmo estado |
| `WhatsApp Image 2026-03-16 at 19.10.54 (3).jpeg` | Configuracoes | Tela principal | Linhas expansiveis, valor a direita, status de dispositivo, toggle e acoes empilhadas | Mistura linhas compactas com botoes largos em fundo claro |
| `WhatsApp Image 2026-03-16 at 19.10.54 (4).jpeg` | Configuracoes | Modal de selecao de leitor | Modal escuro com itens de radio e cantos bem arredondados | Excecao importante: modal escuro, diferente do restante do app |
| `WhatsApp Image 2026-03-16 at 19.10.54 (5).jpeg` | Configuracoes | Modal automatica/manual | Modal escuro com duas opcoes | Mesmo padrao do modal de leitor, mas mais curto |
| `WhatsApp Image 2026-03-16 at 19.10.54 (6).jpeg` | Configuracoes | Dialogo de permissao | Dialogo escuro com texto central e dois botoes empilhados claros | Composicao menos Android puro e mais proxima de um prompt customizado |
| `WhatsApp Image 2026-03-16 at 19.10.55.jpeg` | Dispositivos | Lista simples / sheet de dispositivos | Lista branca simples com titulo e fechar no topo direito | Funciona como sheet ou tela utilitaria de descoberta |
| `WhatsApp Image 2026-03-16 at 19.10.55 (1).jpeg` | Leitura Livre | Estado base de configuracoes | Card "Controles de leitura" e linhas `SET/GET` abaixo | Nesta captura os toggles superiores nao aparecem |
| `WhatsApp Image 2026-03-16 at 19.10.55 (2).jpeg` | Leitura Livre | Estado completo | Mesmo topo da leitura livre + toggles de beep, vibracao, RSSI, latitude/longitude | Melhor referencia para densidade e ritmo das linhas tecnicas |

## Fluxos Principais Identificados

1. Acesso e selecao inicial:
   - Login
   - Unidades
   - Inventario
   - Drawer lateral
2. Consulta e busca:
   - Produtos
   - Busca Global
   - Buscar Global Produto
   - Associar Tags
   - Dialogos de codigo reduzido, EAN-13 e Tag
3. Conferencia operacional:
   - Nota Fiscal
   - Nota Fiscal Conferencia
   - Movimentacao com Conferencia
4. Configuracao e hardware:
   - Configuracoes
   - Selecao de leitor
   - Selecao automatica/manual
   - Permissao
   - Dispositivos
   - Leitura Livre

## Tokens Visuais

### Cores base observadas

- `TopBarBlue`: azul escuro fosco, dominante na barra superior.
- `PrimaryActionBlue`: azul escuro semelhante ao da top bar, usado no botao principal e em salvar.
- `ScreenBackground`: branco ou cinza muito claro.
- `CardSurface`: branco puro para cards e listas.
- `MutedSurface`: cinza claro para item selecionado e alguns grupos.
- `Divider`: cinza claro quase invisivel.
- `TextPrimary`: cinza muito escuro, nao preto puro.
- `TextSecondary`: cinza medio para labels e apoio.
- `PositiveGreen`: verde claro usado em contadores, bordas e botoes positivos.
- `ModalDark`: grafite escuro dos modais de configuracao.
- `DangerSoft`: vermelho muito suave para estados desabilitados ou acoes negativas sem destaque agressivo.

### Tipografia observada

- Titulos de top bar: brancos, medios, sem exagero de peso.
- Titulos de card/lista: semibold ou bold discreto, caixa alta parcial em alguns itens.
- Conteudo secundario: corpo pequeno a medio, bem compacto.
- Rotulos e apoio: cinza claro, pouco contraste.
- Sem variacoes decorativas, sem tipografia de marketing.

### Espacamentos observados

- Inputs e botoes do login usam respiro vertical grande.
- Listas de unidades e produtos usam densidade alta, com pouca margem lateral.
- Cards de inventario usam padding interno moderado e distancia pequena entre cards.
- Dialogos possuem padding enxuto e campos alinhados sem floreio.
- Leitura Livre usa composicao extremamente densa, com repeticao ritmica de linhas.

## Componentes Reutilizaveis

### Componentes obrigatorios para fundacao

- `AppTopBar`
- `ActionButtonPrimary`
- `ActionButtonOutline`
- `SimpleListRow`
- `InventoryCard`
- `EmptyStateBox`

### Componentes que entram na fase 2 ou depois

- `DrawerMenu`
- `SearchHeader`
- `CounterBar`
- `AppDialog`
- `AppBottomSheet`
- `SettingRow`
- `ToggleRow`
- `SetGetRow`

## Regras de Fidelidade

- Priorizar legibilidade operacional, nao beleza de vitrine.
- Manter fundo claro em quase todas as telas.
- Nao usar sombras grandes nem gradientes.
- Evitar cantos muito arredondados; excecao principal sao os modais escuros de configuracao.
- Botoes azuis precisam parecer utilitarios e corporativos.
- Botoes verdes devem parecer acao positiva, mas ainda discretos.
- Listas devem ser densas e rapidas de ler.
- Truncamentos precisam existir onde o legado mostra texto cortado.
- O drawer deve ser seco, claro e funcional.
- Leitura Livre deve preservar densidade tecnica e repeticao visual das linhas de parametro.

## Similaridades e Reaproveitamento

- `Nota Fiscal`, `Nota Fiscal Conferencia` e `Movimentacao com Conferencia` compartilham a mesma estrutura.
- `Buscar Global Produto` e `Associar Tags` compartilham layout principal.
- Os tres dialogos de busca global compartilham o mesmo molde.
- `Configuracoes` concentra tres composicoes especiais: tela clara, modal escuro e dialogo escuro com acoes empilhadas.
- `Leitura Livre` precisa de um grupo especifico de componentes, mas ainda segue a mesma top bar azul e fundo claro.

## Riscos de Interpretacao

- `WhatsApp Image 2026-03-16 at 19.10.54 (2).jpeg` parece ser duplicata funcional de movimentacao.
- Algumas capturas da Busca Global mostram o mesmo estado com pequenas variacoes de overlay; isso exige componentes compostos em camadas.
- O arquivo `pipeline-dev-software.html` ajuda a organizar tokens e consistencia, mas a estetica final nao deve seguir seu visual escuro.

## Prioridade por fase

### Fase 2

- Login
- Unidades
- Inventario
- Drawer lateral
- Tema ja precisa suportar top bar azul, botao azul, estados selecionados e cards de inventario
- Componentes mais urgentes:
  - `AppTopBar`
  - `ActionButtonPrimary`
  - `SimpleListRow`
  - `InventoryCard`

### Fases 3 a 6

- Busca Global e seus dialogos
- Produtos
- Buscar Global Produto
- Associar Tags
- Nota Fiscal e Movimentacao
- Configuracoes
- Dispositivos
- Leitura Livre
- Componentes a amadurecer nessas fases:
  - `CounterBar`
  - `AppDialog`
  - `AppBottomSheet`
  - `SettingRow`
  - `ToggleRow`
  - `SetGetRow`

### Fase 7

- Selecao real de dispositivo
- Permissoes reais de Bluetooth/localizacao
- Integracao com leitor C72 e UR6/R6
- Port de comportamento do demo legado sem reaproveitar a UI XML

## Resultado Pratico

Esta referencia deve ser tratada como a fonte visual de verdade da base legado-corporativa do projeto. O `Prompt.md` define o escopo, mas este arquivo traduz esse escopo em decisoes visuais acionaveis para implementacao.
