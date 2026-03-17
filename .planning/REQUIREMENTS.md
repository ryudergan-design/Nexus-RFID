# Requisitos

## Funcionais
- [ ] **REQ-01**: O aplicativo deve ser Android nativo em Kotlin com Jetpack Compose e Material 3 apenas como base tecnica.
- [ ] **REQ-02**: O projeto deve possuir um tema proprio com `AppColors`, `AppTypography`, `AppSpacing`, `AppShapes` e base de `AppComponents`.
- [ ] **REQ-03**: A interface deve preservar a estrutura operacional do legado, mas aplicar a identidade visual Nexus em variante light, com superficies opacas, azul Nexus como cor principal e baixa ornamentacao visual.
- [ ] **REQ-04**: A tela de Login deve conter logo central, campo de usuario, campo de senha, botao `Entrar` e validacao visual simples para campos obrigatorios.
- [ ] **REQ-05**: A tela de Departamentos deve conter top bar com menu e lista vertical simples em formato denso, substituindo a antiga nomenclatura `Unidades`.
- [ ] **REQ-06**: A tela de Inventario deve exibir cards com titulo, descricao, data/hora, responsavel e quantidade de estoque do sistema.
- [ ] **REQ-07**: O drawer lateral deve conter os itens de menu definidos no prompt e exibir a versao no rodape.
- [ ] **REQ-08**: A tela de Produtos deve conter campo de busca, botao `Buscar` e lista com nome e codigo do produto.
- [ ] **REQ-09**: A tela de Busca Global deve conter acoes de leitura com icones, contadores `Lidas` e `Encontradas`, estado vazio, botoes de targets e seletor do tipo de busca.
- [ ] **REQ-10**: O app deve possuir dialogos para adicionar codigo reduzido, EAN-13 e Tag.
- [ ] **REQ-11**: A tela Buscar Global Produto deve conter campos de referencia, cor e tamanho, estado de carregamento e botao `Selecionar` quando aplicavel.
- [ ] **REQ-12**: A tela Associar Tags deve manter estrutura semelhante a busca de produto, seguindo a referencia visual.
- [ ] **REQ-13**: As telas Nota Fiscal e Movimentacao com Conferencia devem exibir mensagens vazias e acoes para leitura de codigo de barras.
- [ ] **REQ-14**: A tela Configuracoes deve conter secoes, acoes de leitor, modais escuros de selecao e dialogo de permissao proximo ao legado.
- [ ] **REQ-15**: A tela Leitura Livre deve conter controles de leitura, toggles de configuracao, linhas `SET/GET` e opcoes de leitor mostradas nas referencias.
- [ ] **REQ-16**: O projeto deve possuir bottom sheet ou tela simples para listar dispositivos encontrados.
- [ ] **REQ-17**: O app deve oferecer navegacao local simples entre telas, previews Compose e dados mockados organizados.

## Tecnicos
- [ ] **REQ-18**: Nesta primeira etapa, o aplicativo nao deve integrar RFID real, backend real, autenticacao real, camera ou persistencia definitiva.
- [ ] **REQ-19**: O codigo deve ser separado por responsabilidade em `ui/theme`, `ui/components`, `ui/screens`, `ui/navigation`, `data/mock` e futura camada `rfid`.
- [ ] **REQ-20**: A futura integracao real com leitores C72 e UR6/R6 deve usar a pasta `Nexus RFID/RFID R6 and C72/demo-uhf_bluetooth_as` como referencia tecnica de permissoes, fluxo de conexao e comandos.
- [ ] **REQ-21**: A implementacao visual deve ser validada contra as 23 screenshots, preservando densidade, espacamentos, truncamentos e hierarquia visual observada.
