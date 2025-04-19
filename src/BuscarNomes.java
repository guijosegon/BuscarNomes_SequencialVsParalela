import Consts.Mensagens;
import Consts.Campos;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class BuscarNomes {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print(Mensagens.DIGITE_NOME);
        String nomeBuscado = scanner.nextLine();

        System.out.print(Mensagens.ESCOLHA_ARQUIVOS);
        int opcaoArquivos = scanner.nextInt();
        scanner.nextLine();

        List<Path> arquivos;
        try {
            List<Path> arquivosSelecionados = new ArrayList<>();
            Path base = Paths.get(Campos.CAMINHO_ARQUIVOS);
            Path datasetG = base.resolve("datasetG");
            Path datasetP = base.resolve("datasetP");
            Path datasetK = base.resolve("datasetK");

            if (opcaoArquivos == 1) {
                if (Files.exists(datasetG))
                    arquivosSelecionados.addAll(Files.walk(datasetG).filter(p -> p.toString().endsWith(".txt")).toList());
                if (Files.exists(datasetP))
                    arquivosSelecionados.addAll(Files.walk(datasetP).filter(p -> p.toString().endsWith(".txt")).toList());
            } else if (opcaoArquivos == 2) {
                if (Files.exists(datasetG))
                    arquivosSelecionados.addAll(Files.walk(datasetG).filter(p -> p.toString().endsWith(".txt")).toList());
                if (Files.exists(datasetP))
                    arquivosSelecionados.addAll(Files.walk(datasetP).filter(p -> p.toString().endsWith(".txt")).toList());
                if (Files.exists(datasetK))
                    arquivosSelecionados.addAll(Files.walk(datasetK).filter(p -> p.toString().endsWith(".txt")).toList());
            } else {
                System.out.println("Opção inválida. Usando apenas Padrões.");
                arquivosSelecionados.addAll(Files.walk(datasetG).filter(p -> p.toString().endsWith(".txt")).toList());
                arquivosSelecionados.addAll(Files.walk(datasetP).filter(p -> p.toString().endsWith(".txt")).toList());
            }

            arquivos = arquivosSelecionados;
        } catch (IOException e) {
            System.out.println("Erro ao acessar os arquivos: " + e.getMessage());
            return;
        }

        System.out.print(Mensagens.ESCOLHA_MODO);
        int escolha = scanner.nextInt();

        switch (escolha) {
            case 1 -> executarSequencial(arquivos, nomeBuscado);
            case 2 -> executarParalelo(arquivos, nomeBuscado);
            default -> System.out.println(Mensagens.OPCAO_INVALIDA);
        }
    }

    private static void executarSequencial(List<Path> arquivos, String nomeBuscado) {
        long inicio = System.nanoTime();

        for (Path arquivo : arquivos) {
            buscarNomeNoArquivo(arquivo.toFile(), nomeBuscado);
        }

        long fim = System.nanoTime();
        System.out.printf("Tempo total (sequencial): %.3f ms%n", (fim - inicio) / 1_000_000.0);
    }

    private static void executarParalelo(List<Path> arquivos, String nomeBuscado) {
        ForkJoinPool pool = new ForkJoinPool();
        long inicio = System.nanoTime();

        TarefaBuscaBloco tarefa = new TarefaBuscaBloco(arquivos, nomeBuscado);
        pool.invoke(tarefa);

        long fim = System.nanoTime();
        System.out.printf("Tempo total (paralelo): %.3f ms%n", (fim - inicio) / 1_000_000.0);

        pool.shutdown();
    }

    static class TarefaBuscaBloco extends RecursiveAction {
        private final List<Path> arquivos;
        private final String nomeBuscado;

        public TarefaBuscaBloco(List<Path> arquivos, String nomeBuscado) {
            this.arquivos = arquivos;
            this.nomeBuscado = nomeBuscado;
        }

        @Override
        protected void compute() {
            if (arquivos.size() <= Campos.LIMITE_ARQUIVOS_POR_TAREFA) {
                for (Path arquivo : arquivos) {
                    buscarNomeNoArquivo(arquivo.toFile(), nomeBuscado);
                }
            } else {
                int meio = arquivos.size() / 2;
                invokeAll(
                    new TarefaBuscaBloco(arquivos.subList(0, meio), nomeBuscado),
                    new TarefaBuscaBloco(arquivos.subList(meio, arquivos.size()), nomeBuscado)
                );
            }
        }
    }

    private static void buscarNomeNoArquivo(File arquivo, String nome) {
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            int numeroLinha = 1;
            String dataset = arquivo.getParentFile().getName();

            while ((linha = leitor.readLine()) != null) {
                if (linha.contains(nome)) {
                    System.out.printf("%s > " + Mensagens.ENCONTRADO,
                            dataset, arquivo.getName(), numeroLinha, linha.trim());
                }
                numeroLinha++;
            }

        } catch (IOException e) {
            System.out.println(Mensagens.ERRO_ARQUIVO + arquivo.getName());
        }
    }
}