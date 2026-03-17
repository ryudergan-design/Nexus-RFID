Quero que você atue como um desenvolvedor Android sênior com foco em reprodução fiel de interface.



Objetivo:

Criar um aplicativo Android nativo para leitura RFID, mas nesta primeira etapa o foco é apenas recriar com máxima fidelidade visual as telas do sistema de referência a partir das imagens enviadas. Ainda NÃO quero integrar RFID real, APIs, banco, autenticação real ou regras complexas. Quero primeiro replicar as telas, navegação básica e componentes visuais.



Referências disponíveis:

1\. Um conjunto de screenshots de um app Android legado de RFID/estoque.

2\. Um arquivo HTML de base que deve ser usado como referência de organização visual, consistência de tokens, hierarquia, espaçamento, componentização, nomenclatura e estrutura geral de design system.

Importante: o HTML é mais moderno e escuro, mas as screenshots do app são claras e corporativas. Portanto:

\- a aparência final deve seguir as screenshots do app

\- o HTML deve influenciar a organização do código, os tokens, a consistência e a qualidade estrutural

\- NÃO modernize demais a interface

\- NÃO transforme o app em algo futurista

\- preserve o estilo de sistema corporativo Android simples e funcional



Stack desejada:

\- Kotlin

\- Jetpack Compose

\- Material 3 apenas como base técnica

\- Criar tema próprio para aproximar o visual das screenshots

\- Dados mockados

\- Navegação local simples

\- Sem backend real nesta etapa

\- Sem integração RFID real nesta etapa



Meta principal:

Replicar a interface o mais próximo possível das imagens:

\- layout

\- espaçamentos

\- barra superior

\- listas

\- cards

\- botões

\- modais

\- bottom sheets

\- campos

\- textos

\- ícones

\- hierarquia visual

\- estados vazios

\- cores principais

\- sensação de app legado corporativo



Regras visuais obrigatórias:

1\. Priorizar fidelidade às screenshots acima de “embelezamento”.

2\. Manter o aspecto de app empresarial simples.

3\. Usar fundo claro/cinza muito claro.

4\. Usar barra superior azul escura.

5\. Usar botões principais em azul escuro.

6\. Usar ações positivas em verde claro/verde contornado quando aparecer nas referências.

7\. Manter cards retangulares simples, com bordas discretas e pouca ou nenhuma sombra forte.

8\. Manter listas densas, lineares e objetivas.

9\. Usar tipografia limpa, sem exagero de peso ou contraste.

10\. Não inventar animações sofisticadas.

11\. Não arredondar demais os componentes.

12\. Não criar dashboard moderno.

13\. Não alterar os textos das telas; manter tudo em português.

14\. Quando houver texto truncado nas imagens, preserve o comportamento de truncamento visual.

15\. Onde a imagem mostrar interface mais “seca” e direta, siga isso.



Direção visual prática:

\- App Android em retrato

\- largura pensada para smartphone comum

\- top app bar azul escura com título branco

\- menu hamburguer à esquerda quando aplicável

\- fundo geral claro

\- divisores finos em listas

\- cards de inventário com fundo branco ou cinza muito claro

\- item selecionado pode usar cinza um pouco mais forte

\- campos simples

\- modais e bottom sheets com aparência Android clássica

\- textos de apoio em cinza

\- botões com aparência corporativa



Crie um pequeno design system interno baseado nas referências:

\- AppColors

\- AppTypography

\- AppSpacing

\- AppShapes

\- AppComponents



Mas esse design system deve servir para reproduzir o app legado, não para reinventá-lo.



Telas a recriar com dados mockados:

1\. Login

&#x20;  - logo central

&#x20;  - campo usuário

&#x20;  - campo senha

&#x20;  - botão Entrar



2\. Unidades

&#x20;  - top bar com menu

&#x20;  - lista simples de unidades/empresas

&#x20;  - itens em lista vertical



3\. Inventário

&#x20;  - top bar com menu

&#x20;  - lista de cards

&#x20;  - cada card com:

&#x20;    - título em destaque

&#x20;    - descrição

&#x20;    - data/hora

&#x20;    - responsável

&#x20;    - quantidade de estoque sistema

&#x20;  - manter aparência bem próxima da referência



4\. Menu lateral / drawer

&#x20;  - abrir sobre a tela de inventário

&#x20;  - itens:

&#x20;    - Inventários

&#x20;    - Produtos

&#x20;    - Buscar Produtos

&#x20;    - Associar Tag

&#x20;    - Conferência de Nota Fiscal

&#x20;    - Conferência de nota fiscal com envio de dados

&#x20;    - Conferência de Movimentação Estoque com envio de dados

&#x20;    - Configuração

&#x20;    - Sair

&#x20;  - mostrar versão no rodapé



5\. Produtos

&#x20;  - campo de busca no topo

&#x20;  - botão Buscar

&#x20;  - lista de produtos com nome e código abaixo



6\. Busca Global

&#x20;  - barra superior

&#x20;  - ações de leitura com ícones

&#x20;  - contadores “Lidas” e “Encontradas”

&#x20;  - mensagem de estado vazio

&#x20;  - botões:

&#x20;    - Adicionar Targets

&#x20;    - Remover Targets

