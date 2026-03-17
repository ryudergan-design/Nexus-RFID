package com.example.nexusrfid.ui.navigation

enum class AppDestination(
    val route: String,
    val title: String,
    val summary: String
) {
    Login(
        route = "login",
        title = "Login",
        summary = "Fluxo inicial com logo central, campos simples e botao principal."
    ),
    Departments(
        route = "departments",
        title = "Departamentos",
        summary = "Lista densa de departamentos e empresas com leitura direta."
    ),
    Inventory(
        route = "inventory",
        title = "Inventario",
        summary = "Cards operacionais com responsavel, data e quantidade."
    ),
    Products(
        route = "products",
        title = "Produtos",
        summary = "Busca simples com lista textual de produtos e codigo."
    ),
    GlobalSearch(
        route = "global_search",
        title = "Busca Global",
        summary = "Fluxo com leitura, contadores e selecao do tipo de busca."
    ),
    AssociateTags(
        route = "associate_tags",
        title = "Associar Tags",
        summary = "Busca de produto orientada a associacao de etiqueta."
    ),
    Invoice(
        route = "invoice",
        title = "Nota Fiscal",
        summary = "Estado vazio com leitura de codigo de barras da NF."
    ),
    Movement(
        route = "movement",
        title = "Movimentacao",
        summary = "Conferencia de movimentacao com empty state semelhante a NF."
    ),
    Settings(
        route = "settings",
        title = "Configuracoes",
        summary = "Secoes do leitor, selecao de dispositivo e modais escuros."
    ),
    FreeRead(
        route = "free_read",
        title = "Leitura Livre",
        summary = "Painel tecnico com controles, toggles e linhas SET/GET."
    ),
    Devices(
        route = "devices",
        title = "Dispositivos",
        summary = "Lista utilitaria de dispositivos encontrados."
    );

    companion object {
        val placeholderFlows = listOf(
            Products,
            GlobalSearch,
            AssociateTags,
            Invoice,
            Movement,
            Settings,
            FreeRead,
            Devices
        )
    }
}
