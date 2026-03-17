# Registro Visual

## Objetivo

Guardar o historico visual do app para comparar o estado inicial com cada mudanca de interface feita ao longo do projeto.

## Estrutura

- `00-registro-inicial-todas-as-telas/`
  - Guarda o registro completo das telas atuais.
  - Regra: um arquivo por tela.
  - Regra: o nome do arquivo deve ser o nome da tela.

- `_modelo-mudanca-visual/`
  - Modelo para as proximas alteracoes visuais.
  - Cada nova mudanca deve virar uma copia desta estrutura.

## Padrao para as proximas mudancas

Quando houver uma nova alteracao visual, criar uma pasta assim:

- `01-ajuste-login-e-inventario/`
- `02-refino-drawer-e-topbar/`
- `03-ajuste-listas-e-cards/`

Dentro de cada pasta de mudanca:

- `antes/`
- `depois/`

## Regra de quantidade de imagens

- Se mudou 1 tela: salvar 2 imagens.
- Se mudou 2 telas: salvar 4 imagens.
- Salvar apenas as telas afetadas naquela mudanca.

## Regra de nomes

Usar nomes curtos e consistentes:

- `login.png`
- `departamentos.png`
- `inventario.png`
- `drawer-departamentos.png`
- `drawer-inventario.png`

Se surgirem novas telas, seguir o mesmo padrao:

- `produtos.png`
- `busca-global.png`
- `associar-tag.png`

## Fluxo recomendado

1. Antes de mexer no visual, salvar as imagens atuais em `antes/`.
2. Aplicar a mudanca visual.
3. Salvar as novas imagens em `depois/`.
4. Nomear a pasta pela mudanca feita.

## Observacao

Hoje, em `16/03/2026`, as capturas iniciais foram geradas usando o fluxo oficial de screenshot por preview do Compose.
