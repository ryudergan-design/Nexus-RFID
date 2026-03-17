package com.example.nexusrfid.data.mock

object MockDataSource {
    const val appVersion = "V 1.1.23-2"

    val departments = listOf(
        DepartmentListItem("39952", "NOVOS HORIZONTES MODA FASHION LTDA EP"),
        DepartmentListItem("3T", "VESTUARIO LTDA"),
        DepartmentListItem("42.820.296/0001-37", "COLCCI"),
        DepartmentListItem("47.81-4-00", "COMERCIO VAREJISTA DE ARTIGOS DO VESTUARIO"),
        DepartmentListItem("821", "LOJA OPEN ICARAI"),
        DepartmentListItem("822", "LOJA OPEN BARRA DA TIJUCA"),
        DepartmentListItem("823", "LOJA OPEN DESIGN"),
        DepartmentListItem("824", "LOJA OPEN RIO SUL"),
        DepartmentListItem("825", "LOJA OPEN IPANEMA"),
        DepartmentListItem("826", "LOJA OPEN BH"),
        DepartmentListItem("AMC", "A.M.C. TEXTIL LTDA."),
        DepartmentListItem("FORUM", "A.M.C. TEXTIL LTDA. - OSCAR FREIRE T.D./FORUM"),
        DepartmentListItem("COM", "AMC - COMERCIAL"),
        DepartmentListItem("F75", "AMC - FORUM | SOMMER - 75.364.570/0007-55"),
        DepartmentListItem("ATA", "AMC - OPEN ATACADO"),
        DepartmentListItem("TRI", "AMC - TRITON | REPLAY")
    )

    val inventories = listOf(
        InventoryCardItem(
            title = "INVENTARIO FORUM 2 ATC PRIMA 27 TAR...",
            description = "Inventario com target por epcs",
            dateTime = "16/03/2026 17:12",
            responsible = "Anderson Alves da Silva",
            systemStock = 667
        ),
        InventoryCardItem(
            title = "INVENTARIO FORUM 2 ATC PRIMA 27 - TA...",
            description = "Inventario com target por epcs",
            dateTime = "13/03/2026 16:55",
            responsible = "Anderson Alves da Silva",
            systemStock = 578,
            selected = true
        ),
        InventoryCardItem(
            title = "INVENTARIO FORUM 2 ATC PRIMA 27 MA...",
            description = "Inventario com target por epcs",
            dateTime = "13/03/2026 10:27",
            responsible = "Anderson Alves da Silva",
            systemStock = 514
        ),
        InventoryCardItem(
            title = "MAIKE COLCCI 1 - 13-03",
            description = "Inventario com target por epcs",
            dateTime = "13/03/2026 9:23",
            responsible = "Fernando Silva de Souza",
            systemStock = 954
        ),
        InventoryCardItem(
            title = "INVENTARIO FORUM 2 ATC PRIMA 27 TAR...",
            description = "Inventario com target por epcs",
            dateTime = "11/03/2026 17:01",
            responsible = "Anderson Alves da Silva",
            systemStock = 452
        )
    )

    val drawerItems = listOf(
        DrawerMenuItem("Buscar Produtos", "global_search"),
        DrawerMenuItem("Produtos", "products"),
        DrawerMenuItem("Inventarios", "inventory"),
        DrawerMenuItem(
            label = "Conferencias",
            route = "conferences",
            children = listOf(
                DrawerMenuItem("Nota Fiscal", "invoice"),
                DrawerMenuItem("Movimentacao de Estoque", "movement")
            )
        ),
        DrawerMenuItem("Configuracoes", "settings"),
        DrawerMenuItem("Sair", "login")
    )

    val products = listOf(
        ProductListItem("ACESSORIO EM MACRAME", "064.22.00238"),
        ProductListItem("ACESSORIOS GEOMETRICO 3D", "064.22.00037"),
        ProductListItem("ACRILICO TRIANGULAR CLC", "067.53.00113"),
        ProductListItem("ADESIVO COLCCI DIA DOS PAIS PAC 25 ADES", "067.01.01886"),
        ProductListItem("ADESIVO COLCCI REVENDEDOR", "067.01.02193"),
        ProductListItem("ADESIVO COLCCI SPORT MM", "067.57.00074")
    )
}
