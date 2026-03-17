---
phase: "01"
name: "Fundacao visual e contexto"
created: 2026-03-16
---

# Phase 1: Fundacao visual e contexto - Context

## Goal

Deixar a base pronta para implementar as telas Compose das proximas fases com consistencia visual, estrutural e tecnica.

## Scope

- Mapear as referencias visuais e relacionar screenshots, telas, estados e componentes.
- Estruturar os pacotes base do app para tema, componentes, navegacao, telas e mocks.
- Definir modelos mockados e rotas iniciais de navegacao local.
- Implementar o tema proprio do app e os componentes fundamentais reutilizaveis.
- Preparar a fundacao que permitira implementar Login, Unidades e Inventario na Fase 2 sem retrabalho.

## Decisions

- Seguir o [ROADMAP.md](C:/Users/AMC/AndroidStudioProjects/NexusRFID/.planning/ROADMAP.md) como fonte principal da ordem e do escopo das fases.
- Tratar o `Prompt.md` como referencia geral de produto e interface, sem sobrescrever a sequencia definida no roadmap.
- Manter `Login`, `Unidades` e `Inventario` fora desta fase; essas telas continuam pertencendo a Fase 2.
- Limitar a Fase 1 a fundacao visual e estrutural: tema, tokens, componentes base, navegacao inicial, mocks e mapeamento de referencias.
- Preservar a fidelidade ao legado desde a fundacao: top bar azul escura, fundo claro, densidade alta, listas objetivas e componentes simples.
- Usar as screenshots da pasta `Nexus RFID/Fotos-Refecenia` como referencia visual principal.
- Usar `pipeline-dev-software.html` como referencia de organizacao, tokens, consistencia e componentizacao, sem modernizar a aparencia final do app.
- Manter fora de escopo nesta fase qualquer integracao real com RFID, backend, autenticacao, camera, codigo de barras real ou persistencia definitiva.

## Discretion Areas

- Nome exato dos arquivos, classes e subpastas, desde que a separacao por responsabilidade fique clara.
- Estrategia inicial de navegacao local, desde que seja simples e pronta para crescer nas proximas fases.
- Quantidade inicial de componentes base a serem entregues nesta fase, desde que cubram a fundacao necessaria para as telas da Fase 2.
- Formato dos mocks iniciais e dos modelos de dados de interface.
- Nivel de detalhe do mapeamento interno das screenshots, desde que facilite a implementacao futura.

## References

- `Nexus RFID/Prompt.md`
- `Nexus RFID/Fotos-Refecenia/`
- `Nexus RFID/pipeline-dev-software.html`
- `.planning/PROJECT.md`
- `.planning/REQUIREMENTS.md`
- `.planning/ROADMAP.md`
- `Nexus RFID/RFID R6 and C72/demo-uhf_bluetooth_as` como referencia tecnica futura, nao como item de execucao desta fase

## Deferred Ideas

- Implementacao das telas Login, Unidades, Inventario e drawer lateral na Fase 2.
- Componentes e modais mais especificos das telas de busca, configuracao e leitura livre nas fases seguintes.
- Adaptacao do demo legado `cw-deviceapi.jar` e das bibliotecas nativas para a futura camada `rfid`.
