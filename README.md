# Profanator-API
___
API reativa desenvolvida em **Spring WebFlux** com o propósito de fornecer dados relacionados aos itens do **Profane**.

- [Endpoints](https://github.com/J-Player/Profanator-API#endpoints)
- [Dados](https://github.com/J-Player/Profanator-API#dados)
- [Dependências](https://github.com/J-Player/Profanator-API#depend%C3%AAncias)
- [Contato](https://github.com/J-Player/Profanator-API#contato)

# Endpoints
___
Para informações detalhadas sobre os _Endpoints_ da aplicação, acesse o **_Swagger UI_** da API enquanto o servidor
estiver *rodando*.

Sintaxe: http://&lt;host&gt;[:&lt;port&gt;]/swagger-ui.html

Exemplo: http://localhost:8080/swagger-ui.html.

# Dados
___
Como o Profane ainda não tem uma API para desenvolvedores, foi necessário criar todos os dados (itens) da API
manualmente e adiciona-los num **banco de dados pessoal provisório**.

Atualmente, com a versão **Pré-Alpha** do Profane, há um total de **184 itens** cadastrados neste banco de dados.

Os dados, por enquanto, **NÃO** estão incluídos no projeto.

# Dependências
___
```groovy
implementation 'org.springframework.boot:spring-boot-starter-webflux:2.6.7' //SPRING WEBFLUX
implementation 'org.springframework.boot:spring-boot-starter-data-r2dbc:2.6.7' //SPRING R2DBC
implementation 'org.springframework.boot:spring-boot-starter-validation:2.6.7' //SPRING VALIDATION
implementation 'org.springframework.boot:spring-boot-starter-security:2.6.7' //SPRING SECURITY
implementation 'io.r2dbc:r2dbc-postgresql:0.8.12.RELEASE' //R2DBC POSTGRESQL
implementation 'org.springframework.boot:spring-boot-devtools:2.6.7' //SPRING DEV-TOOLS
implementation 'org.springdoc:springdoc-openapi-webflux-ui:1.6.8' //SPRINGDOC OPENAPI WEBFLUX
implementation 'io.projectreactor.netty:reactor-netty:1.0.19' //NETTY
implementation 'org.projectlombok:lombok:1.18.24' //LOMBOK
annotationProcessor 'org.projectlombok:lombok:1.18.24' //LOMBOK
implementation 'org.mapstruct:mapstruct:1.4.2.Final' //MAPSTRUCT
annotationProcessor 'org.mapstruct:mapstruct-processor:1.4.2.Final' //MAPSTRUCT
testImplementation 'io.projectreactor.tools:blockhound:1.0.6.RELEASE' //BLOCKHOUND
testImplementation 'org.springframework.boot:spring-boot-starter-test:2.6.7' //SPRING TEST
testImplementation 'org.springframework.security:spring-security-test:5.6.3' //SPRING SECURITY TEST
testImplementation 'io.projectreactor:reactor-test:3.4.18' //REACTOR TEST
```

# Contato
___
Para mais informações, feedbacks ou dúvidas, contate-me.

**Discord: JP#3185**