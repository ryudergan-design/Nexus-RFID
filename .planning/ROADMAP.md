# Roadmap: Nexus RFID

## Overview

O projeto sera entregue em camadas. Primeiro sera criada uma base Android Compose capaz de reproduzir fielmente o app legado com navegacao e mocks. Em seguida, as telas operacionais restantes serao concluidas e refinadas. So depois dessa estabilidade visual a integracao real com os leitores C72 e UR6/R6 entrara em cena, isolada em uma camada propria para reduzir risco tecnico.

## Phases

**Phase Numbering:**
- Integer phases (1, 2, 3): Planned milestone work
- Decimal phases (2.1, 2.2): Urgent insertions (marked with INSERTED)

Decimal phases appear between their surrounding integers in numeric order.

- [x] **Phase 1: Fundacao visual e contexto** - Consolidar referencias, estrutura do projeto, tokens e base de navegacao.
- [x] **Phase 2: Fluxos iniciais** - Implementar Login, Departamentos, Inventario e drawer lateral.
- [ ] **Phase 3: Catalogo e busca global** - Implementar Produtos, Busca Global e dialogos de entrada.
- [ ] **Phase 4: Busca de produto e associacao** - Entregar Buscar Global Produto e Associar Tags.
- [ ] **Phase 5: Fluxos fiscais e configuracoes** - Implementar Nota Fiscal, Movimentacao e Configuracoes com seus modais.
- [ ] **Phase 6: Leitura Livre e acabamento visual** - Finalizar Leitura Livre, lista de dispositivos, previews e revisao de fidelidade.
- [ ] **Phase 7: Integracao RFID real C72 e UR6/R6** - Preparar adaptadores e conectar os fluxos reais do leitor com base no demo legado.

## Phase Details

### Phase 1: Fundacao visual e contexto
**Goal**: Deixar a base pronta para implementar telas Compose com consistencia visual e tecnica.
**Depends on**: Nothing (first phase)
**Requirements**: [REQ-01, REQ-02, REQ-03, REQ-19, REQ-21]
**Success Criteria** (what must be TRUE):
1. A estrutura do projeto separa tema, componentes, navegacao, telas e mocks.
2. O tema base reproduz a linguagem visual corporativa do legado.
3. A navegacao inicial e os dados mockados permitem evoluir as telas sem depender de backend ou hardware.
**Plans**: 3 plans

Plans:
- [x] 01-01: Consolidar mapeamento visual de telas, componentes e referencias.
- [x] 01-02: Estruturar pacotes base, modelos mockados e rotas de navegacao.
- [x] 01-03: Implementar tema, tokens e componentes fundamentais reutilizaveis.

### Phase 2: Fluxos iniciais
**Goal**: Entregar a entrada do sistema e os fluxos principais de selecao de departamento e inventario.
**Depends on**: Phase 1
**Requirements**: [REQ-04, REQ-05, REQ-06, REQ-07, REQ-17]
**Success Criteria** (what must be TRUE):
1. Usuario consegue abrir Login como entrada principal, validar campos obrigatorios e seguir para Departamentos, Inventario e drawer usando navegacao local.
2. Login, lista de departamentos e cards de inventario ficam visualmente proximos das screenshots.
3. O drawer mostra os itens esperados e a versao no rodape.
**Plans**: 3 plans

Plans:
- [x] 02-01: Implementar tela de Login com validacao visual simples e tema aplicado.
- [x] 02-02: Implementar Departamentos e Inventario com listas e cards mockados.
- [x] 02-03: Implementar top bar, drawer e ligacao basica entre as rotas.

### Phase 3: Catalogo e busca global
**Goal**: Construir o fluxo de produtos e busca global com estados visuais completos.
**Depends on**: Phase 2
**Requirements**: [REQ-08, REQ-09, REQ-10, REQ-17, REQ-21]
**Success Criteria** (what must be TRUE):
1. Tela de Produtos permite busca mockada e lista densa de itens.
2. Busca Global mostra controles, contadores, estado vazio e tipo de busca.
3. Os dialogos de codigo reduzido, EAN-13 e Tag seguem o comportamento visual esperado.
**Plans**: 3 plans

Plans:
- [ ] 03-01: Implementar tela de Produtos e seus componentes de busca.
- [ ] 03-02: Implementar tela de Busca Global com contadores e acoes.
- [ ] 03-03: Implementar dialogos de entrada para reduzido, EAN-13 e Tag.

