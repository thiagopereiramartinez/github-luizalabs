# Teste Técnico - Luiza Labs

## Descrição
O aplicativo utiliza a API do GitHub para buscar repositórios mais populares da linguagem **Kotlin** e seus respectivos **Pull Requests** em aberto

### Recursos
- Escrito em **Kotlin 2.1.0**
- Arquitetura **MVVM**
- **Kotlin Coroutines** para programação reativa
- Interface desenvolvida com **Jetpack Compose**
- Injeção de dependências usando **Hilt**
- Suporte offline com cache de imagens e dados da API
- Design baseado em **Material Design 3**
- **Testes unitários e de UI**
- Relatório de cobertura de código gerado com **JaCoCo**

### Bibliotecas Utilizadas
- **Jetpack Compose**
- **Retrofit**
- **Gson**
- **Room**
- **Coil**
- **Robolectric**
- **Mockito**
- **JUnit4**
- **Espresso**

---

## Como Configurar o Projeto
A API do GitHub possui um limite de acessos sem autenticação. Caso o aplicativo comece a exibir erros relacionados ao limite de requisições, é necessário gerar um token pessoal.  

### Gerando um Token Pessoal
Siga as instruções no [guia oficial](https://docs.github.com/pt/rest/authentication/authenticating-to-the-rest-api?apiVersion=2022-11-28#autenticar-com-um-personal-access-token) para criar seu token.

Após gerar o token, configure-o no arquivo `local.properties` adicionando a seguinte linha:

### Passo a Passo para Configuração:

1. **Gerar o token Pessoal**  
   Acesse o [guia oficial do GitHub](https://docs.github.com/pt/rest/authentication/authenticating-to-the-rest-api?apiVersion=2022-11-28#autenticar-com-um-personal-access-token) e siga as instruções para criar o token.  
   
   Certifique-se de que o token gerado tenha as permissões necessárias para acessar repositórios públicos.

2. **Adicionar o Token no Projeto**  
   No diretório raiz do projeto, abra o arquivo `local.properties` (ou crie-o caso não exista) e adicione a seguinte linha:  
   ```properties
   GITHUB_TOKEN="<SEU_TOKEN_AQUI>"
   ```

3. **Salvar e Sincronizar**
    Após adicionar o token, salve o arquivo e sincronize o projeto no Android Studio para garantir que as alterações sejam aplicadas.


## Relatório de cobertura de código
Para gerar o relatório de cobertura de código, execute o seguinte comando no terminal:  
```sh
./gradlew :app:jacocoTestDebugReport
```
Após a execução, um relatório será gerado. Para acessá-lo, abra o arquivo de saída no navegador e visualize os detalhes da cobertura de testes.
