````md
## 🚀 Como clonar o repositório
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
````

---

## 🐳 Como subir o projeto com Docker

Certifique-se de ter o **Docker** instalado.

### ▶️ Subir os containers

```bash
docker-compose up -d
```

### 🛑 Parar os containers

```bash
docker-compose down
```

---

## 🧱 Estrutura do Projeto

O projeto segue uma arquitetura em camadas, separando responsabilidades para facilitar manutenção e escalabilidade.

```text
src/
│
├── config/
│   └── Configurações da aplicação (ex: Spring Security, autenticação, etc.)
│
├── controller/
│   └── Camada responsável por receber as requisições HTTP e retornar respostas
│
├── DTO/
│   ├── request/
│   │   └── Objetos usados para receber dados das requisições
│   └── response/
│       └── Objetos usados para enviar dados nas respostas
│
├── model/
│   └── Entidades do sistema (representação das tabelas do banco)
│
├── repository/
│   └── Interfaces responsáveis pelo acesso ao banco de dados
│
└── service/
    └── Regras de negócio da aplicação
```

---

## 🔄 Fluxo da aplicação

1. O **Controller** recebe a requisição
2. Chama o **Service**
3. O **Service** aplica regras de negócio
4. O **Repository** acessa o banco de dados
5. A resposta retorna passando pelo fluxo inverso

---

# 📘 Documentação API - SorteioOnline

## 🔐 Autenticação — `/auth`

### **[POST] /auth/register**

* **Permissão:** Público

### Body (JSON)

```json
{
  "nome": "string",
  "email": "string",
  "senha": "string"
}
```

### Resposta de sucesso

```json
{
  "nome": "string",
  "email": "string"
}
```

### Status

| Código | Descrição                      |
| ------ | ------------------------------ |
| 200    | Cadastro realizado com sucesso |
| 400    | Campos inválidos ou faltando   |

---

### **[POST] /auth/login**

* **Permissão:** Público

### Body (JSON)

```json
{
  "email": "string",
  "senha": "string"
}
```

### Resposta de sucesso

```json
{
  "token": "string (JWT)"
}
```

### Status

| Código | Descrição              |
| ------ | ---------------------- |
| 200    | Login OK (retorna JWT) |
| 400    | Campos vazios          |
| 401    | Credenciais inválidas  |

---

# 🎲 Sorteios — `/sorteio`

### **[POST] /sorteio**

* **Permissão:** Autenticado

### Body (JSON)

```json
{
  "nome": "string",
  "status": "ativo | encerrado"
}
```

### Resposta de sucesso

```json
{
  "id": "Long",
  "nomeSorteio": "string",
  "statusSorteio": "ativo | encerrado"
}
```

### Status

| Código | Descrição              |
| ------ | ---------------------- |
| 201    | Criado                 |
| 401    | Não autenticado        |
| 500    | Usuário não encontrado |

---

### **[GET] /sorteio/lista_sorteios**

* **Permissão:** Público

### Resposta de sucesso

```json
[
  {
    "id": "Long",
    "nomeSorteio": "string",
    "statusSorteio": "ativo | encerrado"
  }
]
```

### Status

| Código | Descrição         |
| ------ | ----------------- |
| 200    | Lista de sorteios |

---

### **[GET] /sorteio/{id}**

* **Permissão:** Público

### Path Param

```text
id: Long
```

### Resposta de sucesso

```json
{
  "id": "Long",
  "nomeSorteio": "string",
  "statusSorteio": "ativo | encerrado"
}
```

### Status

| Código | Descrição  |
| ------ | ---------- |
| 200    | Encontrado |
| 404    | Não existe |

---

### **[PUT] /sorteio/{id}**

* **Permissão:** Dono / Admin

### Body (JSON)

```json
{
  "nome": "string",
  "status": "ativo | encerrado"
}
```

### Resposta de sucesso

```text
Sorteio atualizado com sucesso!
```

### Status

| Código | Descrição                        |
| ------ | -------------------------------- |
| 200    | Atualizado                       |
| 401    | Não autenticado                  |
| 404    | Não autorizado ou não encontrado |

---

### **[PATCH] /sorteio/encerrado/{id}**

* **Permissão:** Dono / Admin

### Resposta de sucesso

```text
sorteio encerrado
```

### Status

| Código | Descrição       |
| ------ | --------------- |
| 200    | Encerrado       |
| 401    | Não autenticado |
| 500    | Erro interno    |

---

### **[DELETE] /sorteio/{id}**

* **Permissão:** Dono / Admin

### Path Param

```text
id: Long
```

### Resposta de sucesso

```text
User deletado
```

### Status

| Código | Descrição                        |
| ------ | -------------------------------- |
| 200    | Deletado                         |
| 401    | Não autenticado                  |
| 404    | Não autorizado ou não encontrado |

---

### **[GET] /sorteio/list_participantes/{id}**

* **Permissão:** Autenticado

### Path Param

```text
id: Long (idSorteio)
```

### Resposta de sucesso

```json
[
  {
    "id": "Long",
    "nome": "string",
    "email": "string"
  }
]
```

### Status

| Código | Descrição                        |
| ------ | -------------------------------- |
| 200    | Lista de participantes retornada |

---

### **[GET] /sorteio/sortear/{id}**

* **Permissão:** Autenticado

### Path Param

```text
id: Long (idSorteio)
```

### Resposta de sucesso

```json
{
  "id": "Long",
  "nome": "string",
  "email": "string"
}
```

### Status

| Código | Descrição                     |
| ------ | ----------------------------- |
| 200    | Sorteio realizado com sucesso |

---

# 👤 Usuários — `/usuario`

### **[GET] /usuario**

* **Permissão:** Autenticado

### Resposta de sucesso

```json
[
  {
    "id": "Long",
    "nome": "string",
    "email": "string"
  }
]
```

### Status

| Código | Descrição         |
| ------ | ----------------- |
| 200    | Lista de usuários |

---

### **[GET] /usuario/{id}**

* **Permissão:** Autenticado

### Path Param

```text
id: Long
```

### Resposta de sucesso

```json
{
  "id": "Long",
  "nome": "string",
  "email": "string"
}
```

### Status

| Código | Descrição    |
| ------ | ------------ |
| 200    | Encontrado   |
| 500    | Erro interno |

---

### **[PUT] /usuario/{id}**

* **Permissão:** Próprio / Admin

### Body (JSON)

```json
{
  "nome": "string",
  "email": "string",
  "senha": "string"
}
```

### Resposta de sucesso

```text
atualizado
```

### Status

| Código | Descrição       |
| ------ | --------------- |
| 200    | Atualizado      |
| 401    | Não autenticado |
| 500    | Não autorizado  |

---

### **[DELETE] /usuario/{id}**

* **Permissão:** Próprio / Admin

### Path Param

```text
id: Long
```

### Resposta de sucesso

```text
User deletado
```

### Status

| Código | Descrição       |
| ------ | --------------- |
| 200    | Deletado        |
| 401    | Não autenticado |
| 500    | Não autorizado  |