### Phase 4: Busca de produto e associacao
**Goal**: Finalizar os fluxos de busca detalhada e associacao de tags.
**Depends on**: Phase 3
**Requirements**: [REQ-11, REQ-12, REQ-17, REQ-21]
**Success Criteria** (what must be TRUE):
1. Buscar Global Produto apresenta campos, carregamento e acao de selecao no rodape.
2. Associar Tags reaproveita a base visual da busca sem quebrar a fidelidade.
3. Os dados mockados suportam os cenarios de lista vazia, carregando e item selecionavel.
**Plans**: 2 plans

Plans:
- [ ] 04-01: Implementar Buscar Global Produto com seus estados visuais.
- [ ] 04-02: Implementar Associar Tags reaproveitando componentes comuns.

### Phase 5: Fluxos fiscais e configuracoes
**Goal**: Recriar as telas fiscais e de configuracao, incluindo modais e permissoes.
**Depends on**: Phase 4
**Requirements**: [REQ-13, REQ-14, REQ-17, REQ-21]
**Success Criteria** (what must be TRUE):
1. Nota Fiscal e Movimentacao exibem mensagem vazia e acoes coerentes com a referencia.
2. Configuracoes apresenta secoes, opcoes do leitor e modais escuros de selecao.
3. O dialogo de permissao de dispositivo ou localizacao fica proximo da composicao original.
**Plans**: 3 plans

Plans:
- [ ] 05-01: Implementar Nota Fiscal e Movimentacao com seus estados vazios.
- [ ] 05-02: Implementar tela Configuracoes com linhas, secoes e status.
- [ ] 05-03: Implementar modal de leitor, modal automatico/manual e dialogo de permissao.

### Phase 6: Leitura Livre e acabamento visual
**Goal**: Entregar a tela mais tecnica do app e concluir a revisao visual da fase mockada.
**Depends on**: Phase 5
**Requirements**: [REQ-15, REQ-16, REQ-17, REQ-18, REQ-21]
**Success Criteria** (what must be TRUE):
1. Leitura Livre mostra controles, toggles e linhas `SET/GET` em layout proximo ao legado.
2. A lista ou bottom sheet de dispositivos esta implementada com visual simples.
3. Todas as telas mockadas possuem previews e passam por revisao de fidelidade visual.
**Plans**: 3 plans

Plans:
- [ ] 06-01: Implementar tela Leitura Livre e componentes de configuracao do leitor.
- [ ] 06-02: Implementar bottom sheet ou tela de dispositivos encontrados.
- [ ] 06-03: Revisar previews, estados e ajustes finos de fidelidade nas telas mockadas.

### Phase 7: Integracao RFID real C72 e UR6/R6
**Goal**: Conectar a interface Compose ao hardware real, sem perder a separacao entre UI e SDK legado.
**Depends on**: Phase 6
**Requirements**: [REQ-19, REQ-20]
**Success Criteria** (what must be TRUE):
1. A camada `rfid` encapsula permissao, descoberta, conexao e comandos do leitor.
2. Os dispositivos C72 e UR6/R6 podem ser selecionados e conectados pelo app.
3. Os fluxos reais de leitura e configuracao reaproveitam a UI existente com minima adaptacao.
**Plans**: 3 plans

Plans:
- [ ] 07-01: Mapear e portar a base tecnica do demo legado para adaptadores modernos.
- [ ] 07-02: Implementar gerenciamento de permissao, descoberta e conexao de dispositivo.
- [ ] 07-03: Integrar leitura, configuracao e validacao em aparelho real.

## Progress

**Execution Order:**
Phases execute in numeric order: 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7

| Phase | Plans Complete | Status | Completed |
|-------|----------------|--------|-----------|
| 1. Fundacao visual e contexto | 3/3 | Complete | 2026-03-16 |
| 2. Fluxos iniciais | 3/3 | Complete | 2026-03-16 |
| 3. Catalogo e busca global | 0/3 | Not started | - |
| 4. Busca de produto e associacao | 0/2 | Not started | - |
| 5. Fluxos fiscais e configuracoes | 0/3 | Not started | - |
| 6. Leitura Livre e acabamento visual | 0/3 | Not started | - |
| 7. Integracao RFID real C72 e UR6/R6 | 0/3 | Not started | - |
