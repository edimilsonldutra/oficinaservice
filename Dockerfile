# Estágio 1: Build da Aplicação com Maven e JDK 21
# Usamos uma imagem oficial do Maven com o Eclipse Temurin 21.
# O alias 'AS build' nomeia este estágio para que possamos referenciá-lo mais tarde.
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do container.
WORKDIR /app

# Define o ambiente do container para usar UTF-8, resolvendo problemas de codificação.
ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8

# Copia o arquivo de definição do projeto primeiro.
# Isso aproveita o cache de camadas do Docker. Se o pom.xml não mudar,
# o Maven não precisará baixar as dependências novamente.
COPY pom.xml .

# Baixa todas as dependências do projeto.
RUN mvn dependency:go-offline

# Copia todo o código-fonte da aplicação para o container.
COPY src ./src

# Compila a aplicação e empacota-a num arquivo .jar.
# A codificação já está definida no pom.xml, então o comando é simples.
RUN mvn package -DskipTests


# Estágio 2: Criação da Imagem Final de Execução
# Usamos uma imagem JRE (Java Runtime Environment) baseada em Alpine,
# que é extremamente leve.
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho na imagem final.
WORKDIR /app

# Copia apenas o arquivo .jar compilado do estágio 'build' para a imagem final.
# O arquivo é renomeado para app.jar para facilitar a execução.
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080, que é a porta padrão que o Spring Boot usa.
EXPOSE 8080

# Define o comando que será executado quando o container iniciar.
# Isto executa a aplicação Java.
ENTRYPOINT ["java", "-jar", "app.jar"]