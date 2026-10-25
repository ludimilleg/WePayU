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

`Main` executa em sequência todos os scripts disponíveis das User Stories 1 a 10, incluindo os complementares de persistência (`us1_1.txt` a `us6_1.txt` e `us9_1.txt`/`us10_1.txt`). Os scripts de persistência dependem dos dados gerados pelos scripts principais anteriores.

## Arquitetura

- **`Facade`** — adaptador do contrato EasyAccept: encaminha as operações a `Sistema` e formata resultados.
- **`Sistema`** — valida os parâmetros de entrada, coordena as operações sobre empregados, o histórico transacional e a persistência automática.
- **`AlteradorEmpregado`** — aplica as alterações de empregado, com validação e operações separadas por atributo.
- **`AlteracaoEmpregado`** — representa os dados de uma solicitação de alteração.
- **`HistoricoSistema`** — mantém os snapshots usados para undo/redo; `EstadoSistema` copia os empregados para não expor estado mutável.
- **`PersistenciaXML`** — lê e grava o estado do sistema em XML, mantendo a serialização fora dos modelos.
- **`folha`** — calcula totais e gera a folha de pagamento.
- **`models`** — hierarquia de empregados:

```
Empregado (abstrata)
├── Horista
└── Assalariado
    └── Comissionado
```

- Construtores e setters dos modelos validam seus valores; sindicalização e forma de pagamento são alteradas em conjunto, rejeitando dados parciais sem modificar o estado anterior.
- **`Comissionado`** centraliza a validação da data de pagamento quinzenal, o intervalo de vendas e o cálculo da parcela fixa e da comissão.
- **`DataUtil`** e **`ConversorDecimal`** — centralizam a conversão de datas e números recebidos como texto.
- **`AgendaPagamento`** — interpreta descrições de agendas semanais e mensais, calcula as datas de pagamento e delimita os períodos.
- **`exception`** — uma classe de exceção específica para cada erro de domínio (sem exceções genéricas).

## Funcionalidades principais

- Criar, remover, alterar e consultar empregados
- Criar agendas semanais e mensais, e atribuí-las aos empregados
- Lançar cartão de ponto, venda e taxa de serviço
- Consultar horas trabalhadas, vendas e taxas em um período
- Calcular (`totalFolha`) e gerar (`rodaFolha`) a folha de pagamento
- Desfazer/refazer operações (`undo`/`redo`)
- Persistência automática em `empregados.xml`

## Persistência

Cada operação que altera dados é salva em `empregados.xml` por substituição de um arquivo temporário. Ao ser instanciada, a `Facade` solicita a `Sistema` que carregue esse arquivo; se não existir, o sistema inicia vazio. Se a gravação falhar, a operação lança `FalhaPersistenciaSistemaException`, restaura o estado anterior e preserva a possibilidade de redo.

## Undo/redo

Baseado em snapshots do estado, não em reversão de comandos individuais. A criação, confirmação e descarte dos snapshots são detalhes internos de `Sistema`. Se uma operação falhar, o snapshot é descartado via `try/finally`, sem capturar exceções genéricas.

## Princípios de projeto

- Sem `instanceof`: comportamentos que variam por tipo de empregado são métodos virtuais em `Empregado`, sobrescritos nas subclasses.
- Sem exceções genéricas: cada erro de domínio tem sua própria classe.
- As invariantes internas dos modelos também usam exceções específicas, em vez de `IllegalArgumentException` ou `IllegalStateException`.
- Duplicação reduzida: parsing de data (`DataUtil`), parsing de id (`Sistema.parseId`), conversão decimal (`ConversorDecimal`) e cópia de campos comuns (`Empregado.copiarCamposComuns`).
