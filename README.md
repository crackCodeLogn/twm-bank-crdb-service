## 🏦 Tandem Workflow Manager - Bank CockroachDB Service

### 📌 Overview

This service acts as the **central handler** for managing banking-related data in a locally hosted **CockroachDB**
instance. It provides **RESTful APIs** to perform various operations, including **CRUD and complex compute support** for
banking entities.

### 🗄️ Managed Tables

1. **Bank**
   - 🏛 **Name**
   - 🔢 **IFSC Code** *(Primary Key)* – Equivalent to bank numbers (IFSC = Indian Financial System Code)
   - 🔗 **External ID**
   - 🌍 **Country Code**
   - 📞 **Contact Number**
   - 🏦 **Bank Type** *(Government or Private)*
   - ✅ **Is Active?**


2. **Bank Account**
   - 🏷️ **Name**
   - 🔢 **ID** *(Primary Key)*
   - 🔗 **External ID**
   - 🔑 **Bank IFSC** *(Foreign Key to Bank)*
   - 💳 **Account Number**
   - 🔢 **Transit Number**
   - 🏢 **Institution Number**
   - 💰 **Current Balance**
   - 📈 **Interest Rate**
   - 🏷️ **Tags** – List of tags for account types, helping distinguish characteristics
   - 💳 **Max Overdraft Balance Allowed**
   - ✅ **Is Active?**


3. **Bank Fixed Deposit**

   - 🔢 **FD Number** *(Logical Composite Key)*
   - 👤 **Direct User**
   - 👥 **Original User**
   - 🆔 **Customer ID** *(of Direct User)*
   - 🔑 **Bank IFSC** *(Logical Foreign Key to Bank - Not a Hard Key Yet)*
   - 🔗 **External ID**
   - 💰 **Principal Amount Deposited**
   - 📈 **Rate of Interest**
   - 📅 **Start Date**
   - 📅 **End Date**
   - 💵 **Expected Amount**
   - 💸 **Expected Interest** *(Expected Amount - Principal)*
   - 🗓️ **Number of Months**
   - 📅 **Number of Days Over Months**
   - 🧐 **Interest Type** – *On Maturity, Annual, or Other*
   - 👤 **Nominee**
   - ✅ **Is Active?**
   - 💡 **Type of Deposit** – *TFSA 🇨🇦 / FHSA 🇨🇦 / Non-Registered 🇨🇦 / India 🇮🇳*
   - 🔒 **Freeze the Expected Amount?**

Note: Fixed deposit is also known as

- 🇺🇸 **Certificate of Deposit (USA)**
- 🇨🇦 **Guaranteed Investment Certificate (Canada)**
- 🇮🇳 **Fixed Deposit (India)**

### ⚙️ Features

✔ **CRUD and complex operations** on banking entities  
✔ **RESTful API endpoints** for seamless integration  
✔ **CockroachDB-backed** storage for high availability and scalability

---

## 🚀 Versions

| Stable     | Latest |
|------------|--------|
| **2.1.29** | 2.1.29 |

## 🛠️ Tech Stack

![Java](https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=openjdk)  
![CockroachDB](https://img.shields.io/badge/Database-CockroachDB-green?style=for-the-badge)  
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-brightgreen?style=for-the-badge&logo=spring)  
![Spring JPA](https://img.shields.io/badge/Spring%20JPA-Enabled-orange?style=for-the-badge)  
![Feign Client](https://img.shields.io/badge/Feign%20Client-REST-red?style=for-the-badge)  
![Eureka](https://img.shields.io/badge/Eureka%20Client-Service%20Discovery-blueviolet?style=for-the-badge)  
![Protobuf](https://img.shields.io/badge/Protobuf-Protocol%20Buffers-red?style=for-the-badge)  
![Guava](https://img.shields.io/badge/Guava-Google%20Library-yellow?style=for-the-badge)  
![Gson](https://img.shields.io/badge/Gson-JSON%20Parsing-lightgrey?style=for-the-badge)  
![Apache Commons Lang3](https://img.shields.io/badge/Apache%20Commons%20Lang3-Utilities-orange?style=for-the-badge)  
![Lombok](https://img.shields.io/badge/Lombok-Reduces%20Boilerplate-red?style=for-the-badge)

## 📚 Personal Libraries

- 🏗️ **[twm-artifactory](https://github.com/crackCodeLogn/twm-artifactory/)**  
  *Centralized repository for source and compiled Protocol Buffers (Protobufs) used across TWM projects.*

- 🔗 **[twm-ping-client](https://github.com/crackCodeLogn/twm-ping-client/)**  
  *A lightweight library providing Eureka-based heartbeat (ping) functionality for service discovery.*

## 🧪 Test Libraries

![Mockito](https://img.shields.io/badge/Mockito-Mocking-blue?style=for-the-badge&logo=java)  
![JUnit](https://img.shields.io/badge/JUnit-Testing-red?style=for-the-badge&logo=java)  
![AssertJ](https://img.shields.io/badge/AssertJ-Fluent%20Assertions-orange?style=for-the-badge)  
![Spring Boot Test](https://img.shields.io/badge/Spring%20Boot%20Test-Integration-brightgreen?style=for-the-badge&logo=spring)


---

## Launch mechanism

1. 