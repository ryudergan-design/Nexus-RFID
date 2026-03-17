package com.example.nexusrfid.data.mock

import com.example.nexusrfid.rfid.RfidDevice

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

    val drawerMenuItems = listOf(
        DrawerMenuItem("Buscar Produtos", "global_search"),
        DrawerMenuItem("Configuracoes", "settings"),
        DrawerMenuItem("Sair", "login")
    )

    val searchSummary = SearchCounterSummary(
        readCount = 128,
        foundCount = 46
    )

    val searchTargets = listOf(
        SearchTargetItem(key = "all", label = "Todos"),
        SearchTargetItem(key = "pending", label = "Pendentes", targetState = ProductTargetState.Pending),
        SearchTargetItem(key = "found", label = "Encontrados", targetState = ProductTargetState.Found),
        SearchTargetItem(key = "divergent", label = "Divergentes", targetState = ProductTargetState.Divergent)
    )

    val searchTypes = listOf(
        SearchTypeOption(key = "product", label = "Produto"),
        SearchTypeOption(
            key = "reduced",
            label = "Reduzido",
            dialogTitle = "Codigo reduzido",
            inputPlaceholder = "Digite o codigo reduzido",
            numericInput = true
        ),
        SearchTypeOption(
            key = "ean13",
            label = "Ean-13",
            dialogTitle = "EAN-13",
            inputPlaceholder = "Digite o codigo EAN-13",
            numericInput = true
        ),
        SearchTypeOption(
            key = "tag",
            label = "Tag",
            dialogTitle = "Tag",
            inputPlaceholder = "Digite a tag",
            numericInput = false
        )
    )

    val rfidTagPreviewTargets = listOf(
        RfidTargetPreviewItem(
            epc = "E28069950000502122380001",
            proximityPercent = 84,
            proximityLabel = "Muito perto",
            matchedProductName = "ACESSORIO EM MACRAME"
        ),
        RfidTargetPreviewItem(
            epc = "E28069950000502114210007",
            proximityPercent = 52,
            proximityLabel = "Perto",
            matchedProductName = "BERMUDA SARJA FORUM"
        ),
        RfidTargetPreviewItem(
            epc = "E28069950000502140510010",
            proximityPercent = 18,
            proximityLabel = "Longe",
            matchedProductName = "VESTIDO MIDI OPEN"
        )
    )

    val r6PreviewDevices = listOf(
        RfidDevice(
            name = "R6 Nexus 01",
            address = "00:11:22:33:44:55",
            bonded = true
        ),
        RfidDevice(
            name = "",
            address = "66:77:88:99:AA:BB",
            bonded = true
        )
    )

    val settingsPreviewState = RfidSettingsPreviewState(
        statusMessage = "R6 Nexus 01 conectado.",
        connectedDevice = r6PreviewDevices.first(),
        soundEnabled = true,
        isSearchingDevices = false
    )

    val products = listOf(
        ProductListItem(
            name = "ACESSORIO EM MACRAME",
            code = "064.22.00238",
            reducedCode = "2238",
            ean13 = "7898900022386",
            tagCode = "E28069950000502122380001",
            targetState = ProductTargetState.Pending
        ),
        ProductListItem(
            name = "ACESSORIOS GEOMETRICO 3D",
            code = "064.22.00037",
            reducedCode = "2037",
            ean13 = "7898900020375",
            tagCode = "E28069950000502120370002",
            targetState = ProductTargetState.Found
        ),
        ProductListItem(
            name = "ACRILICO TRIANGULAR CLC",
            code = "067.53.00113",
            reducedCode = "5113",
            ean13 = "7898900051131",
            tagCode = "E28069950000502151130003",
            targetState = ProductTargetState.Pending
        ),
        ProductListItem(
            name = "ADESIVO COLCCI DIA DOS PAIS PAC 25 ADES",
            code = "067.01.01886",
            reducedCode = "1886",
            ean13 = "7898900018865",
            tagCode = "E28069950000502118860004",
            targetState = ProductTargetState.Divergent
        ),
        ProductListItem(
            name = "ADESIVO COLCCI REVENDEDOR",
            code = "067.01.02193",
            reducedCode = "2193",
            ean13 = "7898900021934",
            tagCode = "E28069950000502121930005",
            targetState = ProductTargetState.Found
        ),
        ProductListItem(
            name = "ADESIVO COLCCI SPORT MM",
            code = "067.57.00074",
            reducedCode = "7074",
            ean13 = "7898900070743",
            tagCode = "E28069950000502170740006",
            targetState = ProductTargetState.Pending
        ),
        ProductListItem(
            name = "BERMUDA SARJA FORUM",
            code = "091.11.00421",
            reducedCode = "1421",
            ean13 = "7898901042107",
            tagCode = "E28069950000502114210007",
            targetState = ProductTargetState.Found
        ),
        ProductListItem(
            name = "CALCA JEANS COLCCI SKINNY",
            code = "103.84.01129",
            reducedCode = "1129",
            ean13 = "7898901112909",
            tagCode = "E28069950000502111290008",
            targetState = ProductTargetState.Pending
        ),
        ProductListItem(
            name = "CAMISETA BASICA NEXUS",
            code = "114.20.00088",
            reducedCode = "3088",
            ean13 = "7898901308807",
            tagCode = "E28069950000502130880009",
            targetState = ProductTargetState.Divergent
        ),
        ProductListItem(
            name = "VESTIDO MIDI OPEN",
            code = "154.67.00051",
            reducedCode = "4051",
            ean13 = "7898901405100",
            tagCode = "E28069950000502140510010",
            targetState = ProductTargetState.Found
        )
    )
}
