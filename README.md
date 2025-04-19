# 🔍 BuscarNomes - Comparação de Busca Sequencial vs Paralela em Arquivos `.txt`

Este projeto em Java compara o tempo de execução entre dois modos de busca de nomes dentro de arquivos `.txt`:

- ✅ Modo Sequencial
- ⚡ Modo Paralelo (usando ForkJoinPool)

Também permite escolher entre diferentes conjuntos de arquivos (datasets).

---

## 💡 Funcionalidades

- Leitura de múltiplos arquivos `.txt`
- Busca de nomes linha a linha
- Exibição no formato:  
  `datasetG > a1.txt (linha 312): Michele Smith`
- Escolha do conjunto de arquivos:
  - `1 - Padrões (G e P)`
  - `2 - Todos (Com K)`
- Comparação entre:
  - `1 - Modo Sequencial`
  - `2 - Modo Paralelo`

---

## 🚀 Como Executar

### 🪟 Windows

```bash
./compilar.bat
```

### 🐧 Linux/macOS

```bash
chmod +x compilar.sh
./compilar.sh
```

### 📥 Exemplo de uso no console

```
Digite o nome a ser buscado: Michele

Arquivos desejados:
1 - Padrões (G e P)
2 - Todos (Com K)
Opção: 2

Escolha o modo de busca:
1 - Sequencial
2 - Paralelo
Opção: 2
```

---

## 🧱 Tecnologias Utilizadas

- Java 17+
- API `java.nio.file` para leitura eficiente
- `ForkJoinPool` para paralelismo
- Estrutura modular com uso de constantes

---
