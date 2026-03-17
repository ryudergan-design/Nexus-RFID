package com.example.nexusrfid.ui.navigation

enum class AppDestination(
    val route: String,
    val title: String,
    val summary: String
) {
    Login(
        route = "login",
        title = "Login",
        summary = "Acesso inicial com usuario, senha e entrada no app."
    ),
    Departments(
        route = "departments",
        title = "Departamentos",
        summary = "Escolha o departamento para seguir no app."
    ),
    Inventory(
        route = "inventory",
        title = "Inventario",
        summary = "Lista de inventarios com responsavel, data e quantidade."
    ),
    Products(
        route = "products",
        title = "Produtos",
        summary = "Consulta simples de produtos com pesquisa e resultados."
    ),
    GlobalSearch(
        route = "global_search",
        title = "Buscar Produtos",
        summary = "Busca com contadores, filtros e tipo de consulta."
    ),
    AssociateTags(
        route = "associate_tags",
        title = "Associar Tags",
        summary = "Associe uma etiqueta a um produto localizado."
    ),
    Invoice(
        route = "invoice",
        title = "Nota Fiscal",
        summary = "Conferencia de nota fiscal com leitura dos itens."
    ),
    Movement(
        route = "movement",
        title = "Movimentacao de Estoque",
        summary = "Conferencia das movimentacoes do estoque."
    ),
    Settings(
        route = "settings",
        title = "Configuracoes",
        summary = "Escolha do coletor e configuracao minima da operacao."
    ),
    FreeRead(
        route = "free_read",
        title = "Leitura Livre",
        summary = "Leitura livre para acompanhar etiquetas em tempo real."
    ),
    Devices(
        route = "devices",
        title = "Dispositivos",
        summary = "Lista de dispositivos disponiveis para conexao."
    );

    companion object {
        val placeholderFlows = listOf(
            AssociateTags,
            Invoice,
            Movement,
            FreeRead,
            Devices
        )
    }
}
