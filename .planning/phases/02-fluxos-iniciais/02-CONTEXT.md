---
phase: "02"
name: "Fluxos iniciais"
created: 2026-03-16
---

# Phase 2: Fluxos iniciais - Context

## Goal

Entregar o primeiro fluxo real do app legado em Compose, com entrada por Login, acesso a Departamentos, abertura do drawer lateral e chegada ao Inventario com dados mockados.

## Scope

- Trocar a entrada principal do app para `Login`, removendo a tela tecnica `Foundation` do fluxo normal de uso.
- Implementar a tela de `Login` com logo central, campo de usuario, campo de senha, botao `Entrar` e validacao visual simples para campos obrigatorios.
- Navegar de `Login` para `Departamentos` quando usuario e senha estiverem preenchidos.
- Tratar a antiga tela `Unidades` como `Departamentos`, mantendo a referencia visual de lista densa com top bar e menu.
- Implementar a tela `Departamentos` com lista textual simples e drawer acessivel pelo menu hamburguer.
- Implementar a tela `Inventario` com cards mockados, mantendo densidade visual e item selecionado quando aplicavel.
- Implementar o drawer lateral compartilhado entre `Departamentos` e `Inventario`, com os itens do prompt e versao no rodape.
- Manter os demais destinos fora do escopo visual detalhado desta fase, podendo continuar como placeholders de navegacao local.

## Decisions

- `Login` passa a ser a tela inicial real do app nesta fase.
- Nao deve existir tela tecnica intermediaria antes do login no fluxo normal do usuario.
- O toque em `Entrar` deve exigir preenchimento de usuario e senha; a validacao pode ser local e apenas visual.
- A tela antes nomeada `Unidades` passa a ser implementada como `Departamentos` a partir desta fase.
- `Departamentos` fica imediatamente apos o login no fluxo principal.
- O drawer deve estar disponivel tanto em `Departamentos` quanto em `Inventario`.
- O drawer deve preservar os itens definidos no `Prompt.md` e exibir a versao no rodape.
- Itens do drawer que ainda pertencem a fases futuras podem navegar para rotas placeholder ja existentes, desde que o menu esteja montado corretamente.
- Os dados continuam totalmente mockados; nao ha autenticacao real, sessao real, backend, persistencia ou hardware nesta fase.
- Renomeacoes tecnicas provisorias herdadas da Fase 1, como `Units` e `units`, podem ser corrigidas durante a execucao desta fase.

## Discretion Areas

- Forma exata da validacao visual do login, desde que os dois campos sejam obrigatorios e a tela continue fiel a referencia.
- Estrategia de estado do drawer, desde que a experiencia visual e a navegacao fiquem coerentes nas duas telas.
- Ajustes finos de nomenclatura e texto em `Departamentos`, desde que preservem leitura objetiva e densidade de lista.
- Decisao sobre manter a rota `Foundation` apenas para uso interno de desenvolvimento, desde que ela nao seja a entrada principal do app.

## References

- `Nexus RFID/Prompt.md`
- `.planning/PROJECT.md`
- `.planning/REQUIREMENTS.md`
- `.planning/ROADMAP.md`
- `.planning/phases/01-fundacao-visual-e-contexto/01-CONTEXT.md`
- `.planning/phases/01-fundacao-visual-e-contexto/01-REFERENCE-MAP.md`

## Deferred Ideas

- Navegacao final dos itens do menu que pertencem as fases 3 a 7.
- Autenticacao real, perfil de usuario, sessao persistida ou lembranca de credenciais.
- Busca real, filtros avancados, RFID real, codigo de barras real ou persistencia definitiva.
