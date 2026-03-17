---
phase: "03"
name: "Catalogo e busca global"
created: 2026-03-16
---

# Phase 3: Catalogo e busca global - Context

## Goal

Entregar o fluxo de consulta de produtos e o fluxo operacional de `Buscar Produtos`, mantendo a estrutura visual do legado e preparando a base para futuras consultas RFID com dados reais.

## Scope

- Implementar a tela `Produtos` como uma tela de consulta simples, com pesquisa no topo e lista de produtos abaixo.
- Implementar a entrada de menu `Buscar Produtos` como a evolucao visual da antiga `Busca Global`, mantendo contadores, targets e seletor de tipo de busca.
- Implementar na tela `Buscar Produtos` os controles visuais de leitura e busca vistos na referencia, incluindo `Lidas`, `Encontradas`, acoes superiores e estado vazio.
- Implementar o seletor de tipo com as opcoes `Produto`, `Reduzido`, `EAN-13` e `Tag`.
- Implementar os dialogos de entrada para `Reduzido`, `EAN-13` e `Tag`.
- Manter a navegacao local e os dados mockados, sem integrar RFID real, backend real, codigo de barras real ou persistencia.
- Reduzir textos tecnicos de bastidor da interface para que o APK fique mais proximo de um produto final e menos de uma base tecnica.

## Decisions

- O item de menu que o usuario ve deve se chamar `Buscar Produtos`.
- `Buscar Produtos` deve manter contadores, targets e seletor de tipo de busca, porque esse fluxo sera a base futura para localizar pecas RFID.
- A tela `Produtos` deve ser objetiva: campo de pesquisa em cima e produtos encontrados em baixo.
- O tipo `Produto` dentro de `Buscar Produtos` deve servir como consulta simples de produto nesta fase, sem puxar ainda a busca detalhada de referencia, cor e tamanho da Fase 4.
- A busca detalhada de produto com campos mais especificos continua fora do escopo desta fase.
- A copy da interface deve evitar termos internos como quantidade de fluxos, atalhos prontos, resumos tecnicos de sessao e textos muito voltados a implementacao.
- O visual quadrado aprovado nas ultimas alteracoes continua valendo para `Produtos`, `Buscar Produtos` e dialogos desta fase.

## Discretion Areas

- Forma exata de ligar `Buscar Produtos` ao modo `Produto`, desde que o resultado fique simples e coerente com a consulta de itens desta fase.
- Organizacao dos componentes de busca e dos contadores, desde que a densidade e a hierarquia visual continuem proximas da referencia.
- Texto final de apoio nas telas, desde que seja curto, operacional e nao soe tecnico demais.
- Estrategia de reuso entre `Produtos`, `Buscar Produtos` e os dialogos, desde que reduza retrabalho para a Fase 4.

## References

- `Nexus RFID/Prompt.md`
- `.planning/PROJECT.md`
- `.planning/REQUIREMENTS.md`
- `.planning/ROADMAP.md`
- `.planning/VISUAL-IDENTITY.md`
- `.planning/phases/01-fundacao-visual-e-contexto/01-REFERENCE-MAP.md`
- `.planning/phases/02-fluxos-iniciais/02-CONTEXT.md`

## Deferred Ideas

- Busca detalhada de produto com referencia, cor e tamanho.
- Associacao de tags.
- RFID real, leitura real e consulta por dados vindos de API.
- Regras de negocio definitivas para filtros, contadores e targets.
