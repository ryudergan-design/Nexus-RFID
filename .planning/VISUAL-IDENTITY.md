# Nexus RFID - Visual Identity

## Objetivo

Documentar a nova direcao visual do Nexus RFID para que o projeto mantenha a estrutura logica do legado, mas adote uma identidade propria da familia Nexus.

## Papel deste arquivo

- Este arquivo define a pele visual atual do produto.
- O legado continua sendo fonte de estrutura, fluxo, densidade e hierarquia operacional.
- O `Nexus Service Desk` passa a ser a referencia de linguagem visual e atmosfera da marca.

## Fontes de referencia

- `Nexus RFID/Fotos-Refecenia/`
- `Nexus Service Desk/mobile/login/2026-03-15-mobile-app/after.png`
- `Nexus Service Desk/mobile/dashboard/2026-03-15-mobile-app/after.png`
- `Nexus Service Desk/mobile/chamados-central/2026-03-15-mobile-app/after.png`
- `Nexus Service Desk/mobile/home-publica/2026-03-15-mobile-app/after.png`

## Decisao central

O Nexus RFID nao vai mais copiar literalmente a estetica do app legado. A partir desta definicao:

- a logica das telas continua vindo das screenshots do RFID legado;
- a identidade visual passa a seguir a familia Nexus;
- o tema do RFID deve ser uma variante light do universo visual mais dark usado no Nexus Service Desk;
- o RFID deve aparecer de forma sutil, limpa e opaca, sem cair em visual gamer, futurista ou neon.

## O que permanece do legado

- Fluxo das telas
- Ordem de informacao
- Densidade operacional
- Hierarquia dos blocos
- Presenca de listas, cards, drawer e top bars
- Clareza direta de uso em contexto operacional

## O que muda na linguagem visual

- A marca deixa de usar a nuvem anterior e passa a usar assinatura Nexus RFID.
- O azul principal sai do tom corporativo antigo e entra na familia azul Nexus.
- Os fundos continuam claros, mas com base azulada bem suave em vez de branco neutro seco.
- Campos, cards e botoes ficam mais limpos e opacos, com bordas suaves e menos cara de sistema legado puro.
- O RFID entra apenas como detalhe de sinal, leitura ou pulso, nunca como ilustracao exagerada.

## Principios visuais

### 1. Light by default

- Fundo geral claro.
- Superficies brancas ou azuladas muito suaves.
- Contraste alto para leitura, mas sem preto absoluto.

### 2. Clean e opaco

- Nada de gradientes fortes.
- Nada de vidro, glow ou transparencia chamativa.
- Sombras leves ou quase inexistentes.

### 3. Nexus, nao startup generica

- Azul com carater tecnico, nao infantil.
- Tipografia firme e legivel.
- Visual de plataforma operacional madura.

### 4. RFID sutil

- Ondas, pulsos ou sinais devem ser discretos.
- O simbolo de RFID serve como assinatura de produto, nao como decoracao dominante.

## Sistema visual proposto

### Paleta base

- `TopBarBlue`: `#294765`
- `PrimaryActionBlue`: `#4F86E8`
- `BrandSignalBlue`: `#6CC6FF`
- `ScreenBackground`: `#F2F6FB`
- `CardSurface`: `#FFFFFF`
- `MutedSurface`: `#E7EEF7`
- `Divider`: `#D7E1EC`
- `TextPrimary`: `#14233A`
- `TextSecondary`: `#6B7C90`
- `FieldBackground`: `#F7FAFD`
- `DarkModal`: `#223046`

### Formas

- Inputs: quadrados
- Cards: quadrados
- Botoes: quadrados
- Modais: quadrados

### Tipografia

- Titulos principais com peso firme
- Labels de acao com leve espacamento entre letras
- Corpo com leitura objetiva e alta legibilidade

## Conceito de logo

### Nome do conceito

`Nexus Pulse`

### Estrutura

- Monograma `N` como base principal
- Bloco solido e opaco
- Pequeno pulso de sinal no canto superior direito

### Leitura simbolica

- `N`: Nexus
- Pulso: RFID, leitura, conexao, captura
- Bloco: estabilidade operacional

### Regra de uso

- O pulso deve ser sutil
- A marca deve funcionar bem em tela clara
- A wordmark `Nexus RFID` deve acompanhar a marca quando houver espaco

## Traducao para os componentes do app

### Login

- Deve usar a nova marca Nexus RFID no topo
- Campos com base clara azulada e borda suave
- Botao principal em azul Nexus

### Cards de introducao

- Cada tela principal pode abrir com um card de contexto compacto.
- Esse card deve combinar texto operacional com um resumo numerico da sessao.
- A marca aparece como apoio visual, nao como elemento dominante.

### Top bar

- Continua escura para sustentar contraste e navegacao
- Azul mais alinhado ao universo Nexus
- Deve trazer uma pequena assinatura Nexus RFID acima do titulo principal

### Drawer

- Continua claro e funcional
- Fundo opaco e selecao suave
- Sem efeitos pesados
- O topo do drawer pode carregar um bloco escuro com status da sessao e versao
- O item selecionado deve ficar evidente com destaque suave, nunca com glow
- O menu inicial deve priorizar: `Buscar Produtos`, `Produtos`, `Conferencias`, `Configuracoes` e `Sair`
- `Conferencias` deve expandir `Nota Fiscal` e `Movimentacao de Estoque`
- `Associar Tag` sai do menu inicial

### Cards de inventario

- Continuam densos
- Ficam com cantos mais suaves
- Borda leve em vez de sombra forte
- Devem agrupar metadados em superfice interna clara para leitura rapida

### Lista de departamentos

- A lista continua direta e operacional
- Cada entrada deve parecer um cartao leve e tocavel
- Codigo do departamento entra como selo pequeno e discreto

### Telas futuras

- Produtos e Busca Global devem usar a mesma base light
- Dialogos e bottom sheets devem parecer parte da familia Nexus, nao do app legado bruto
- Modais escuros continuam permitidos quando fizer sentido tecnico, mas alinhados ao azul-grafite Nexus

## O que evitar

- Roxo como cor principal
- Preto puro como fundo dominante
- Glow azul
- Visual cyberpunk
- Elementos futuristas demais
- RFID literal demais, como antenas gigantes, chips desenhados ou wireframes decorativos

## Regra pratica para as proximas fases

Sempre que houver conflito entre:

1. estrutura logica das screenshots antigas; e
2. visual da familia Nexus

use esta regra:

- manter a estrutura, a ordem e a densidade do legado;
- aplicar a superficie, a paleta e a identidade do Nexus.

## Resultado esperado

O app deve parecer parte do ecossistema Nexus, mas sem perder a objetividade operacional do produto RFID.

## Aplicacao inicial ja implementada

- `Login`: faixa tonal superior clara, selo `Plataforma Nexus`, marca `Nexus RFID` e card de acesso com borda suave.
- `Departamentos`: card de contexto da sessao, lista em superficie unica e entradas com codigo em selo.
- `Inventario`: card de contexto, resumo de atualizacao e cartoes com acento lateral e metadados agrupados.
- `Drawer`: cabecalho com status da sessao, versao e itens com texto de apoio.
- `Top bar`: agora usa assinatura Nexus RFID acima do titulo da tela.
