# WePayU

Sistema de folha de pagamento (Projeto de POO). Gerencia empregados horistas, assalariados e comissionados, com lançamento de cartões de ponto, vendas e taxas sindicais, geração de folha de pagamento, undo/redo e persistência em XML.

## Requisitos

- JDK 17+
- `lib/easyaccept.jar` (incluso no repositório)

## Compilação

```bash
rm -rf out
mkdir out
javac -cp "lib/easyaccept.jar" -d out $(find src -name "*.java")
```

## Execução

Windows (Git Bash):
```bash
java -cp "out;lib/easyaccept.jar" Main
```

Linux/macOS:
```bash
java -cp "out:lib/easyaccept.jar" Main
```

`Main` executa os testes de aceitação (EasyAccept) listados em `tests/`, usando `Facade` como classe sob teste. Para habilitar uma User Story, descomente a linha correspondente em `Main.java`.

## Arquitetura

- **`Facade`** — ponto de entrada. Valida parâmetros, delega a `Sistema`, controla undo/redo e persistência.
- **`Sistema`** — regras de negócio, repositório de empregados, cálculo de folha, undo/redo, serialização XML.
- **`models`** — hierarquia de empregados:

```
Empregado (abstrata)
├── Horista
└── Assalariado
    └── Comissionado
```

- **`DataUtil`** — formatador de data único, reutilizado em todo o projeto.
- **`Exception`** — uma classe de exceção específica para cada erro de domínio (sem exceções genéricas).

## Funcionalidades principais

- Criar, remover, alterar e consultar empregados
- Lançar cartão de ponto, venda e taxa de serviço
- Consultar horas trabalhadas, vendas e taxas em um período
- Calcular (`totalFolha`) e gerar (`rodaFolha`) a folha de pagamento
- Desfazer/refazer operações (`undo`/`redo`)
- Persistência automática em `empregados.xml`

## Persistência

Cada operação que altera dados é salva em `empregados.xml`. O `Facade` recarrega esse arquivo ao ser instanciado; se não existir, o sistema inicia vazio.

## Undo/redo

Baseado em snapshots do estado (`Sistema.salvarParaUndo`), não em reversão de comandos individuais. Se uma operação falhar, o snapshot é descartado via `try/finally`, sem capturar exceções genéricas.

## Princípios de projeto

- Sem `instanceof`: comportamentos que variam por tipo de empregado são métodos virtuais em `Empregado`, sobrescritos nas subclasses.
- Sem exceções genéricas: cada erro de domínio tem sua própria classe.
- Duplicação eliminada: parsing de data (`DataUtil`), parsing de id (`Facade.parseId`), cópia de campos comuns (`Empregado.copiarCamposComuns`).