&#x20;  - parte inferior com seleção do tipo de busca:

&#x20;    - Produto

&#x20;    - Reduzido

&#x20;    - Ean-13

&#x20;    - Tag



7\. Modal / diálogo de adicionar código reduzido



8\. Modal / diálogo de adicionar EAN-13



9\. Modal / diálogo de adicionar Tag



10\. Buscar Global Produto

&#x20;  - campos como referência, cor, tamanho

&#x20;  - botão Buscar

&#x20;  - estado de carregamento/aguarde

&#x20;  - botão Selecionar no rodapé quando existir



11\. Associar Tags

&#x20;  - tela parecida com busca de produto

&#x20;  - preservar estrutura da referência



12\. Nota Fiscal

&#x20;  - mensagem “Nenhuma nota fiscal identificada”

&#x20;  - ação para ler código de barras da NF



13\. Movimentação com Conferência

&#x20;  - mesma ideia da nota fiscal

&#x20;  - ação para ler código de barras de movimentação



14\. Configurações

&#x20;  - seções:

&#x20;    - Leitor

&#x20;    - Configuração

&#x20;    - Dispositivo padrão

&#x20;    - Show change config inventário

&#x20;  - ações:

&#x20;    - Configuração de Leitura

&#x20;    - Conectar Dispositivo

&#x20;    - Desconectar Dispositivo

&#x20;    - Percentual bateria

&#x20;    - Enviroment

&#x20;  - manter layout bem semelhante à imagem



15\. Modal de seleção de leitor/dispositivo

&#x20;  - lista de opções

&#x20;  - visual escuro no modal conforme referência



16\. Modal de configuração automática/manual

&#x20;  - visual escuro no modal

&#x20;  - opção selecionável



17\. Diálogo de permissão de localização/dispositivo

&#x20;  - manter composição próxima à referência



18\. Leitura Livre

&#x20;  - card “Controles de leitura”

&#x20;  - ações:

&#x20;    - Iniciar

&#x20;    - Parar

&#x20;    - Limpar

&#x20;  - seção “Configurações do leitor”

&#x20;  - opções:

&#x20;    - Beep

&#x20;    - Vibração

&#x20;    - RSSI

&#x20;    - Latitude / Longitude

&#x20;  - linhas com ações SET / GET

&#x20;  - opções como Focus, Link Frequency, Tx Power, Session, Target, Query(q), Tx Cycle On, Tx Cycle Off



19\. Bottom sheet / tela de dispositivos

&#x20;  - listar dispositivos encontrados

&#x20;  - manter aspecto simples



Escopo técnico desta etapa:

\- Criar apenas interface e navegação mockada

\- Todas as listas podem usar dados fictícios baseados no padrão visual das imagens

\- Não conectar em hardware

\- Não usar câmera

\- Não usar RFID real

\- Não implementar login real

\- Não implementar persistência real

\- Não inventar fluxo novo fora das telas mostradas



Entregáveis esperados:

1\. Estrutura do projeto Android

2\. Theme com tokens próprios

3\. Componentes reutilizáveis

4\. Todas as telas acima em Compose

5\. Navegação básica entre telas

6\. Previews das telas

7\. Dados mockados organizados

8\. README curto explicando a estrutura

9\. Código limpo e separado por responsabilidade



Organização sugerida:

\- ui/theme

\- ui/components

\- ui/screens/login

\- ui/screens/unidades

\- ui/screens/inventario

\- ui/screens/produtos

\- ui/screens/busca\_global

\- ui/screens/associar\_tag

\- ui/screens/nota\_fiscal

\- ui/screens/movimentacao

\- ui/screens/configuracoes

\- ui/screens/leitura\_livre

\- ui/navigation

\- data/mock



Componentes reutilizáveis esperados:

\- AppTopBar

\- DrawerMenu

\- InventoryCard

\- SimpleListRow

\- SearchHeader

\- CounterBar

\- ActionButtonPrimary

\- ActionButtonOutline

\- EmptyStateBox

\- AppDialog

\- AppBottomSheet

\- SettingRow

\- ToggleRow

\- SetGetRow



Instruções de execução:

1\. Primeiro, faça um mapeamento das telas e componentes com base nas imagens.

2\. Depois, proponha a estrutura do projeto.

3\. Em seguida, gere o tema e os componentes base.

4\. Depois, implemente as telas uma por uma.

5\. No final, entregue a navegação e os mocks.

6\. Sempre explique rapidamente qual tela está sendo implementada.

7\. Sempre prefira semelhança visual à criatividade.



Restrições importantes:

\- Não usar visual genérico de app moderno de startup.

\- Não trocar a linguagem do app.

\- Não resumir demais as telas.

\- Não criar placeholders gigantes sem estrutura.

\- Não simplificar componentes que aparecem nas imagens.

\- Não ignorar modais, bottom sheets e estados vazios.

\- Não alterar a barra superior azul escura.

\- Não trocar a lógica visual dos botões verdes e azuis.

\- Não remover densidade da interface.



Quero que você comece por:

1\. mapear todas as telas identificadas

2\. listar os componentes reutilizáveis

3\. propor a estrutura inicial do projeto

4\. gerar o tema base

5\. implementar a tela de Login, Unidades e Inventário primeiro

